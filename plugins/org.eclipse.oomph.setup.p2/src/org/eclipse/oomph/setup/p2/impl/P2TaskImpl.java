/*
 * Copyright (c) 2014-2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.p2.impl;

import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.CertificateConfirmer;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.p2.internal.core.CacheUsageConfirmer;
import org.eclipse.oomph.p2.internal.core.CachingRepositoryManager;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.internal.p2.SetupP2Plugin;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Package;
import org.eclipse.oomph.setup.p2.util.P2TaskUISevices;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.Confirmer.Confirmation;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.internal.p2.metadata.repository.MetadataRepositoryManager;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.ProvisioningListener;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.IRepositoryReference;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Install Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#isLicenseConfirmationDisabled <em>License Confirmation Disabled</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#isMergeDisabled <em>Merge Disabled</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.p2.impl.P2TaskImpl#getProfileProperties <em>Profile Properties</em>}</li>
 * </ul>
 *
 * @generated
 */
public class P2TaskImpl extends SetupTaskImpl implements P2Task
{
  private static final boolean SKIP = PropertiesUtil.isProperty(PROP_SKIP);

  // This is used only for documentation capture.
  private static final boolean FORCE = PropertiesUtil.isProperty("oomph.setup.p2.force"); //$NON-NLS-1$

  private static final Object FIRST_CALL_DETECTION_KEY = new Object();

  /**
   * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabel()
   * @generated
   * @ordered
   */
  protected static final String LABEL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabel()
   * @generated
   * @ordered
   */
  protected String label = LABEL_EDEFAULT;

  /**
   * The cached value of the '{@link #getRequirements() <em>Requirements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRequirements()
   * @generated
   * @ordered
   */
  protected EList<Requirement> requirements;

  /**
   * The cached value of the '{@link #getRepositories() <em>Repositories</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepositories()
   * @generated
   * @ordered
   */
  protected EList<Repository> repositories;

  /**
   * The default value of the '{@link #isLicenseConfirmationDisabled() <em>License Confirmation Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLicenseConfirmationDisabled()
   * @generated
   * @ordered
   */
  protected static final boolean LICENSE_CONFIRMATION_DISABLED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isLicenseConfirmationDisabled() <em>License Confirmation Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isLicenseConfirmationDisabled()
   * @generated
   * @ordered
   */
  protected boolean licenseConfirmationDisabled = LICENSE_CONFIRMATION_DISABLED_EDEFAULT;

  /**
   * The default value of the '{@link #isMergeDisabled() <em>Merge Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isMergeDisabled()
   * @generated
   * @ordered
   */
  protected static final boolean MERGE_DISABLED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isMergeDisabled() <em>Merge Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isMergeDisabled()
   * @generated
   * @ordered
   */
  protected boolean mergeDisabled = MERGE_DISABLED_EDEFAULT;

