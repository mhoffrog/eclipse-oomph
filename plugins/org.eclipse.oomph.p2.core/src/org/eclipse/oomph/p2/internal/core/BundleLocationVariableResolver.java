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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import org.osgi.framework.Bundle;

import java.io.File;
import java.util.Optional;

/**
 * @author Eike Stepper
 */
public class BundleLocationVariableResolver implements IDynamicVariableResolver
{
  @Override
  public String resolveValue(IDynamicVariable variable, String symbolicName) throws CoreException
  {
    if (!StringUtil.isEmpty(symbolicName))
    {
      Bundle bundle = Platform.getBundle(symbolicName);
      if (bundle != null)
      {
        Optional<File> bundleFileLocation = FileLocator.getBundleFileLocation(bundle);
        if (bundleFileLocation.isPresent())
        {
          return bundleFileLocation.get().getAbsolutePath();
        }
      }
    }

    return null;
  }
}
