/*
 * Copyright (c) 2010,2011 Starschema Kft.
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
package com.starschema.stagekillaz;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import org.apache.xmlbeans.XmlOptions;
import org.apache.log4j.Logger;
import com.ascentialsoftware.dataStage.export.DSExportDocument;
import com.ascentialsoftware.dataStage.export.DSExportDocument.DSExport;

/**
 *
 * @author Tamas Foldi
 */
public class DataStageReader
{

  static Logger logger = Logger.getLogger(DataStageReader.class);

  public DSExport loadFile(String filePath) throws KillaException
  {
    File inputFile = new File(filePath);
    Map nsSubst = new HashMap();

    try {
      nsSubst.put("", "http://www.ascentialsoftware.com/DataStage/export");
      XmlOptions parseOptions = new XmlOptions();
      parseOptions.setLoadSubstituteNamespaces(nsSubst);

      DSExportDocument dsExportDocument = DSExportDocument.Factory.parse(inputFile, parseOptions);
      DSExport dse = dsExportDocument.getDSExport();

      logger.info("\"" + filePath + "\" has been successfuly parsed. File was created by "
              + dse.getHeaderArray(0).getExportingTool()
              + ", v" + dse.getHeaderArray(0).getToolVersion()
              + ", from server " + dse.getHeaderArray(0).getServerName()
              + " at " + dse.getHeaderArray(0).getDate().toString()
              + " " + dse.getHeaderArray(0).getTime().toString());

      return dse;
    } catch (org.apache.xmlbeans.XmlException exc) {
      throw new KillaException("Error in DataStage export file: " + exc.getMessage(),
              exc);
    } catch (IOException exc) {
      throw new KillaException("Can not load DataStage export file: " + exc.getMessage(),
              exc);
    }
  }
}
