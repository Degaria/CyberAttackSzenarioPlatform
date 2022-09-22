package de.schuetzmarvin.caspprovidermod;

import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProviderNmap implements IProvider {
    @Override
    public List<Needed_Values> getNeededValues() {
        List<Needed_Values> needed_values = List.of(Needed_Values.IP_ADDRESS);
        return needed_values;
    }

    @Override
    public List<Provided_Values> getProvidedValues() {
        List<Provided_Values> provided_values = List.of(Provided_Values.IP_ADDRESS, Provided_Values.MAC_ADDRESS, Provided_Values.PORT_ID, Provided_Values.PORT_PROTOCOL, Provided_Values.PORT_STATUS, Provided_Values.VENDOR,Provided_Values.PORT_SERVICE_NAME);
        return provided_values;
    }

    @Override
    public ArrayList<String> getNeededParameters() {
        ArrayList<String> needed_parametes = new ArrayList<>();
        needed_parametes.add("ip_address");
        return needed_parametes;
    }

    @Override
    public String getFilePath(File file) {
        return file.getAbsolutePath();
    }

    @Override
    public void saveFile(String value) throws IOException {

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
