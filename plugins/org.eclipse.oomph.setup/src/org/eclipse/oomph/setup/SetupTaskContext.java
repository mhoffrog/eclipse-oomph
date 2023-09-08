/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IProgressMonitor;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface SetupTaskContext extends ProgressLog
{
  public IProgressMonitor getProgressMonitor(boolean working);

  public SetupPrompter getPrompter();

  public Trigger getTrigger();

  public void checkCancelation();

  public boolean isSelfHosting();

  public boolean isPerforming();

  public boolean isOffline();

  public boolean isMirrors();

  public boolean isRestartNeeded();

  public void setRestartNeeded(String reason);

  public User getUser();

  public Workspace getWorkspace();

  public Installation getInstallation();

  public File getInstallationLocation();

  public File getProductLocation();

  public File getProductConfigurationLocation();

  public File getWorkspaceLocation();

  public String getRelativeProductFolder();

  public OS getOS();

  public URIConverter getURIConverter();

  public URI redirect(URI uri);

  public String redirect(String uri);

  public Object get(Object key);

  public Object put(Object key, Object value);

  public Set<Object> keySet();

  public String getLauncherName();

  public boolean matchesFilterContext(String filter);

  // @patch mhoffrog
  public Map<String, String> getFilterVariableProperties();

}
