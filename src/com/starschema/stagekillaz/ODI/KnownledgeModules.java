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

// Common stuff
import com.starschema.stagekillaz.KillaException;
import java.io.IOException;
import oracle.odi.core.exception.OdiRuntimeException;
import org.apache.log4j.Logger;
// ODI
import oracle.odi.core.persistence.IOdiEntityManager;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.ITransactionCallback;
import oracle.odi.core.persistence.transaction.support.TransactionTemplate;
import oracle.odi.domain.project.OdiIKM;
import oracle.odi.domain.project.OdiLKM;
import oracle.odi.domain.project.OdiProject;
import oracle.odi.domain.project.finder.IOdiIKMFinder;
import oracle.odi.domain.project.finder.IOdiLKMFinder;
import oracle.odi.domain.project.finder.IOdiProjectFinder;
import oracle.odi.impexp.IImportService;
import oracle.odi.impexp.OdiImportException;
import oracle.odi.impexp.OdiImportNotSupportedException;
import oracle.odi.impexp.support.ImportServiceImpl;

public class KnownledgeModules
{
  /* log4j logger */

  static Logger logger = Logger.getLogger(KnownledgeModules.class);

  static void addInterfaces(final SimpleOdiInstanceHandle odiInstanceHandle,
          final ConfigurationArgs confArgs) throws KillaException
  {

    try {
      TransactionTemplate tx = new TransactionTemplate(odiInstanceHandle.getOdiInstance().getTransactionManager());
      tx.execute(new ITransactionCallback()
      {

        public Object doInTransaction(ITransactionStatus pStatus)
        {
          logger.debug("Add missing KMs");

          // Retrieve the import service
          IImportService importService = new ImportServiceImpl(odiInstanceHandle.getOdiInstance());
          IOdiEntityManager entityManager = odiInstanceHandle.getOdiInstance().getTransactionalEntityManager();

          OdiProject project = ((IOdiProjectFinder) entityManager.getFinder(OdiProject.class)).findByCode(Manager.PROJECT_CODE);

          try {
            // Import the KMs using duplication mode
            if (((IOdiLKMFinder) entityManager.getFinder(OdiLKM.class)).findByName("LKM SQL to SQL", Manager.PROJECT_CODE).isEmpty()) {
              logger.info("Importing KM" + confArgs.getRootKMPath() + "KM_LKM SQL to SQL.xml");
              importService.importObjectFromXml(IImportService.IMPORT_MODE_DUPLICATION, confArgs.getRootKMPath() + "KM_LKM SQL to SQL.xml", project, false);
            }

            if (((IOdiIKMFinder) entityManager.getFinder(OdiIKM.class)).findByName("IKM SQL to SQL Append", Manager.PROJECT_CODE).isEmpty()) {
              logger.info("Importing KM" + confArgs.getRootKMPath() + "KM_IKM SQL to SQL Append.xml");
              importService.importObjectFromXml(IImportService.IMPORT_MODE_DUPLICATION, confArgs.getRootKMPath() + "KM_IKM SQL to SQL Append.xml", project, false);
            }
          } catch (OdiImportNotSupportedException e) {
            throw new OdiRuntimeException(e);
          } catch (OdiImportException e) {
            throw new OdiRuntimeException(e);
          } catch (IOException e) {
            throw new OdiRuntimeException(e);
          }

          logger.debug("Import successful");

          return null;
        }
      });
    } catch (OdiRuntimeException exc) {
      throw new KillaException("Cannot search for parameters: " + exc.getMessage(),
              exc);
    }
  }
}
