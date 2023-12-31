/*
 * Copyright (c) 2015 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.base.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Ed Merks
 */
public class BytesResourceFactoryImpl implements Resource.Factory
{
  @Override
  public Resource createResource(URI uri)
  {
    return new BytesResourceImpl(uri);
  }
}
