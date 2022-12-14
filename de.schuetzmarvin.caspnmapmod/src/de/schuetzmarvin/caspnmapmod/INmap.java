package de.schuetzmarvin.caspnmapmod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface INmap {

    boolean run(String ip_adressen) throws IOException, InterruptedException, TransformerException, SAXException, ParserConfigurationException;
    boolean run() throws IOException, ParserConfigurationException, SAXException, InterruptedException, TransformerException;

    boolean checkIFconfiguration();
    String TOOL_NAME = "Nmap";
}
