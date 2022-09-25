package de.schuetzmarvin.casphydramod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface IHydra {
    boolean run(String ip_address, String dictionary, String port, String username) throws IOException, InterruptedException, TransformerException, SAXException, ParserConfigurationException;
}
