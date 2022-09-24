package de.schuetzmarvin.caspscriptsmod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface IScriptrunner<String> {
    boolean run(String script, String ip) throws IOException, InterruptedException, ParserConfigurationException, SAXException;
}