  /**
   * The default value of the '{@link #getProfileProperties() <em>Profile Properties</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProfileProperties()
   * @generated
   * @ordered
   */
  protected static final String PROFILE_PROPERTIES_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProfileProperties() <em>Profile Properties</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProfileProperties()
   * @generated
   * @ordered
   */
  protected String profileProperties = PROFILE_PROPERTIES_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected P2TaskImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return SetupP2Package.Literals.P2_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLabel(String newLabel)
  {
    String oldLabel = label;
    label = newLabel;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupP2Package.P2_TASK__LABEL, oldLabel, label));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Requirement> getRequirements()
  {
    if (requirements == null)
    {
      requirements = new EObjectContainmentEList<Requirement>(Requirement.class, this, SetupP2Package.P2_TASK__REQUIREMENTS);
    }
    return requirements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Repository> getRepositories()
  {
    if (repositories == null)
    {
      repositories = new EObjectContainmentEList<Repository>(Repository.class, this, SetupP2Package.P2_TASK__REPOSITORIES);
    }
    return repositories;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isLicenseConfirmationDisabled()
  {
    return licenseConfirmationDisabled;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLicenseConfirmationDisabled(boolean newLicenseConfirmationDisabled)
  {
    boolean oldLicenseConfirmationDisabled = licenseConfirmationDisabled;
    licenseConfirmationDisabled = newLicenseConfirmationDisabled;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED, oldLicenseConfirmationDisabled,
          licenseConfirmationDisabled));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isMergeDisabled()
  {
    return mergeDisabled;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMergeDisabled(boolean newMergeDisabled)
  {
    boolean oldMergeDisabled = mergeDisabled;
    mergeDisabled = newMergeDisabled;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupP2Package.P2_TASK__MERGE_DISABLED, oldMergeDisabled, mergeDisabled));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getProfileProperties()
  {
    return profileProperties;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProfileProperties(String newProfileProperties)
  {
    String oldProfileProperties = profileProperties;
    profileProperties = newProfileProperties;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupP2Package.P2_TASK__PROFILE_PROPERTIES, oldProfileProperties, profileProperties));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        return ((InternalEList<?>)getRequirements()).basicRemove(otherEnd, msgs);
      case SetupP2Package.P2_TASK__REPOSITORIES:
        return ((InternalEList<?>)getRepositories()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__LABEL:
        return getLabel();
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        return getRequirements();
      case SetupP2Package.P2_TASK__REPOSITORIES:
        return getRepositories();
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
        return isLicenseConfirmationDisabled();
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        return isMergeDisabled();
      case SetupP2Package.P2_TASK__PROFILE_PROPERTIES:
        return getProfileProperties();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__LABEL:
        setLabel((String)newValue);
        return;
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        getRequirements().clear();
        getRequirements().addAll((Collection<? extends Requirement>)newValue);
        return;
      case SetupP2Package.P2_TASK__REPOSITORIES:
        getRepositories().clear();
        getRepositories().addAll((Collection<? extends Repository>)newValue);
        return;
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
        setLicenseConfirmationDisabled((Boolean)newValue);
        return;
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        setMergeDisabled((Boolean)newValue);
        return;
      case SetupP2Package.P2_TASK__PROFILE_PROPERTIES:
        setProfileProperties((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__LABEL:
        setLabel(LABEL_EDEFAULT);
        return;
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        getRequirements().clear();
        return;
      case SetupP2Package.P2_TASK__REPOSITORIES:
        getRepositories().clear();
        return;
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
        setLicenseConfirmationDisabled(LICENSE_CONFIRMATION_DISABLED_EDEFAULT);
        return;
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        setMergeDisabled(MERGE_DISABLED_EDEFAULT);
        return;
      case SetupP2Package.P2_TASK__PROFILE_PROPERTIES:
        setProfileProperties(PROFILE_PROPERTIES_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SetupP2Package.P2_TASK__LABEL:
        return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
      case SetupP2Package.P2_TASK__REQUIREMENTS:
        return requirements != null && !requirements.isEmpty();
      case SetupP2Package.P2_TASK__REPOSITORIES:
        return repositories != null && !repositories.isEmpty();
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
        return licenseConfirmationDisabled != LICENSE_CONFIRMATION_DISABLED_EDEFAULT;
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        return mergeDisabled != MERGE_DISABLED_EDEFAULT;
      case SetupP2Package.P2_TASK__PROFILE_PROPERTIES:
        return PROFILE_PROPERTIES_EDEFAULT == null ? profileProperties != null : !PROFILE_PROPERTIES_EDEFAULT.equals(profileProperties);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (label: "); //$NON-NLS-1$
    result.append(label);
    result.append(", licenseConfirmationDisabled: "); //$NON-NLS-1$
    result.append(licenseConfirmationDisabled);
    result.append(", mergeDisabled: "); //$NON-NLS-1$
    result.append(mergeDisabled);
    result.append(", profileProperties: "); //$NON-NLS-1$
    result.append(profileProperties);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getPriority()
  {
    return PRIORITY_INSTALLATION;
  }

  @Override
  public Object getOverrideToken()
  {
    if (isMergeDisabled())
    {
      return super.getOverrideToken();
    }

    // This ensures that he p2 tasks with different excluded triggers don't merge early.
    // In SetupTaskPerformer.getSubstitutions(EList<SetupTask>), this list is modified to ensure that merging happens later.
    List<Object> token = new ArrayList<Object>();
    token.add(getClass());
    token.addAll(getTriggers());
    return token;
  }

  @Override
  public void overrideFor(SetupTask overriddenSetupTask)
  {
    super.overrideFor(overriddenSetupTask);

    P2Task overriddenP2Task = (P2Task)overriddenSetupTask;
    getRequirements().addAll(overriddenP2Task.getRequirements());
    getRepositories().addAll(overriddenP2Task.getRepositories());

    String overriddenProfileProperties = overriddenP2Task.getProfileProperties();
    if (!StringUtil.isEmpty(overriddenProfileProperties))
    {
      String profileProperties = getProfileProperties();
      if (StringUtil.isEmpty(profileProperties))
      {
        setProfileProperties(overriddenProfileProperties);
      }
      else
      {
        setProfileProperties(profileProperties + "," + overriddenProfileProperties); //$NON-NLS-1$
      }
    }

    String overriddenLabel = overriddenP2Task.getLabel();
    if (!StringUtil.isEmpty(overriddenLabel))
    {
      String label = getLabel();
      if (!StringUtil.isEmpty(label))
      {
        List<String> labelSegments = new UniqueEList<String>(SegmentSequence.create(" + ", overriddenLabel).segmentsList()); //$NON-NLS-1$
        labelSegments.addAll(SegmentSequence.create(" + ", label).segmentsList()); //$NON-NLS-1$
        overriddenLabel = SegmentSequence.create(" + ", labelSegments.toArray(new String[labelSegments.size()])).toString(); //$NON-NLS-1$
      }

      setLabel(overriddenLabel);
    }
  }

  @Override
  public void consolidate()
  {
    Set<String> installableUnitKeys = new HashSet<String>();
    for (Iterator<Requirement> it = getRequirements().iterator(); it.hasNext();)
    {
      Requirement requirement = it.next();
      String name = requirement.getName();
      if (StringUtil.isEmpty(name) || !installableUnitKeys.add(name + "->" + requirement.getVersionRange().toString())) //$NON-NLS-1$
      {
        it.remove();
      }
    }

    EList<Repository> repositories = getRepositories();
    Set<String> repositoryKeys = new HashSet<String>();
    Set<Repository> releaseTrainAlternates = new HashSet<Repository>();
    boolean containsReleaseTrain = false;
    for (Iterator<Repository> it = repositories.iterator(); it.hasNext();)
    {
      Repository repository = it.next();
      if (repository.getAnnotation(AnnotationConstants.ANNOTATION_RELEASE_TRAIN) != null)
      {
        containsReleaseTrain = true;
      }

      String url = repository.getURL();
      if (StringUtil.isEmpty(url) || !repositoryKeys.add(url))
      {
        it.remove();
      }
      else if (repository.getAnnotation(AnnotationConstants.ANNOTATION_RELEASE_TRAIN_ALTERNATE) != null)
      {
        releaseTrainAlternates.add(repository);
      }
    }

    if (containsReleaseTrain)
    {
      repositories.removeAll(releaseTrainAlternates);
    }
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 100;
  }

  private void addUnknownRepository(IRepositoryManager<?> repositoryManager, Set<String> knownRepositories, String url)
  {
    if (!knownRepositories.contains(url))
    {
      try
      {
        repositoryManager.addRepository(new URI(url));
      }
      catch (Exception ex)
      {
        SetupP2Plugin.INSTANCE.log(ex);
      }
    }
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    if (SKIP)
    {
      return FORCE;
    }

    Trigger trigger = context.getTrigger();
    if (trigger == Trigger.BOOTSTRAP)
    {
      return true;
    }

    if (context.isSelfHosting())
    {
      return false;
    }

    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    Profile profile = agent.getCurrentProfile();
    if (profile == null)
    {
      // We're most likely in self hosting mode, where software updates are not really well supported.
      return FORCE;
    }

    IMetadataRepositoryManager metadataRepositoryManager = agent.getMetadataRepositoryManager();
    IArtifactRepositoryManager artifactRepositoryManager = agent.getArtifactRepositoryManager();

    Set<String> knownMetadataRepositories = P2Util.getKnownRepositories(metadataRepositoryManager);
    Set<String> knownArtifactRepositories = P2Util.getKnownRepositories(artifactRepositoryManager);

    for (Repository repository : getRepositories())
    {
      String url = repository.getURL();
      addUnknownRepository(metadataRepositoryManager, knownMetadataRepositories, url);
      addUnknownRepository(artifactRepositoryManager, knownArtifactRepositories, url);
    }

    if (trigger == Trigger.MANUAL)
    {
      return true;
    }

    Map<String, String> currentProfileProperties = getProfileProperties(agent);
    Map<String, String> profilePropertiesMap = P2Util.toProfilePropertiesMap(getProfileProperties());
    for (Map.Entry<String, String> entry : profilePropertiesMap.entrySet())
    {
      String key = entry.getKey();
      String value = entry.getValue();
      if (!ObjectUtil.equals(currentProfileProperties.get(key), value))
      {
        return true;
      }
    }

    Set<Requirement> unsatisifiedRequirements = new LinkedHashSet<Requirement>();
    for (Requirement requirement : getRequirements())
    {
      if (context.matchesFilterContext(requirement.getFilter()) && !requirement.isOptional())
      {
        IQueryResult<IInstallableUnit> result = profile.query(QueryUtil.createMatchQuery(requirement.toIRequirement().getMatches()), null);
        if (result.isEmpty())
        {
          unsatisifiedRequirements.add(requirement);
        }
      }
    }

    P2TaskUISevices p2TaskUISevices = (P2TaskUISevices)context.get(P2TaskUISevices.class);
    if (trigger == Trigger.STARTUP && !unsatisifiedRequirements.isEmpty() && p2TaskUISevices != null)
    {
      return p2TaskUISevices.handleUnsatisfiedRequirements(unsatisifiedRequirements, getInstalledUnits(agent));
    }

    return !unsatisifiedRequirements.isEmpty();
  }

  public void perform(final SetupTaskContext context) throws Exception
  {
    boolean offline = context.isOffline();
    context.log(Messages.P2TaskImpl_offline + " = " + offline); //$NON-NLS-1$

    boolean mirrors = context.isMirrors();
    context.log(Messages.P2TaskImpl_mirrors + " = " + mirrors); //$NON-NLS-1$

    String profileProperties = getProfileProperties();
    if (!StringUtil.isEmpty(profileProperties))
    {
      context.log(Messages.P2TaskImpl_profileProperties + " = " + profileProperties); //$NON-NLS-1$
    }

    EList<Requirement> requirements = getRequirements();
    EList<Repository> repositories = getRepositories();

    context.log((requirements.size() == 1 ? Messages.P2TaskImpl_resolvingRequirement : NLS.bind(Messages.P2TaskImpl_resolvingRequirements, requirements.size()))
        + " " + (repositories.size() == 1 ? Messages.P2TaskImpl_fromRepository : NLS.bind(Messages.P2TaskImpl_fromRepositories, repositories.size())) //$NON-NLS-1$
        + NLS.bind(Messages.P2TaskImpl_resolvingRequirements_to, context.getProductLocation()));

    for (Requirement requirement : requirements)
    {
      context.log(NLS.bind(Messages.P2TaskImpl_requirement, requirement));
    }

    for (Repository repository : repositories)
    {
      context.log(NLS.bind(Messages.P2TaskImpl_repository, repository));
    }

    Profile profile = getProfile(context);
    ProfileTransaction transaction = profile.change();

    ProfileDefinition profileDefinition = transaction.getProfileDefinition();
    profileDefinition.setRequirements(requirements);
    profileDefinition.setRepositories(repositories);
    profileDefinition.setProfileProperties(getProfileProperties());

    ProfileTransaction.CommitContext commitContext = new ProfileTransaction.CommitContext()
    {
      @Override
      public boolean handleProvisioningPlan(ResolutionInfo info) throws CoreException
      {
        try
        {
          processLicenses(context, info.getProvisioningPlan(), context.getProgressMonitor(false));
        }
        catch (Exception ex)
        {
          SetupP2Plugin.INSTANCE.coreException(ex);
        }

        return true;
      }

      @Override
      public ProvisioningContext createProvisioningContext(ProfileTransaction transaction, IProfileChangeRequest profileChangeRequest) throws CoreException
      {
        ProvisioningContext result;
        if (context.get(Resolution.class) != null)
        {
          final IProvisioningAgent provisioningAgent = transaction.getProfile().getAgent().getProvisioningAgent();
          result = new ProvisioningContext(provisioningAgent)
          {
            private URI[] metadataRepositories;

            @Override
            public IQueryable<IInstallableUnit> getMetadata(IProgressMonitor monitor)
            {
              IMetadataRepositoryManager repositoryManager = provisioningAgent.getService(IMetadataRepositoryManager.class);
              Set<IMetadataRepository> loadedRepositories = new LinkedHashSet<IMetadataRepository>();
              if (metadataRepositories != null && repositoryManager instanceof MetadataRepositoryManager)
              {
                MetadataRepositoryManager metadataRepositoryManager = (MetadataRepositoryManager)repositoryManager;
                for (URI uri : metadataRepositories)
                {
                  IMetadataRepository repository = metadataRepositoryManager.getRepository(uri);
                  loadedRepositories.add(repository);

                  Collection<IRepositoryReference> references = repository.getReferences();
                  for (IRepositoryReference reference : references)
                  {
                    URI location = reference.getLocation();
                    IMetadataRepository referencedRepository = metadataRepositoryManager.getRepository(location);
                    loadedRepositories.add(referencedRepository);
                  }
                }

                if (!loadedRepositories.contains(null))
                {
                  return QueryUtil.compoundQueryable(loadedRepositories);
                }
              }

              return super.getMetadata(monitor);
            }

            @Override
            public void setMetadataRepositories(URI... metadataRepositories)
            {
              this.metadataRepositories = metadataRepositories;
              super.setMetadataRepositories(metadataRepositories);
            }
          };
        }
        else
        {
          result = super.createProvisioningContext(transaction, profileChangeRequest);
        }

        result.setProperty(ProvisioningContext.FOLLOW_REPOSITORY_REFERENCES, Boolean.TRUE.toString());
        return result;
      }

      @Override
      public Confirmer getUnsignedContentConfirmer()
      {
        return (Confirmer)context.get(Certificate.class);
      }

      @Override
      public CertificateConfirmer getCertficateConfirmer()
      {
        return (CertificateConfirmer)context.get(CertificateConfirmer.class);
      }
    };

    transaction.setMirrors(mirrors);

    IProvisioningAgent provisioningAgent = profile.getAgent().getProvisioningAgent();

    CacheUsageConfirmer cacheUsageConfirmer = (CacheUsageConfirmer)context.get(CacheUsageConfirmer.class);
    CacheUsageConfirmer oldCacheUsageConfirmer = (CacheUsageConfirmer)provisioningAgent.getService(CacheUsageConfirmer.SERVICE_NAME);

    IProvisioningEventBus eventBus = null;
    ProvisioningListener provisioningListener = (ProvisioningListener)context.get(ProvisioningListener.class);
    if (provisioningListener != null)
    {
      eventBus = (IProvisioningEventBus)provisioningAgent.getService(IProvisioningEventBus.SERVICE_NAME);
      if (eventBus != null)
      {
        eventBus.addListener(provisioningListener);
      }
    }

    boolean originalBetterMirrorSelection = CachingRepositoryManager.enableBetterMirrorSelection();

    try
    {
      if (cacheUsageConfirmer != null)
      {
        provisioningAgent.registerService(CacheUsageConfirmer.SERVICE_NAME, cacheUsageConfirmer);
      }

      if (context.get(Resolution.class) != null)
      {
        Resolution resolution = transaction.resolve(commitContext, context.getProgressMonitor(true));
        context.put(Resolution.class, resolution);
      }
      else
      {
        boolean profileChanged = transaction.commit(commitContext, context.getProgressMonitor(true));
        if (context.getTrigger() != Trigger.BOOTSTRAP)
        {
          if (profileChanged)
          {
            context.setRestartNeeded(Messages.P2TaskImpl_newSoftwareInstalled);
          }
          else
          {
            context.log(Messages.P2TaskImpl_noUpdatesAvailable);
          }
        }
        else
        {
          context.put(Profile.class, profile);
        }
      }
    }
    finally
    {
      CachingRepositoryManager.setBetterMirrorSelection(originalBetterMirrorSelection);

      if (eventBus != null)
      {
        eventBus.removeListener(provisioningListener);
      }

      if (cacheUsageConfirmer != null)
      {
        provisioningAgent.unregisterService(CacheUsageConfirmer.SERVICE_NAME, cacheUsageConfirmer);
        if (oldCacheUsageConfirmer != null)
        {
          provisioningAgent.registerService(CacheUsageConfirmer.SERVICE_NAME, oldCacheUsageConfirmer);
        }
      }
    }
  }

  private Profile getProfile(final SetupTaskContext context) throws Exception
  {
    Profile profile;
    if (context.getTrigger() == Trigger.BOOTSTRAP)
    {
      BundlePool bundlePool;

      String bundlePoolLocation = (String)context.get(AgentManager.PROP_BUNDLE_POOL_LOCATION);
      boolean sharedPool;
      File productLocation = context.getProductLocation();
      if (bundlePoolLocation != null && !AgentManager.BUNDLE_POOL_LOCATION_NONE.equalsIgnoreCase(bundlePoolLocation))
      {
        sharedPool = true;
        bundlePool = P2Util.getAgentManager().getBundlePool(new File(bundlePoolLocation));

        // TODO Remove the following two lines after bug 485018 has been fixed.
        File eclipseExtensionFeaturesFolder = new File(bundlePoolLocation, ".eclipseextension/features"); //$NON-NLS-1$
        eclipseExtensionFeaturesFolder.mkdirs();
      }
      else
      {
        sharedPool = false;
        File agentLocation = new File(productLocation, "p2"); //$NON-NLS-1$
        Agent agent = P2Util.createAgent(agentLocation);
        bundlePool = agent.addBundlePool(agentLocation.getParentFile());
      }

      IProvisioningAgent currentAgent = P2Util.getCurrentProvisioningAgent();
      Agent agent = bundlePool.getAgent();
      agent.getProvisioningAgent().registerService(IProvisioningAgent.INSTALLER_AGENT, currentAgent);

      profile = agent.getProfile(productLocation);
      if (profile != null && context.put(FIRST_CALL_DETECTION_KEY, Boolean.TRUE) == null)
      {
        profile.delete(true);
        profile = null;
      }

      String profileID;
      if (profile == null)
      {
        String baseProfileID = IOUtil.encodeFileName(productLocation.toString());
        profileID = baseProfileID;
        profile = agent.getProfile(profileID);
        for (int i = 1; profile != null; ++i)
        {
          profileID = baseProfileID + "_" + i; //$NON-NLS-1$
          profile = agent.getProfile(profileID);
        }
      }
      else
      {
        profileID = profile.getProfileId();
      }

      if (profile == null)
      {
        OS os = context.getOS();

        ProfileCreator profileCreator = bundlePool.addProfile(profileID, Profile.TYPE_INSTALLATION);
        profileCreator.setInstallFeatures(true);
        profileCreator.setInstallFolder(productLocation);
        profileCreator.addOS(os.getOsgiOS());
        profileCreator.addWS(os.getOsgiWS());
        profileCreator.addArch(os.getOsgiArch());
        profileCreator.setRoaming(true);

        // This property is used in org.eclipse.oomph.p2.internal.core.LazyProfile.setProperty(String, String)
        // to ensure that when org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry.updateRoamingProfile(Profile)
        // updates the profile's install location, it doesn't also replace the shared pool location with a local pool.
        // Of course if this is an installation with a local pool, it should be replace during a roaming update.
        profileCreator.set(Profile.PROP_PROFILE_SHARED_POOL, sharedPool);

        profile = profileCreator.create();

        // @patch mhoffrog
        Map<String, String> filterProperties = context.getFilterProperties();
        for (String key : filterProperties.keySet())
        {
          profile.getProperties().putIfAbsent(key, filterProperties.get(key));
        }

      }

      UIServices uiServices = (UIServices)context.get(UIServices.class);
      if (uiServices != null)
      {
        IProvisioningAgent provisioningAgent = profile.getAgent().getProvisioningAgent();
        provisioningAgent.registerService(UIServices.SERVICE_NAME, uiServices);
      }

      return profile;
    }
    else
    {
      Agent agent = P2Util.getAgentManager().getCurrentAgent();
      profile = agent.getCurrentProfile();
    }

    return profile;
  }

  private void processLicenses(final SetupTaskContext context, IProvisioningPlan provisioningPlan, IProgressMonitor monitor) throws Exception
  {
    if (isLicenseConfirmationDisabled())
    {
      return;
    }

    User user = context.getUser();
    Confirmer licenseConfirmer = (Confirmer)context.get(ILicense.class);

    processLicenses(provisioningPlan, licenseConfirmer, user, false, monitor);
  }

  public static void processLicenses(IProvisioningPlan provisioningPlan, Confirmer licenseConfirmer, final User user, boolean saveChangedUser,
      IProgressMonitor monitor)
  {
    Set<LicenseInfo> acceptedLicenses = new HashSet<LicenseInfo>();
    if (user != null)
    {
      acceptedLicenses.addAll(user.getAcceptedLicenses());
    }

    final Map<ILicense, List<IInstallableUnit>> licensesToIUs = new HashMap<ILicense, List<IInstallableUnit>>();
    Set<Pair<ILicense, String>> set = new HashSet<Pair<ILicense, String>>();

    IQueryable<IInstallableUnit> queryable = provisioningPlan.getAdditions();
    IQueryResult<IInstallableUnit> result = queryable.query(QueryUtil.ALL_UNITS, monitor);
    for (IInstallableUnit iu : P2Util.asIterable(result))
    {
      Collection<ILicense> licenses = iu.getLicenses(null);
      for (ILicense license : licenses)
      {
        if ("license".equals(license.getBody())) //$NON-NLS-1$
        {
          // Work around bug 463387.
          continue;
        }

        String uuid = license.getUUID();
        if (acceptedLicenses.contains(new LicenseInfo(uuid, null)))
        {
          continue;
        }

        String name = iu.getProperty(IInstallableUnit.PROP_NAME, null);
        if (name == null)
        {
          name = iu.getId();
        }

        if (!set.add(Pair.create(license, name)))
        {
          continue;
        }

        List<IInstallableUnit> ius = licensesToIUs.get(license);
        if (ius == null)
        {
          ius = new ArrayList<IInstallableUnit>();
          licensesToIUs.put(license, ius);
        }

        ius.add(iu);
      }
    }

    if (!licensesToIUs.isEmpty())
    {
      if (licenseConfirmer == null)
      {
        licenseConfirmer = Confirmer.DECLINE;
      }

      Confirmation confirmation = licenseConfirmer.confirm(false, licensesToIUs);
      if (!confirmation.isConfirmed())
      {
        throw new OperationCanceledException("Licenses have been declined"); //$NON-NLS-1$
      }

      if (user != null && confirmation.isRemember())
      {
        for (ILicense license : licensesToIUs.keySet())
        {
          String uuid = license.getUUID();
          String name = LicenseInfo.getFirstLine(license.getBody());

          LicenseInfo licenseInfo = new LicenseInfo(uuid, name);
          user.getAcceptedLicenses().add(licenseInfo);
        }

        if (saveChangedUser)
        {
          try
          {
            XMLResource xmlResource = (XMLResource)user.eResource();
            xmlResource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
          }
          catch (IOException ex)
          {
            SetupP2Plugin.INSTANCE.log(ex);
          }
        }
      }
    }
  }

  private static IProfile getProfile(Agent agent)
  {
    IProfileRegistry profileRegistry = agent.getProfileRegistry();
    IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
    return profile;
  }

  private static Set<IInstallableUnit> getInstalledUnits(Agent agent)
  {
    Set<IInstallableUnit> result = new HashSet<IInstallableUnit>();
    IProfile profile = getProfile(agent);
    if (profile != null)
    {
      IQueryResult<IInstallableUnit> queryResult = profile.query(QueryUtil.createIUAnyQuery(), null);
      for (IInstallableUnit requirement : P2Util.asIterable(queryResult))
      {
        result.add(requirement);
      }
    }

    return result;
  }

  private static Map<String, String> getProfileProperties(Agent agent)
  {
    IProfile profile = getProfile(agent);
    if (profile != null)
    {
      return profile.getProperties();
    }

    return Collections.emptyMap();
  }

  public static void saveConfigIni(File file, Map<String, String> properties, Class<?> caller)
  {
    PropertiesUtil.saveProperties(file, properties, false, true, "This configuration file was written by: " + caller.getName()); //$NON-NLS-1$
  }
} // InstallTaskImpl
