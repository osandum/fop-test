package com.example.fop;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopConfParser;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;

public class FopConfig {
  private final static Log LOG = LogFactory.getLog(FopConfig.class);
  private final static URL CONFIG_XML = FopConfig.class.getResource("config.xml");      

  private final FopFactory fopFactory;

  private FopConfig(FopFactory fopFactory) {
    this.fopFactory = fopFactory;
  }

  public static FopConfig getInstance() {
    return load(CONFIG_XML);      
  }

  public static FopConfig load(URL configXml) {
    FopConfParser confParser;
    try (InputStream is = configXml.openStream()) {
      URI baseUri = configXml.toURI();
      confParser = new FopConfParser(is, baseUri);
    }
    catch (Exception ex) {
      throw new IllegalArgumentException("bad " + configXml + ": " + ex.getMessage());
    }

    FopFactoryBuilder buildFactory = confParser.getFopFactoryBuilder();
    FopFactory fopFactory = buildFactory.build();

    LOG.info(configXml + " loaded");
    
    return new FopConfig(fopFactory);
  }

  public void convert(Source fo, OutputStream out) {
    Result res = newPdfResult(out);
    Transformer xslt = newTransformer();
    try {
      xslt.transform(fo, res);
    }
    catch (TransformerException ex) {
      throw new RuntimeException(fo.getSystemId() + " failed", ex);
    }
  }
  
  private Result newPdfResult(OutputStream out) {
    FOUserAgent ua = fopFactory.newFOUserAgent();
    try {
      Fop fop = ua.newFop(MimeConstants.MIME_PDF, out);
      return new SAXResult(fop.getDefaultHandler());
    }
    catch (FOPException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  private Transformer newTransformer() {
    try {
      return TransformerFactory.newInstance().newTransformer();
    }
    catch (TransformerConfigurationException ex) {
      throw new RuntimeException(ex);
    }
  }
}
