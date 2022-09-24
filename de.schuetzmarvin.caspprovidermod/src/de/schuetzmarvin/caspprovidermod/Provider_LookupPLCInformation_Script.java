package de.schuetzmarvin.caspprovidermod;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Provider_LookupPLCInformation_Script implements IProvider {
    @Override
    public List<Needed_Values> getNeededValues() {
        List<Needed_Values> needed_values = List.of(Needed_Values.IP_ADDRESS);
        return needed_values;
    }

    @Override
    public List<Provided_Values> getProvidedValues() {
        List<Provided_Values> provided_values = List.of(Provided_Values.IP_ADDRESS,Provided_Values.FIRMWARE_REVISION,Provided_Values.MAC_ADDRESS,Provided_Values.ORDER_NUMBER,Provided_Values.PLC_TYP);
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

    public ArrayList<String> get_ip_address(String file) throws ParserConfigurationException, IOException, SAXException {
        boolean helper_for_double_values = false;
        ArrayList<String> ip_address_list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(file));
        document.getDocumentElement().normalize();
        NodeList addresses_list = document.getElementsByTagName("address");
        for (int i = 0; i < addresses_list.getLength(); i++) {
            Node address = addresses_list.item(i);

            if (address.getNodeType() == Node.ELEMENT_NODE) {

                Element addressElement = (Element) address;

                if (addressElement.hasAttribute("vendor")) {

                } else {
                    String ip_address = addressElement.getAttribute("addr");
                    if (ip_address_list.size() == 0) {
                        ip_address_list.add(ip_address);
                    }
                    for (String string : ip_address_list) {
                        if (ip_address.equals(string)) {
                            helper_for_double_values = true;
                        }
                    }

                    if(helper_for_double_values == false){
                        ip_address_list.add(ip_address);
                    }
                }
            }
            helper_for_double_values = false;
        }
        return ip_address_list;
    }
}
