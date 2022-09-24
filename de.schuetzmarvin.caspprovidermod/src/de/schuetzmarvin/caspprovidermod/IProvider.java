package de.schuetzmarvin.caspprovidermod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public interface IProvider {
    List<Needed_Values> getNeededValues();
    List<Provided_Values> getProvidedValues();
    ArrayList<String> getNeededParameters();
    String getFilePath(File file) throws IOException;
    void saveFile(String value, String filename) throws IOException;
    boolean isXml(String path_to_file);
    //boolean check_if_is_done(String file) throws IOException;
    //boolean is_xml(String path_to_file);
    //ArrayList<String> get_ip_address(String file) throws ParserConfigurationException, IOException, SAXException;
    //ArrayList<String> get_information(String file_hydra, String file_lupi) throws IOException, SAXException, ParserConfigurationException;
}
