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
import org.apache.log4j.Logger;
import java.io.IOException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

/**
 *
 * @author Tamas Foldi
 */
public class DataStageReader
{

  static Logger logger = Logger.getLogger(DataStageReader.class);
  static final XPathFactory factory = XPathFactory.newInstance();
  static final XPath xpath = factory.newXPath();
  static Document doc = null;

  public static NodeList executeXPath(String query) throws XPathExpressionException, KillaException
  {
    if ( doc == null )
      throw new KillaException("Document is not initalized yet.");
    
    XPathExpression expr = xpath.compile(query);

    return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
  }

  public Document loadFile(String filePath) throws KillaException
  {
    
    try {
      DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
      domFactory.setNamespaceAware(true); // never forget this!

      DocumentBuilder builder = domFactory.newDocumentBuilder();
      doc = builder.parse(filePath);

      NodeList nodes = executeXPath( "//Header");

      logger.info("\"" + filePath + "\" has been successfuly parsed. File was created by "
              + nodes.item(0).getAttributes().getNamedItem("ExportingTool").getNodeValue()
              + ", v" + nodes.item(0).getAttributes().getNamedItem("ToolVersion").getNodeValue()
              + ", from server " + nodes.item(0).getAttributes().getNamedItem("ServerName").getNodeValue()
              + " at " + nodes.item(0).getAttributes().getNamedItem("Date").getNodeValue()
              + " " + nodes.item(0).getAttributes().getNamedItem("Time").getNodeValue());

      return doc;
    } catch (IOException exc) {
      throw new KillaException("Can not load DataStage export file: " + exc.getMessage(),
              exc);
    } catch (Exception exc) {
      throw new KillaException("Error in DataStage export file: " + exc.getMessage(),
              exc);
    }
  }

  public static String propertyValue(Node node, String property) throws KillaException
  {
    Node ret;

    try {
      ret = (Node) xpath.evaluate("./Property[@Name='" + property + "']", node, XPathConstants.NODE);
    } catch (XPathExpressionException exc) {
      throw new KillaException("Cannot get property: " + property
              + "," + exc.getMessage(), exc);
    }

    if ( ret == null )
      return "";
    
    return ret.getTextContent();
  }
}
