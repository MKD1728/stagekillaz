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

public class ConfigurationArgs {

 	private static final String DEFAULT_FOLDER = "Migrated jobs";
 	private static final String DEFAULT_ROOT_KM_PATH = "c:/Oracle/ODI/oracledi/xml-reference/";

  /**
   *  Default constructor, initalize default values for props
   */
  public ConfigurationArgs()
  {
    folderName = DEFAULT_FOLDER;
    rootKMPath = DEFAULT_ROOT_KM_PATH;
  }

  protected String rootKMPath;

  /**
   * Get the value of rootKMPath
   *
   * @return the value of rootKMPath
   */
  public String getRootKMPath()
  {
    return rootKMPath;
  }

  /**
   * Set the value of rootKMPath
   *
   * @param rootKMPath new value of rootKMPath
   */
  public void setRootKMPath(String rootKMPath)
  {
    this.rootKMPath = rootKMPath;
  }

  protected String folderName;

  /**
   * Get the value of folderName
   *
   * @return the value of folderName
   */
  public String getFolderName()
  {
    return folderName;
  }

  /**
   * Set the value of folderName
   *
   * @param folderName new value of folderName
   */
  public void setFolderName(String folderName)
  {
    this.folderName = folderName;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final ConfigurationArgs other = (ConfigurationArgs) obj;
    if ((this.rootKMPath == null) ? (other.rootKMPath != null) : !this.rootKMPath.equals(other.rootKMPath))
      return false;
    if ((this.folderName == null) ? (other.folderName != null) : !this.folderName.equals(other.folderName))
      return false;
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 17 * hash + (this.rootKMPath != null ? this.rootKMPath.hashCode() : 0);
    hash = 17 * hash + (this.folderName != null ? this.folderName.hashCode() : 0);
    return hash;
  }

}
