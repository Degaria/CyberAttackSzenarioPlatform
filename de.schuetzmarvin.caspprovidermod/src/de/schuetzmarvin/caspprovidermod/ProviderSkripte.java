/*package de.schuetzmarvin.caspprovidermod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import org.jsoup.*;
//import org.jsoup.nodes.Document;
//import org.jsoup.parser.Parser;


public class ProviderSkripte implements IProvider {
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
    public void saveFile(String value, String filename) throws IOException {

    }

    @Override
    public boolean isXml(String path_to_file) {
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
    public ArrayList<String> get_ip_address_nmap_file(String file) throws ParserConfigurationException, IOException, SAXException {
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

    public ArrayList<String> get_information(String file_hydra, String file_lupi) throws IOException, SAXException, ParserConfigurationException {
        ArrayList<String> all_information = new ArrayList<>();
        ArrayList<String> hydra_information = get_information_hydra(file_hydra);
        ArrayList<String> lupi_information = get_information_lupi(file_lupi);

        for (String element: hydra_information) {
            all_information.add(element);
        }
        for (String element: lupi_information) {
            all_information.add(element);
        }

        return all_information;
    }
    public ArrayList<String> get_information_hydra(String hydra_file) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(hydra_file));
        document.getDocumentElement().normalize();
        //System.out.println(document.getDocumentElement().getNodeName());
        NodeList info_hydra_list = document.getElementsByTagName("results");
        for (int i = 0; i < info_hydra_list.getLength(); i++) {
            Node result = info_hydra_list.item(i);
            //System.out.println(result);

            if (result.getNodeType() == Node.ELEMENT_NODE) {

                Element resultElement = (Element) result;
                String login = getString("login", resultElement);
                System.out.println(login);
                String password = getString("password", resultElement);
                String ip = getString("host", resultElement);
                all_information.add(login);
                all_information.add(password);
                all_information.add(ip);
            }
        }
        return  all_information;
    }

    public ArrayList<String> get_information_lupi(String lupi_file) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(lupi_file));
        document.getDocumentElement().normalize();
        NodeList info_lupi_list = document.getElementsByTagName("Device1"); //TODO: Sollte später für mehre Devices möglich sein.
        for (int i = 0; i < info_lupi_list.getLength(); i++) {
            Node device_node = info_lupi_list.item(i);
            //System.out.println(plc_type);

            if (device_node.getNodeType() == Node.ELEMENT_NODE) {
                Element device_Element = (Element) device_node;
                System.out.println(device_Element);
                String device_string = getString("PLC_Type", device_Element);
                System.out.println(device_string);
                if((all_information.contains(device_string)) == false) {
                    all_information.add(device_string);
                }
            }
        }
        return all_information;
    }

    protected String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            if(list.item(0).hasChildNodes() ==true) {
                NodeList subList = list.item(0).getChildNodes();

                if (subList != null && subList.getLength() > 0) {
                    return subList.item(0).getNodeValue();
                }
            }else{
                    return list.item(0).getNodeValue();
            }
        }

        return null;
    }

}

 */



