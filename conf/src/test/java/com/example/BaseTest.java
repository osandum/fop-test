package com.example;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.Fop;
import static org.junit.Assert.*;
import org.apache.fop.apps.FopConfParser;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class BaseTest {
    private final static URL CONFIG_XML = BaseTest.class.getClassLoader().getResource("com/example/fop/config.xml");
    private final static URL HELLO_FO = BaseTest.class.getResource("doc/hello.fo");

    @Before
    public void setup() {
        assertNotNull(CONFIG_XML);
        assertNotNull(HELLO_FO);
    }
    
    @Test
    public void testConfigSetup() throws IOException, URISyntaxException, SAXException, TransformerException {
        FopConfParser confParser = new FopConfParser(CONFIG_XML.openStream(), CONFIG_XML.toURI());        
        assertNotNull(confParser);
        
        FopFactoryBuilder buildFactory = confParser.getFopFactoryBuilder();
        assertNotNull(buildFactory);
        
        FopFactory fopFactory = buildFactory.build();
        assertNotNull(fopFactory);
        
        File helloPdf = new File("hello.pdf");
        FileOutputStream out = new FileOutputStream(helloPdf);
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
        assertNotNull(fop);

        StreamSource fo = new StreamSource(HELLO_FO.toString());
        SAXResult res = new SAXResult(fop.getDefaultHandler());
        TransformerFactory.newInstance().newTransformer().transform(fo, res);

        System.out.println("wrote " + helloPdf.getAbsolutePath() + " (" + helloPdf.length() + " byte(s))");
    }
}
