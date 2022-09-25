package de.schuetzmarvin.caspprovidermod;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProviderStorage implements IProvider{

    @Override
    public List<ValuesEnum> getNeededValuesAutomatic() {
        return null;
    }

    @Override
    public List<ValuesEnum> getNeededValuesManual() {
        return null;
    }

    @Override
    public List<ValuesEnum> getProvidedValues() {
        return null;
    }


    @Override
    public String getFilePath(File file) {
        return file.getAbsolutePath();
    }

    @Override
    public void saveFile(String value, String filename) throws IOException {
        File file = new File(filename);
        if(file.exists() == false) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(getFilePath(file),false);
        writer.write(value);
        writer.close();
        return;
    }

    @Override
    public boolean isXml(String path_to_file) {
        String extension = FilenameUtils.getExtension(path_to_file);
        String xml_extension = "xml";
        if(extension.equals(xml_extension)) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<String> getParametersforExecution() throws ParserConfigurationException, IOException, SAXException {
        return null;
    }

    @Override
    public ArrayList<ArrayList<String>> getParametersforExecutionmultipleValues() throws ParserConfigurationException, IOException, SAXException {
        return null;
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
