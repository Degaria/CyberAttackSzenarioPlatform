package de.schuetzmarvin.caspprovidermod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProviderOutput implements IProvider {
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

    /*@Override
    public boolean check_if_is_done(String file) throws IOException {
        return false;
    }

    @Override
    public boolean is_xml(String path_to_file) {
        return false;
    }

    @Override
    public ArrayList<String> get_ip_address(String file) throws ParserConfigurationException, IOException, SAXException {
        return null;
    }

    @Override
    public ArrayList<String> get_information(String file_hydra, String file_lupi) throws IOException, SAXException, ParserConfigurationException {
        return null;
    }*/
}
