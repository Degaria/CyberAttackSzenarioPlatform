package de.schuetzmarvin.caspprovidermod;

import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProviderNmap implements IProvider {

    @Override
    public List<ValuesEnum> getNeededValuesAutomatic() {
        List<ValuesEnum> needed_values = List.of(ValuesEnum.IP_ADDRESS);
        return needed_values;
    }

    @Override
    public List<ValuesEnum> getNeededValuesManual() {
        List<ValuesEnum> needed_values = List.of(ValuesEnum.IP_ADDRESS);
        return needed_values;
    }

    @Override
    public List<ValuesEnum> getProvidedValues() {
        List<ValuesEnum> _valueEnums = List.of(ValuesEnum.IP_ADDRESS, ValuesEnum.MAC_ADDRESS, ValuesEnum.PORT_ID, ValuesEnum.PORT_PROTOCOL, ValuesEnum.PORT_STATUS, ValuesEnum.VENDOR, ValuesEnum.PORT_SERVICE_NAME);
        return _valueEnums;
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
