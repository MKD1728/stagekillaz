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
  /** ODI Folder name */
  protected String folderName;
  /** ODI Folder handler */
  protected OdiFolder folder;

  static final String PROJECT_DESC = "Migrated DataStage Jobs";
  static final String PROJECT_CODE = "PROJECT_DS";

  /* log4j logger */
  static Logger logger = Logger.getLogger(Manager.class);

  public Manager(Document dse, ConnectionArgs ca, String fn)
  {
    dsExport = dse;
    connectionArgs = ca;
    isODIConnected = false;
    folderName = fn;
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

        logger.info("Check if the project already exists");

        // Open project
        project = ((IOdiProjectFinder)entityManager.getFinder(OdiProject.class)).findByCode(PROJECT_CODE);
        if (project != null)
          return null;

        logger.info("Create new ODI project and folder");

        // Create project
        project = new OdiProject(PROJECT_DESC, PROJECT_CODE);
			  folder = new OdiFolder(project, folderName );

        logger.debug("Saving ODI project");
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
