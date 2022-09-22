package de.schuetzmarvin.caspprovidermod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProviderStorage implements IProvider<String> {
    @Override
    public List<Needed_Values> getNeededValues() {
        return null;
    }

    @Override
    public List<Provided_Values> getProvidedValues() {
        return null;
    }

    @Override
    public ArrayList<String> getNeededParameters() {
        return null;
    }

    @Override
    public String getFilePath(File file) {
        return null;
    }

    @Override
    public void saveFile(String value) throws IOException {

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
