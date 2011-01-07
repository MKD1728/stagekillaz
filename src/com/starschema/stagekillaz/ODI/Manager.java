/*
 * Copyright (c) 2010,2011 Starschema Kft. - www.starschema.net
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.starschema.stagekillaz.ODI;

// common stuff
import com.starschema.stagekillaz.KillaException;
import org.apache.log4j.Logger;
// ODI
import oracle.odi.core.config.OdiConfigurationException;
import oracle.odi.core.security.AuthenticationException;
import oracle.odi.core.OdiInstance;
import oracle.odi.core.persistence.IOdiEntityManager;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.ITransactionCallback;
import oracle.odi.core.persistence.transaction.support.TransactionTemplate;
import oracle.odi.domain.finder.IFinder;
import oracle.odi.domain.project.OdiFolder;
import oracle.odi.domain.project.OdiProject;
import oracle.odi.domain.project.finder.IOdiFolderFinder;
import oracle.odi.domain.project.finder.IOdiProjectFinder;
import org.w3c.dom.Document;

public class Manager
{

  /** parsed DataStage export document in XMLBean format */
  protected static Document dsExport;
  /** parsed connection arguments for ODI */
  protected static ConnectionArgs connectionArgs;
  /** connection handler to ODI repository */
  protected SimpleOdiInstanceHandle odiInstanceHandle;
  /** ODI native instance class */
  protected OdiInstance odiInstance;
  /** does we connected to ODI? */
  protected boolean isODIConnected;
  /** Project handler in ODI */
  protected OdiProject project;
  /** External configuration values, like ODI Folder and xml-reference path */
  ConfigurationArgs configurationArgs;
  /** ODI Folder handler */
  protected OdiFolder folder;

  public static final String PROJECT_DESC = "Migrated DataStage Jobs";
  public static final String PROJECT_CODE = "PROJECT_DS";

  /* log4j logger */
  private static Logger logger = Logger.getLogger(Manager.class);

  public Manager(Document dse, ConnectionArgs ca, ConfigurationArgs confArgs)
  {
    dsExport = dse;
    connectionArgs = ca;
    isODIConnected = false;
    configurationArgs = confArgs;
  }

  public void run() throws KillaException
  {

    try {

      // 1. Log on to ODI
      connectToRepository();

      // 2. Create/open the project
      openProject();

      // 3. Generate variables
      Variables.storeVariables(dsExport, odiInstanceHandle, project, folder);

      // 4. Load KMs
      KnownledgeModules.addInterfaces(odiInstanceHandle,configurationArgs);

      
    } catch (Exception exc) {
      if (exc instanceof KillaException)
        throw (KillaException) exc;
      else
        throw new KillaException(exc.getLocalizedMessage(), exc);
    } finally {
      if (isODIConnected)
        relaseRepository();
    }

  }

  private void openProject() throws KillaException
  {

    TransactionTemplate tx = new TransactionTemplate(odiInstance.getTransactionManager());
    tx.execute(new ITransactionCallback()
    {

      public Object doInTransaction(ITransactionStatus pStatus)
      {
        IOdiEntityManager entityManager = odiInstance.getTransactionalEntityManager();
        String folderName = configurationArgs.getFolderName();

        logger.debug("Check if the project already exists");

        // Open project
        project = ((IOdiProjectFinder)entityManager.getFinder(OdiProject.class)).findByCode(PROJECT_CODE);
        if (project == null) {
          // Create project
          logger.info("Create new ODI project: " + PROJECT_DESC);
          project = new OdiProject(PROJECT_DESC, PROJECT_CODE);
        }

        if (((IOdiFolderFinder) entityManager.getFinder(OdiFolder.class)).findByName(folderName).isEmpty()) {
          logger.info("Create new ODI project folder: " + folderName);
          folder = new OdiFolder(project, folderName);
        } else {
          folder = (OdiFolder) (((IOdiFolderFinder) entityManager.getFinder(OdiFolder.class)).findByName(folderName).toArray())[0];
        }

        logger.debug("Saving ODI project and folder");
        // Persist the project and the model
        entityManager.persist(project);

        return null;
      }
    });

  }

  private void connectToRepository() throws KillaException
  {
    logger.info("Logging in to ODI");
    logger.debug("ODI connection arguments: " + connectionArgs);

    try {
      odiInstanceHandle = SimpleOdiInstanceHandle.create(
              connectionArgs.getMasterReposUrl(),
              connectionArgs.getMasterReposDriver(),
              connectionArgs.getMasterReposUser(),
              connectionArgs.getMasterReposPassword(),
              connectionArgs.getWorkReposName(),
              connectionArgs.getOdiUsername(),
              connectionArgs.getOdiPassword());

      odiInstance = odiInstanceHandle.getOdiInstance();

      logger.debug("Connected to ODI repository");
    } catch (OdiConfigurationException exc) {
      throw new KillaException("Cannot login to ODI: " + exc.getLocalizedMessage(), exc);
    } catch (AuthenticationException exc) {
      throw new KillaException("ODI authentication error: " + exc.getLocalizedMessage(), exc);
    }

    isODIConnected = true;
  }

  private void relaseRepository()
  {
    logger.info("Logoff from ODI");

    odiInstanceHandle.release();
    isODIConnected = false;
  }
}
