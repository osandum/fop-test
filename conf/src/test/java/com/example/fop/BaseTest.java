package com.example.fop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class BaseTest {
    private final static URL HELLO_FO = BaseTest.class.getResource("samples/hello.fo");

    @Before
    public void setup() {
        assertNotNull(HELLO_FO);
    }
    
    @Test
    public void testConfigSetup() throws IOException, URISyntaxException, SAXException, TransformerException {
      FopConfig fop = FopConfig.getInstance();
        
      File helloPdf = new File("hello.pdf");
      FileOutputStream out = new FileOutputStream(helloPdf);

      Source fo = new StreamSource(HELLO_FO.toString());
      fop.convert(fo, out);

      System.out.println("XSL:FO..: " + HELLO_FO);
      System.out.println("PDF.....: " + helloPdf.getAbsolutePath() + " (" + helloPdf.length() + " bytes)");
  }
}
