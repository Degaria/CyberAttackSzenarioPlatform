package de.schuetzmarvin.caspprovidermod;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProviderNmapClass implements IProvider{

    @Override
    public ArrayList<String> getNeededValues() {
        return null;
    }

    @Override
    public ArrayList<String> getProvidedValues() {
        return null;
    }

    @Override
    public ArrayList<String> getNeededParameters() {
        return null;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public void saveFile(Object value) {

    }

    @Override
    public boolean isXml(String path_to_file) {
        return false;
    }

    /*
    public boolean check_if_is_done(String file) throws IOException {
        BufferedReader checker_empty_file = new BufferedReader(new FileReader(file));
        String lastline = "";
        String lastlinereminder = "";
        while ((lastline = checker_empty_file.readLine()) != null) {
            lastlinereminder = lastline;
        }
        if(lastlinereminder.equals("</nmaprun>")){
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<String> get_ip_address(String file) {
        return null;
    }

    @Override
    public ArrayList<String> get_information(String file_hydra, String file_lupi) throws IOException, SAXException, ParserConfigurationException {
        return null;
    }

    public boolean is_xml(String path_to_file) {
        String extension = FilenameUtils.getExtension(path_to_file);
        String xml_extension = "xml";
        if(extension.equals(xml_extension)) {
            return true;
        }
        return false;
    }
    
     */
}
