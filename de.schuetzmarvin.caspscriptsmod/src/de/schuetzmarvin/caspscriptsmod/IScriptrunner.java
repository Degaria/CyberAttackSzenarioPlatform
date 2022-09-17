package de.schuetzmarvin.caspscriptsmod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface IScriptrunner {
    boolean run(String script) throws IOException, InterruptedException, ParserConfigurationException, SAXException;
}
