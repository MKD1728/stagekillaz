/*
 * Copyright © 2008, 2010 Oracle (Francois-Xavier Nicolas) and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * Neither the name of Oracle Corporation nor the names of its contributors may be used
 * to endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * You acknowledge that this software is not designed, licensed or intended for use in the
 * design, construction, operation or maintenance of any nuclear facility.
 *
 */
package com.starschema.stagekillaz.ODI;

import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiConfigurationException;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.security.Authentication;
import oracle.odi.core.security.AuthenticationException;

public class SimpleOdiInstanceHandle
{

  private OdiInstance mOdiInstance;

  public static SimpleOdiInstanceHandle create(String pMasterReposUrl, String pMasterReposDriver,
          String pMasterReposUser, String pMasterReposPassword, String pWorkReposName, String pOdiUsername,
          String pOdiPassword) throws OdiConfigurationException, AuthenticationException
  {

    MasterRepositoryDbInfo masterInfo = new MasterRepositoryDbInfo(pMasterReposUrl, pMasterReposDriver,
            pMasterReposUser, pMasterReposPassword.toCharArray(), new PoolingAttributes());
    WorkRepositoryDbInfo workInfo = null;
    if (pWorkReposName != null) {
      workInfo = new WorkRepositoryDbInfo(pWorkReposName, new PoolingAttributes());
    }

    OdiInstance inst = OdiInstance.createInstance(new OdiInstanceConfig(masterInfo, workInfo));
    try {
      Authentication auth = inst.getSecurityManager().createAuthentication(pOdiUsername, pOdiPassword.toCharArray());
      inst.getSecurityManager().setCurrentThreadAuthentication(auth);
      return new SimpleOdiInstanceHandle(inst);
    } catch (RuntimeException e) {
      inst.close();
      throw e;
    }
  }

  public static SimpleOdiInstanceHandle create(String pMasterReposUrl, String pMasterReposDriver,
          String pMasterReposUser, String pMasterReposPassword, String pOdiUsername,
          String pOdiPassword) throws OdiConfigurationException, AuthenticationException
  {

    return create(pMasterReposUrl, pMasterReposDriver, pMasterReposUser, pMasterReposPassword, null, pOdiUsername, pOdiPassword);
  }

  private SimpleOdiInstanceHandle(OdiInstance pOdiInstance)
  {
    mOdiInstance = pOdiInstance;
  }

  public OdiInstance getOdiInstance()
  {
    return mOdiInstance;
  }

  public void release()
  {
    try {
      Authentication auth = mOdiInstance.getSecurityManager().getCurrentAuthentication();
      mOdiInstance.getSecurityManager().clearCurrentThreadAuthentication();
      auth.close();
    } finally {
      mOdiInstance.close();
    }
  }
}
