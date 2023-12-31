/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates.provider;

import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.predicates.RepositoryPredicate;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.osgi.util.NLS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.predicates.RepositoryPredicate} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RepositoryPredicateItemProvider extends PredicateItemProvider
{
  private static final IWorkspaceRoot WORKSPACE_ROOT = ResourcesPlugin.getWorkspace().getRoot();

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RepositoryPredicateItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the property descriptors for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
  {
    if (itemPropertyDescriptors == null)
    {
      super.getPropertyDescriptors(object);

      addProjectPropertyDescriptor(object);
      addRelativePathPatternPropertyDescriptor(object);
      addIncludeNestedRepositoriesPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Project feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addProjectPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_RepositoryPredicate_project_feature"), getString("_UI_RepositoryPredicate_project_description"), //$NON-NLS-1$ //$NON-NLS-2$
        PredicatesPackage.Literals.REPOSITORY_PREDICATE__PROJECT, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        List<IProject> result = new ArrayList<>();
        for (IProject project : WORKSPACE_ROOT.getProjects())
        {
          result.add(project);
        }
        return result;
      }
    });
  }

  /**
   * This adds a property descriptor for the Relative Path Pattern feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRelativePathPatternPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_RepositoryPredicate_relativePathPattern_feature"), //$NON-NLS-1$
        getString("_UI_RepositoryPredicate_relativePathPattern_description"), //$NON-NLS-1$
        PredicatesPackage.Literals.REPOSITORY_PREDICATE__RELATIVE_PATH_PATTERN, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Include Nested Repositories feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncludeNestedRepositoriesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_RepositoryPredicate_includeNestedRepositories_feature"), //$NON-NLS-1$
        getString("_UI_RepositoryPredicate_includeNestedRepositories_description"), //$NON-NLS-1$
        PredicatesPackage.Literals.REPOSITORY_PREDICATE__INCLUDE_NESTED_REPOSITORIES, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null,
        null));
  }

  /**
   * This returns RepositoryPredicate.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/RepositoryPredicate")); //$NON-NLS-1$
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected boolean shouldComposeCreationImage()
  {
    return true;
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getText(Object object)
  {
    RepositoryPredicate repositoryPredicate = (RepositoryPredicate)object;
    IProject project = repositoryPredicate.getProject();
    String relativePathPattern = repositoryPredicate.getRelativePathPattern();
    return (project == null ? getString("_UI_RepositoryPredicate_type") //$NON-NLS-1$
        : NLS.bind(Messages.RepositoryPredicateItemProvider_SameRepository_label, project.getName()))
        + (relativePathPattern == null ? "" : NLS.bind(Messages.RepositoryPredicateItemProvider_relativePathLike, relativePathPattern)); //$NON-NLS-1$
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(RepositoryPredicate.class))
    {
      case PredicatesPackage.REPOSITORY_PREDICATE__PROJECT:
      case PredicatesPackage.REPOSITORY_PREDICATE__RELATIVE_PATH_PATTERN:
      case PredicatesPackage.REPOSITORY_PREDICATE__INCLUDE_NESTED_REPOSITORIES:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
    }
    super.notifyChanged(notification);
  }

  @Override
  protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
  {
    return new BaseDragAndDropCommand(domain, owner, location, operations, operation, collection)
    {
      @Override
      protected boolean analyzeForNonContainment(Command command)
      {
        return true;
      }
    };
  }

  @Override
  protected Command createSetCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Object value)
  {
    if (feature == null)
    {
      if (value instanceof Collection<?>)
      {
        for (Object item : (Collection<?>)value)
        {
          if (item instanceof IProject)
          {
            IProject project = (IProject)item;
            return new SetCommand(domain, owner, PredicatesPackage.Literals.REPOSITORY_PREDICATE__PROJECT, project);
          }
        }
      }
    }
    return super.createSetCommand(domain, owner, feature, value);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);
  }

}
