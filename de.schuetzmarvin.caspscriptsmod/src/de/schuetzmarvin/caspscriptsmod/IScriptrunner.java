package de.schuetzmarvin.caspscriptsmod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface IScriptrunner<String> {
    boolean run(String ip) throws IOException, InterruptedException, ParserConfigurationException, SAXException, TransformerException;
}
