package de.schuetzmarvin.casphydramod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface IHydra {
    boolean run(String tool_parameters) throws InterruptedException, IOException, TransformerException, SAXException, ParserConfigurationException;
    boolean run() throws IOException, InterruptedException, TransformerException, SAXException, ParserConfigurationException;
    boolean checkIFconfiguration();
    String TOOL_NAME = "Hydra";
}
