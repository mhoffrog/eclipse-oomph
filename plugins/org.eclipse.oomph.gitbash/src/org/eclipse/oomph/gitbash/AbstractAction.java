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
package org.eclipse.oomph.gitbash;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Eike Stepper
 */
public abstract class AbstractAction<TARGET> implements IObjectActionDelegate
{
  private IWorkbenchPart targetPart;

  private Class<TARGET> targetClass;

  private TARGET target;

  public AbstractAction(Class<TARGET> targetClass)
  {
    this.targetClass = targetClass;
  }

  public IWorkbenchPart getTargetPart()
  {
    return targetPart;
  }

  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
    this.targetPart = targetPart;
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection)
  {
    target = null;
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection ssel = (IStructuredSelection)selection;
      Object element = ssel.getFirstElement();
      target = getAdapter(element, targetClass);
    }
  }

  @Override
  public void run(IAction action)
  {
    if (target != null)
    {
      try
      {
        // TODO Schedule a background job to prevent UI freeze.
        run(targetPart.getSite().getShell(), target);
      }
      catch (Exception ex)
      {
        Activator.log(ex);
      }
    }
  }

  protected abstract void run(Shell shell, TARGET target) throws Exception;

  @SuppressWarnings("unchecked")
  public static <T> T getAdapter(Object adaptable, Class<T> c)
  {
    if (c.isInstance(adaptable))
    {
      return (T)adaptable;
    }

    if (adaptable instanceof IAdaptable)
    {
      IAdaptable a = (IAdaptable)adaptable;
      Object adapter = a.getAdapter(c);
      if (c.isInstance(adapter))
      {
        return (T)adapter;
      }
    }

    return null;
  }
}
