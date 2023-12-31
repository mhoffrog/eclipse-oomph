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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class BundlePoolImpl extends AgentManagerElementImpl implements BundlePool
{
  private final Agent agent;

  private File location;

  private String path;

  private IFileArtifactRepository fileArtifactRepository;

  public BundlePoolImpl(AgentImpl agent, File location)
  {
    this.agent = agent;
    setLocation(location);
  }

  @Override
  public String getElementType()
  {
    return "bundle pool"; //$NON-NLS-1$
  }

  @Override
  public AgentManager getAgentManager()
  {
    return agent.getAgentManager();
  }

  @Override
  public Agent getAgent()
  {
    return agent;
  }

  @Override
  public File getLocation()
  {
    return location;
  }

  public void setLocation(File location)
  {
    this.location = location;
    path = location.getAbsolutePath();
  }

  @Override
  public Set<String> getClients()
  {
    return ((AgentManagerImpl)agent.getAgentManager()).getClientsFor(path);
  }

  @Override
  public boolean isValid()
  {
    return true;
  }

  @Override
  public boolean isCurrent()
  {
    if (!agent.isCurrent())
    {
      return false;
    }

    Profile currentProfile = agent.getCurrentProfile();
    return currentProfile != null && currentProfile.getBundlePool() == this;
  }

  @Override
  public boolean isUsed()
  {
    if (!getProfiles().isEmpty())
    {
      return true;
    }

    return false;
  }

  @Override
  protected void doDelete()
  {
    ((AgentImpl)agent).deleteBundlePool(this);
  }

  @Override
  public synchronized IFileArtifactRepository getFileArtifactRepository()
  {
    if (fileArtifactRepository == null)
    {
      IArtifactRepositoryManager artifactRepositoryManager = agent.getArtifactRepositoryManager();
      URI uri = location.toURI();

      try
      {
        if (artifactRepositoryManager.contains(uri))
        {
          fileArtifactRepository = (IFileArtifactRepository)artifactRepositoryManager.loadRepository(uri, null);
        }
      }
      catch (CoreException ex)
      {
        //$FALL-THROUGH$
      }

      if (fileArtifactRepository == null)
      {
        try
        {
          fileArtifactRepository = (IFileArtifactRepository)artifactRepositoryManager.createRepository(uri, Messages.BundlePoolImpl_SharedBundlePool_label,
              IArtifactRepositoryManager.TYPE_SIMPLE_REPOSITORY, null);
        }
        catch (ProvisionException ex)
        {
          throw new P2Exception(NLS.bind(Messages.BundlePoolImpl_PoolNotLoaded_exception, location));
        }
      }
    }

    return fileArtifactRepository;
  }

  @Override
  public Set<String> getProfileIDs()
  {
    return ((AgentImpl)agent).getProfileIDs(this);
  }

  @Override
  public Collection<Profile> getProfiles()
  {
    return ((AgentImpl)agent).getProfiles(this);
  }

  @Override
  public Profile getProfile(String id)
  {
    Profile profile = agent.getProfile(id);
    if (profile != null && profile.getBundlePool() == this)
    {
      return profile;
    }

    return null;
  }

  @Override
  public Profile getProfile(File installFolder)
  {
    Profile profile = agent.getProfile(installFolder);
    if (profile != null && profile.getBundlePool() == this)
    {
      return profile;
    }

    return null;
  }

  @Override
  public ProfileCreator addProfile(String id, String type)
  {
    return new ProfileCreatorImpl(this, id, type)
    {
      private static final long serialVersionUID = 1L;

      @Override
      public ProfileCreator setCacheFolder(File value)
      {
        if (!location.equals(value))
        {
          throw new IllegalArgumentException(NLS.bind(Messages.BundlePoolImpl_CachCannotBeChanged_exception, value));
        }

        return this;
      }

      @Override
      protected Profile doCreateProfile()
      {
        this.set(Profile.PROP_CACHE, path);
        return ((AgentImpl)agent).createProfile(this);
      }
    };
  }

  @Override
  public String toString()
  {
    return getLocation().getAbsolutePath();
  }
}
