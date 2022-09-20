package de.schuetzmarvin.caspprovidermod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;


public interface IProvider {
    boolean check_if_is_done(String file) throws IOException;
    boolean is_xml(String path_to_file);
    ArrayList<String> get_ip_address(String file) throws ParserConfigurationException, IOException, SAXException;
}
