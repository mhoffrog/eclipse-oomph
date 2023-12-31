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
import org.eclipse.oomph.predicates.util.PredicatesAdapterFactory;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ChildCreationExtenderManager;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PredicatesItemProviderAdapterFactory extends PredicatesAdapterFactory
    implements ComposeableAdapterFactory, IChangeNotifier, IDisposable, IChildCreationExtender
{
  /**
   * This keeps track of the root adapter factory that delegates to this adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ComposedAdapterFactory parentAdapterFactory;

  /**
   * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IChangeNotifier changeNotifier = new ChangeNotifier();

  /**
   * This helps manage the child creation extenders.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ChildCreationExtenderManager childCreationExtenderManager = new ChildCreationExtenderManager(PredicatesEditPlugin.INSTANCE,
      PredicatesPackage.eNS_URI);

  /**
   * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<Object> supportedTypes = new ArrayList<>();

  /**
   * This constructs an instance.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PredicatesItemProviderAdapterFactory()
  {
    supportedTypes.add(IEditingDomainItemProvider.class);
    supportedTypes.add(IStructuredItemContentProvider.class);
    supportedTypes.add(ITreeItemContentProvider.class);
    supportedTypes.add(IItemLabelProvider.class);
    supportedTypes.add(IItemPropertySource.class);
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.NamePredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected NamePredicateItemProvider namePredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.NamePredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createNamePredicateAdapter()
  {
    if (namePredicateItemProvider == null)
    {
      namePredicateItemProvider = new NamePredicateItemProvider(this);
    }

    return namePredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.CommentPredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CommentPredicateItemProvider commentPredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.CommentPredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createCommentPredicateAdapter()
  {
    if (commentPredicateItemProvider == null)
    {
      commentPredicateItemProvider = new CommentPredicateItemProvider(this);
    }

    return commentPredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.LocationPredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LocationPredicateItemProvider locationPredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.LocationPredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createLocationPredicateAdapter()
  {
    if (locationPredicateItemProvider == null)
    {
      locationPredicateItemProvider = new LocationPredicateItemProvider(this);
    }

    return locationPredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.RepositoryPredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RepositoryPredicateItemProvider repositoryPredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.RepositoryPredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createRepositoryPredicateAdapter()
  {
    if (repositoryPredicateItemProvider == null)
    {
      repositoryPredicateItemProvider = new RepositoryPredicateItemProvider(this);
    }

    return repositoryPredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.AndPredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AndPredicateItemProvider andPredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.AndPredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createAndPredicateAdapter()
  {
    if (andPredicateItemProvider == null)
    {
      andPredicateItemProvider = new AndPredicateItemProvider(this);
    }

    return andPredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.OrPredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OrPredicateItemProvider orPredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.OrPredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createOrPredicateAdapter()
  {
    if (orPredicateItemProvider == null)
    {
      orPredicateItemProvider = new OrPredicateItemProvider(this);
    }

    return orPredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.NotPredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected NotPredicateItemProvider notPredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.NotPredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createNotPredicateAdapter()
  {
    if (notPredicateItemProvider == null)
    {
      notPredicateItemProvider = new NotPredicateItemProvider(this);
    }

    return notPredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.NaturePredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected NaturePredicateItemProvider naturePredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.NaturePredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createNaturePredicateAdapter()
  {
    if (naturePredicateItemProvider == null)
    {
      naturePredicateItemProvider = new NaturePredicateItemProvider(this);
    }

    return naturePredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.BuilderPredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BuilderPredicateItemProvider builderPredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.BuilderPredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createBuilderPredicateAdapter()
  {
    if (builderPredicateItemProvider == null)
    {
      builderPredicateItemProvider = new BuilderPredicateItemProvider(this);
    }

    return builderPredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.FilePredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FilePredicateItemProvider filePredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.FilePredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createFilePredicateAdapter()
  {
    if (filePredicateItemProvider == null)
    {
      filePredicateItemProvider = new FilePredicateItemProvider(this);
    }

    return filePredicateItemProvider;
  }

  /**
   * This keeps track of the one adapter used for all {@link org.eclipse.oomph.predicates.ImportedPredicate} instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ImportedPredicateItemProvider importedPredicateItemProvider;

  /**
   * This creates an adapter for a {@link org.eclipse.oomph.predicates.ImportedPredicate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter createImportedPredicateAdapter()
  {
    if (importedPredicateItemProvider == null)
    {
      importedPredicateItemProvider = new ImportedPredicateItemProvider(this);
    }

    return importedPredicateItemProvider;
  }

  /**
   * This returns the root adapter factory that contains this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ComposeableAdapterFactory getRootAdapterFactory()
  {
    return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
  }

  /**
   * This sets the composed adapter factory that contains this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory)
  {
    this.parentAdapterFactory = parentAdapterFactory;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object type)
  {
    return supportedTypes.contains(type) || super.isFactoryForType(type);
  }

  /**
   * This implementation substitutes the factory itself as the key for the adapter.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Adapter adapt(Notifier notifier, Object type)
  {
    return super.adapt(notifier, this);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object adapt(Object object, Object type)
  {
    if (isFactoryForType(type))
    {
      Object adapter = super.adapt(object, type);
      if (!(type instanceof Class<?>) || ((Class<?>)type).isInstance(adapter))
      {
        return adapter;
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List<IChildCreationExtender> getChildCreationExtenders()
  {
    return childCreationExtenderManager.getChildCreationExtenders();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain)
  {
    return childCreationExtenderManager.getNewChildDescriptors(object, editingDomain);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator()
  {
    return childCreationExtenderManager;
  }

  /**
   * This adds a listener.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void addListener(INotifyChangedListener notifyChangedListener)
  {
    changeNotifier.addListener(notifyChangedListener);
  }

  /**
   * This removes a listener.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void removeListener(INotifyChangedListener notifyChangedListener)
  {
    changeNotifier.removeListener(notifyChangedListener);
  }

  /**
   * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void fireNotifyChanged(Notification notification)
  {
    changeNotifier.fireNotifyChanged(notification);

    if (parentAdapterFactory != null)
    {
      parentAdapterFactory.fireNotifyChanged(notification);
    }
  }

  /**
   * This disposes all of the item providers created by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void dispose()
  {
    if (namePredicateItemProvider != null)
    {
      namePredicateItemProvider.dispose();
    }
    if (commentPredicateItemProvider != null)
    {
      commentPredicateItemProvider.dispose();
    }
    if (locationPredicateItemProvider != null)
    {
      locationPredicateItemProvider.dispose();
    }
    if (repositoryPredicateItemProvider != null)
    {
      repositoryPredicateItemProvider.dispose();
    }
    if (andPredicateItemProvider != null)
    {
      andPredicateItemProvider.dispose();
    }
    if (orPredicateItemProvider != null)
    {
      orPredicateItemProvider.dispose();
    }
    if (notPredicateItemProvider != null)
    {
      notPredicateItemProvider.dispose();
    }
    if (naturePredicateItemProvider != null)
    {
      naturePredicateItemProvider.dispose();
    }
    if (builderPredicateItemProvider != null)
    {
      builderPredicateItemProvider.dispose();
    }
    if (filePredicateItemProvider != null)
    {
      filePredicateItemProvider.dispose();
    }
    if (importedPredicateItemProvider != null)
    {
      importedPredicateItemProvider.dispose();
    }
  }

}
