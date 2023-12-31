/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import java.util.Collection;

/**
 * @author Ed Merks
 */
public class OomphDragAdapter implements DragSourceListener
{
  protected EditingDomain domain;

  protected ISelectionProvider selectionProvider;

  protected Collection<? extends OomphTransferDelegate> delegates;

  public OomphDragAdapter(EditingDomain domain, ISelectionProvider selectionProvider, Collection<? extends OomphTransferDelegate> delegates)
  {
    this.domain = domain;
    this.selectionProvider = selectionProvider;
    this.delegates = delegates;
  }

  @Override
  public void dragStart(DragSourceEvent event)
  {
    ISelection selection = selectionProvider.getSelection();
    event.doit = false;
    for (OomphTransferDelegate delegate : delegates)
    {
      if (delegate.setSelection(domain, selection))
      {
        // At least one delegate must have found something interesting in the selection to transfer.
        event.doit = true;
      }
    }
  }

  @Override
  public void dragSetData(DragSourceEvent event)
  {
    for (OomphTransferDelegate delegate : delegates)
    {
      if (delegate.isSupportedType(event.dataType))
      {
        // If the data of the chosen transfer isn't available, veto the event.
        event.data = delegate.getData();
        if (event.data == null)
        {
          event.doit = false;
        }

        break;
      }
    }
  }

  @Override
  public void dragFinished(DragSourceEvent event)
  {
    for (OomphTransferDelegate delegate : delegates)
    {
      delegate.clear();
    }
  }
}
