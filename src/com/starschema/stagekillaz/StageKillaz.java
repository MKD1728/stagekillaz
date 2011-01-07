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

import com.starschema.stagekillaz.ODI.ConfigurationArgs;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import com.starschema.stagekillaz.ODI.Manager;
import com.starschema.stagekillaz.ODI.ConnectionArgs;
import org.w3c.dom.Document;

public class StageKillaz
{
  static StageKillaz stageKillaz;
  static Logger logger = Logger.getLogger(StageKillaz.class);

  /** Main entry point, executes the tool
   *
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    stageKillaz = new StageKillaz();

    stageKillaz.execute(args);
  }

  /** Executes the conversion tasks
   *
   * Executes the conversion tasks described in the
   * command line arguments
   *
   * @param args the command line arguments
   */
  public void execute(String[] args)
  {
    String inputFile = null;
    ConfigurationArgs confArgs = new ConfigurationArgs();
    ConnectionArgs connectionArgs = new ConnectionArgs();

    // Configure logger
    BasicConfigurator.configure();

    // Configure longop
    LongOpt[] longopts = new LongOpt[11];
    StringBuffer sb = new StringBuffer();
    longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
    longopts[1] = new LongOpt("inputfile", LongOpt.REQUIRED_ARGUMENT, sb, 'i');
    longopts[2] = new LongOpt("reposurl", LongOpt.REQUIRED_ARGUMENT, sb, 'r');
    longopts[3] = new LongOpt("reposdriver", LongOpt.REQUIRED_ARGUMENT, sb, 'd');
    longopts[4] = new LongOpt("reposuser", LongOpt.REQUIRED_ARGUMENT, sb, 's');
    longopts[5] = new LongOpt("repospassword", LongOpt.REQUIRED_ARGUMENT, sb, 'a');
    longopts[6] = new LongOpt("reposname", LongOpt.REQUIRED_ARGUMENT, sb, 'n');
    longopts[7] = new LongOpt("odiuser", LongOpt.REQUIRED_ARGUMENT, sb, 'u');
    longopts[8] = new LongOpt("odipassword", LongOpt.REQUIRED_ARGUMENT, sb, 'p');
    longopts[9] = new LongOpt("folder", LongOpt.REQUIRED_ARGUMENT, sb, 'f');
    longopts[10] = new LongOpt("kmpath", LongOpt.REQUIRED_ARGUMENT, sb, 'k');

    Getopt g = new Getopt("stagekillaz", args, "-hi:r:d:s:a:n:u:p:f:", longopts);
    int c;

    // parse arguments
    while ((c = g.getopt()) != -1) {
      // handle longopts as they short ones
      if (c == 0)
        c = (char) (new Integer(sb.toString())).intValue();

      if (c == 'h') {
        help();
        System.exit(0);
      } else if (c == 'i') {
        inputFile = g.getOptarg();
      } else if (c == 'r') {
        connectionArgs.setMasterReposUrl(g.getOptarg());
      } else if (c == 'd') {
        connectionArgs.setMasterReposDriver(g.getOptarg());
      } else if (c == 's') {
        connectionArgs.setMasterReposUser(g.getOptarg());
      } else if (c == 'a') {
        connectionArgs.setMasterReposPassword(g.getOptarg());
      } else if (c == 'n') {
        connectionArgs.setWorkReposName(g.getOptarg());
      } else if (c == 'u') {
        connectionArgs.setOdiUsername(g.getOptarg());
      } else if (c == 'p') {
        connectionArgs.setOdiPassword(g.getOptarg());
      } else if (c == 'f') {
        confArgs.setFolderName( g.getOptarg() );
      } else if (c == 'k') {
        confArgs.setRootKMPath( g.getOptarg() );
      }
    } // while

    // check if input file is present
    if (inputFile == null) {
      System.err.println("Input filename is mandatory");
      System.exit(-1);
    }

    // check if input file is present
    if (connectionArgs.missingArg() != null) {
      System.err.println(connectionArgs.missingArg() + " is missing from ODI connection");
      System.exit(-1);
    }

    logger.info("Conversion started");

    // Parse input file
    DataStageReader reader = new DataStageReader();
    Document dsExport = null;
    try {
      // parse input file
      dsExport = reader.loadFile(inputFile);
    } catch (KillaException e) {
      e.ReportError();
      System.exit(-1);
    }

    // Managers
    Manager odiMgr = new Manager(dsExport, connectionArgs,confArgs);
    try {
      // Generate ODI project
      odiMgr.run();
    } catch (KillaException e) {
      e.ReportError();
      System.exit(-1);
    }

    // Process parsed file
  } // Main

  /** Prints fancy help message
   *
   */
  static void help()
  {
    System.out.println();
    System.out.println("Usage: stagekillaz [-h] -OPT1 VALUE1 --LONGOPT2=VALUE2 ... --OPTIONn=VALUEn ");
    System.out.println();
    System.out.println("Options: ");

    System.out.println("  -h | --help\t\tThis short help message");
    System.out.println("  -i | --inputfile\tDataStage export file");
    System.out.println("  -r | --reposurl\tODI repository's jdbc URL");
    System.out.println("  -d | --reposdriver\tJDBC Driver class");
    System.out.println("  -s | --reposuser\tDatabase user for master repo");
    System.out.println("  -a | --repospassword\tDatabase password for master repo");
    System.out.println("  -n | --reposname\tWorking repository name (optional)");
    System.out.println("  -u | --odiuser\tODI user name");
    System.out.println("  -p | --odipassword\tODI password");
    System.out.println("  -f | --folder\t\tFolder in ODI project (optional)");
    System.out.println("  -k | --kmpath\t\tPath to ODI's xml-reference directory (optional)");
  }
}
