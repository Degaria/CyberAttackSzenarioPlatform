package de.schuetzmarvin.caspprovidermod;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProviderHydra implements IProvider{
    @Override
    public List<Needed_Values> getNeededValues() {
        List<Needed_Values> needed_values = List.of(Needed_Values.IP_ADDRESS,Needed_Values.DICTIONARY,Needed_Values.USERNAME,Needed_Values.PORT);
        return needed_values;
    }

    @Override
    public List<Provided_Values> getProvidedValues() {
        List<Provided_Values> provided_values = List.of(Provided_Values.IP_ADDRESS,Provided_Values.PORT_SERVICE_NAME, Provided_Values.PORT_ID, Provided_Values.USERNAME,Provided_Values.PASSWORD);
        return provided_values;
    }

    @Override
    public ArrayList<String> getNeededParameters() {
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

   /* @Override
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
    }

    */
}

