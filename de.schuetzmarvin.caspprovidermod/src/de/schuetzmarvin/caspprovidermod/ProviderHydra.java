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
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProviderHydra implements IProvider{

    @Override
    public List<ValuesEnum> getNeededValuesAutomatic() {
        List<ValuesEnum> needed_values = List.of(ValuesEnum.IP_ADDRESS,ValuesEnum.PORT_ID);
        return needed_values;
    }

    @Override
    public List<ValuesEnum> getNeededValuesManual() {
        List<ValuesEnum> needed_values = List.of(ValuesEnum.IP_ADDRESS,ValuesEnum.DICTIONARY,ValuesEnum.USERNAME,ValuesEnum.PORT_ID);
        return needed_values;
    }

    @Override
    public List<ValuesEnum> getProvidedValues() {
        List<ValuesEnum> _valueEnums = List.of(ValuesEnum.IP_ADDRESS, ValuesEnum.PORT_SERVICE_NAME, ValuesEnum.PORT_ID, ValuesEnum.USERNAME, ValuesEnum.PASSWORD);
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

    @Override
    public ArrayList<String> getParametersforExecution() throws ParserConfigurationException, IOException, SAXException {
        return null;
    }

    @Override
    public ArrayList<ArrayList<String>> getParametersforExecutionmultipleValues() throws ParserConfigurationException, IOException, SAXException {
        ArrayList<ArrayList<String>> all_information = new ArrayList<>();
        ArrayList<String> ip_address = new ArrayList<>();
        ArrayList<String> ports = new ArrayList<>();
        ArrayList<String> information_tool;
        ArrayList<String> information_tool2;
        File dir = new File("parameterFiles\\");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals("nmap_parameter_output.xml")) {
                        information_tool = ip_from_nmap(file);
                        for (String value: information_tool) {
                            if( (ip_address.isEmpty()==false) &&ip_address.contains(value)){

                            }else{
                                ip_address.add(value);
                            }
                        }
                        information_tool2 = ports_from_nmap(file);
                        for (String value: information_tool2) {
                            System.out.println(value);
                            if((ports.isEmpty() == false) && ports.contains(value)){

                            }else{
                                ports.add(value);
                            }
                        }
                    }
                    if(file.getName().equals("look_up_plc_information_parameter_output.xml")){
                        information_tool = ip_from_lpi(file);
                        for (String value: information_tool) {
                            if( (ip_address.isEmpty()==false) && ip_address.contains(value)){

                            }else{
                                ip_address.add(value);
                            }
                        }
                       // ports = null;
                    }
                    if(file.getName().equals("hydra_parameter_output.xml")){
                        information_tool = ip_from_hydra(file);
                        for (String value: information_tool) {
                            if( (ip_address.isEmpty()==false) && ip_address.contains(value)){

                            }else{
                                ip_address.add(value);
                            }
                        }
                        information_tool2 = ports_from_hydra(file);
                        for (String value: information_tool2) {
                            if( (ports.isEmpty() == false) && ports.contains(value)){

                            }else{
                                ports.add(value);
                            }
                        }
                    }
                }
            }
        }
        all_information.add(ip_address);
        all_information.add(ports);
        return all_information;
    }

    private ArrayList<String> ports_from_hydra(File file) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));
        document.getDocumentElement().normalize();
        //System.out.println(document.getDocumentElement().getNodeName());
        NodeList info_hydra_list = document.getElementsByTagName("results");
        for (int i = 0; i < info_hydra_list.getLength(); i++) {
            Node result = info_hydra_list.item(i);
            //System.out.println(result);

            if (result.getNodeType() == Node.ELEMENT_NODE) {

                Element resultElement = (Element) result;
                String port = getString("PORT", resultElement);
                System.out.println(port);
                all_information.add(port);
            }
        }

        return all_information;

    }

    private ArrayList<String> ports_from_nmap(File file) throws ParserConfigurationException, IOException, SAXException {
        boolean helper_for_double_values = false;
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));
        document.getDocumentElement().normalize();
        //System.out.println(document.getDocumentElement().getNodeName());
        NodeList info_list = document.getElementsByTagName("PORT");
        for (int i = 0; i < info_list.getLength(); i++) {
            Node result = info_list.item(i);
            //System.out.println(result);

            if (result.getNodeType() == Node.ELEMENT_NODE) {
                Element resultElement = (Element) result;
                    String port = resultElement.getAttribute("portid");
                    if (all_information.size() == 0) {
                        all_information.add(port);
                    }
                    for (String string : all_information) {
                        if (port.equals(string)) {
                            helper_for_double_values = true;
                        }
                    }
                    if (helper_for_double_values == false) {
                        all_information.add(port);
                    }
            }
            helper_for_double_values = false;
        }
        return all_information;
    }

    private ArrayList<String> ip_from_nmap(File file) throws ParserConfigurationException, SAXException, IOException {
        boolean helper_for_double_values = false;
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        //System.out.println(document.getDocumentElement().getNodeName());
        NodeList info_list = document.getElementsByTagName("ADDRESS");
        for (int i = 0; i < info_list.getLength(); i++) {
            Node result = info_list.item(i);
            //System.out.println(result);

            if (result.getNodeType() == Node.ELEMENT_NODE) {
                Element resultElement = (Element) result;

                if (resultElement.hasAttribute("vendor")) {

                } else {
                    String ip_address = resultElement.getAttribute("addr");
                    if (all_information.size() == 0) {
                        all_information.add(ip_address);
                    }
                    for (String string : all_information) {
                        if (ip_address.equals(string)) {
                            helper_for_double_values = true;
                        }
                    }

                    if (helper_for_double_values == false) {
                        all_information.add(ip_address);
                    }
                }
            }
            helper_for_double_values = false;
        }
        return all_information;
    }

    private ArrayList<String> ip_from_lpi(File file) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        NodeList info_list = document.getElementsByTagName("Info1"); //TODO: Sollte später für mehre Devices möglich sein.
        for (int i = 0; i < info_list.getLength(); i++) {
            Node device_node = info_list.item(i);
            //System.out.println(plc_type);

            if (device_node.getNodeType() == Node.ELEMENT_NODE) {
                Element device_Element = (Element) device_node;
                System.out.println(device_Element);
                String device_string = getString("ADDRESS", device_Element);
                System.out.println(device_string);
                if((all_information.contains(device_string)) == false) {
                    all_information.add(device_string);
                }
            }
        }
        return all_information;
    }

    private ArrayList<String> ip_from_hydra(File file) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));
        document.getDocumentElement().normalize();
        //System.out.println(document.getDocumentElement().getNodeName());
        NodeList info_hydra_list = document.getElementsByTagName("results");
        for (int i = 0; i < info_hydra_list.getLength(); i++) {
            Node result = info_hydra_list.item(i);
            //System.out.println(result);

            if (result.getNodeType() == Node.ELEMENT_NODE) {

                Element resultElement = (Element) result;
                String ip_address = getString("ADDRESS", resultElement);
                System.out.println(ip_address);
                all_information.add(ip_address);
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

