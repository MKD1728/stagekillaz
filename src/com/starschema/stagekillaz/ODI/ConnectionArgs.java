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

public class ConnectionArgs
{

  protected String masterReposUrl;

  /**
   * Get the value of masterReposUrl
   *
   * @return the value of masterReposUrl
   */
  public String getMasterReposUrl()
  {
    return masterReposUrl;
  }

  /**
   * Set the value of masterReposUrl
   *
   * @param masterReposUrl new value of masterReposUrl
   */
  public void setMasterReposUrl(String masterReposUrl)
  {
    this.masterReposUrl = masterReposUrl;
  }
  protected String masterReposDriver;

  /**
   * Get the value of masterReposDriver
   *
   * @return the value of masterReposDriver
   */
  public String getMasterReposDriver()
  {
    return masterReposDriver;
  }

  /**
   * Set the value of masterReposDriver
   *
   * @param masterReposDriver new value of masterReposDriver
   */
  public void setMasterReposDriver(String masterReposDriver)
  {
    this.masterReposDriver = masterReposDriver;
  }
  protected String masterReposUser;

  /**
   * Get the value of masterReposUser
   *
   * @return the value of masterReposUser
   */
  public String getMasterReposUser()
  {
    return masterReposUser;
  }

  /**
   * Set the value of masterReposUser
   *
   * @param masterReposUser new value of masterReposUser
   */
  public void setMasterReposUser(String masterReposUser)
  {
    this.masterReposUser = masterReposUser;
  }
  protected String masterReposPassword;

  /**
   * Get the value of masterReposPassword
   *
   * @return the value of masterReposPassword
   */
  public String getMasterReposPassword()
  {
    return masterReposPassword;
  }

  /**
   * Set the value of masterReposPassword
   *
   * @param masterReposPassword new value of masterReposPassword
   */
  public void setMasterReposPassword(String masterReposPassword)
  {
    this.masterReposPassword = masterReposPassword;
  }
  protected String workReposName;

  /**
   * Get the value of workReposName
   *
   * @return the value of workReposName
   */
  public String getWorkReposName()
  {
    return workReposName;
  }

  /**
   * Set the value of workReposName
   *
   * @param workReposName new value of workReposName
   */
  public void setWorkReposName(String workReposName)
  {
    this.workReposName = workReposName;
  }
  protected String odiUsername;

  /**
   * Get the value of odiUsername
   *
   * @return the value of odiUsername
   */
  public String getOdiUsername()
  {
    return odiUsername;
  }

  /**
   * Set the value of odiUsername
   *
   * @param odiUsername new value of odiUsername
   */
  public void setOdiUsername(String odiUsername)
  {
    this.odiUsername = odiUsername;
  }
  protected String odiPassword;

  /**
   * Get the value of odiPassword
   *
   * @return the value of odiPassword
   */
  public String getOdiPassword()
  {
    return odiPassword;
  }

  /**
   * Set the value of odiPassword
   *
   * @param odiPassword new value of odiPassword
   */
  public void setOdiPassword(String odiPassword)
  {
    this.odiPassword = odiPassword;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final ConnectionArgs other = (ConnectionArgs) obj;
    if ((this.masterReposUrl == null) ? (other.masterReposUrl != null) : !this.masterReposUrl.equals(other.masterReposUrl))
      return false;
    if ((this.masterReposDriver == null) ? (other.masterReposDriver != null) : !this.masterReposDriver.equals(other.masterReposDriver))
      return false;
    if ((this.masterReposUser == null) ? (other.masterReposUser != null) : !this.masterReposUser.equals(other.masterReposUser))
      return false;
    if ((this.masterReposPassword == null) ? (other.masterReposPassword != null) : !this.masterReposPassword.equals(other.masterReposPassword))
      return false;
    if ((this.workReposName == null) ? (other.workReposName != null) : !this.workReposName.equals(other.workReposName))
      return false;
    if ((this.odiUsername == null) ? (other.odiUsername != null) : !this.odiUsername.equals(other.odiUsername))
      return false;
    if ((this.odiPassword == null) ? (other.odiPassword != null) : !this.odiPassword.equals(other.odiPassword))
      return false;
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 53 * hash + (this.masterReposUrl != null ? this.masterReposUrl.hashCode() : 0);
    hash = 53 * hash + (this.masterReposDriver != null ? this.masterReposDriver.hashCode() : 0);
    hash = 53 * hash + (this.masterReposUser != null ? this.masterReposUser.hashCode() : 0);
    hash = 53 * hash + (this.masterReposPassword != null ? this.masterReposPassword.hashCode() : 0);
    hash = 53 * hash + (this.workReposName != null ? this.workReposName.hashCode() : 0);
    hash = 53 * hash + (this.odiUsername != null ? this.odiUsername.hashCode() : 0);
    hash = 53 * hash + (this.odiPassword != null ? this.odiPassword.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return "ConnectionArgs{" + "masterReposUrl=" + masterReposUrl + "masterReposDriver=" + masterReposDriver + "masterReposUser=" + masterReposUser + "masterReposPassword=" + masterReposPassword + "workReposName=" + workReposName + "odiUsername=" + odiUsername + "odiPassword=********" +  '}';
  }

  public String missingArg()
  {
    if (this.masterReposUrl == null)
      return "masterReposUrl";
    if (this.masterReposDriver == null)
      return "masterReposDriver";
    if (this.masterReposUser == null)
      return "masterReposUser";
    if (this.masterReposPassword == null)
      return "masterReposPassword";
    if (this.odiUsername == null)
      return "odiUsername";
    if (this.odiPassword == null)
      return "odiPassword";
    return null;
  }
}
