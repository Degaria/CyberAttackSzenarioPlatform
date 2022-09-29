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


    // Provider Klasse für die Hydra Klasse. Sie stellt eine Reihe von Methoden Implementationen zur Verfügung, die die Instanzen der Hydra Klasse mit den notwendigen Informationen versorgen.
public class ProviderHydra implements IProvider{


    // gibt die Werte zurück, die zur Ausführung des Tools in der parameterfreien run-Methode zur Verfügung stehen müssen, um erfolreich ausgeführt werden zu können.
    @Override
    public List<ValuesEnum> getNeededValuesAutomatic() {
        List<ValuesEnum> needed_values = List.of(ValuesEnum.IP_ADDRESS,ValuesEnum.PORT_ID);
        return needed_values;
    }


    // gibt die Werte zurück, die zur Ausführung des Tools in der parametrierten run-Methode zur Verfügung stehen müssen, um erfolreich ausgeführt werden zu können.
    @Override
    public List<ValuesEnum> getNeededValuesManual() {
        List<ValuesEnum> needed_values = List.of(ValuesEnum.IP_ADDRESS,ValuesEnum.DICTIONARY,ValuesEnum.USERNAME,ValuesEnum.PORT_ID);
        return needed_values;
    }


    // gibt die Werte zurück, die nach der Ausführung des Tools, durch dessen output zur Verfügung gestellt werden.
    @Override
    public List<ValuesEnum> getProvidedValues() {
        List<ValuesEnum> _valueEnums = List.of(ValuesEnum.IP_ADDRESS, ValuesEnum.PORT_SERVICE_NAME, ValuesEnum.PORT_ID, ValuesEnum.USERNAME, ValuesEnum.PASSWORD);
        return _valueEnums;
    }


    // gibt den absoluten Pfad einer Datei zurück.
    @Override
    public String getFilePath(File file) {
        return file.getAbsolutePath();
    }


    // speichert/schreibt den übergebenen Wert in der übergebenen Datei.
    @Override
    public void saveFile(String value, String filename) throws IOException {
        File file = new File(filename);
        if(file.exists() == false) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(getFilePath(file),false);
        writer.write(value);
        writer.close();
    }


    // gibt für eine Datei zurück, ob sie eine XML-Datei ist oder nicht.
    @Override
    public boolean isXml(String path_to_file) {
        String extension = FilenameUtils.getExtension(path_to_file);
        String xml_extension = "xml";
        if(extension.equals(xml_extension)) {
            return true;
        }
        return false;
    }


    // Methode, die auf vorhandene Informationsquellen vorheriger Tools zurückgreift (CASP/Storage/parameteriles) und aus diesen die benötigten informationen zurückgibt (hier nicht benötigt, aber aufgrund de interfaces mit aufgenommen).
    @Override
    public ArrayList<String> getParametersforExecution() throws ParserConfigurationException, IOException, SAXException {
        return null;
    }


    // Methode, welche im Falle das mehrere verschiedene Parameter für die Ausführung einer Funktion benötigt werden diese zurückgibt.
    @Override
    public ArrayList<ArrayList<String>> getParametersforExecutionmultipleValues() throws ParserConfigurationException, IOException, SAXException {
        ArrayList<ArrayList<String>> all_information = new ArrayList<>();
        ArrayList<String> ip_address = new ArrayList<>();
        ArrayList<String> ports = new ArrayList<>();
        ArrayList<String> information_tool;
        ArrayList<String> information_tool2;
        File dir = new File("CASPStorage\\parameterFiles\\");
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


    // Methode, welche den Port aus einer hydra_parameter_output.xml ausliest und zurückgibt.
    private ArrayList<String> ports_from_hydra(File file) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));
        document.getDocumentElement().normalize();
        NodeList info_hydra_list = document.getElementsByTagName("results");
        for (int i = 0; i < info_hydra_list.getLength(); i++) {
            Node result = info_hydra_list.item(i);

            if (result.getNodeType() == Node.ELEMENT_NODE) {

                Element resultElement = (Element) result;
                String port = getString("PORT", resultElement);
                all_information.add(port);
            }
        }

        return all_information;

    }


    // Methode, welche die Ports aus einer nmap_parameter_output.xml ausliest und zurückgibt.
    private ArrayList<String> ports_from_nmap(File file) throws ParserConfigurationException, IOException, SAXException {
        boolean helper_for_double_values = false;
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));
        document.getDocumentElement().normalize();
        NodeList info_list = document.getElementsByTagName("PORT");
        for (int i = 0; i < info_list.getLength(); i++) {
            Node result = info_list.item(i);

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


    // Methode, welche die IP-Adressen aus einer nmap_parameter_output.xml ausließt und zurückgibt.
    private ArrayList<String> ip_from_nmap(File file) throws ParserConfigurationException, SAXException, IOException {
        boolean helper_for_double_values = false;
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        NodeList info_list = document.getElementsByTagName("ADDRESS");
        for (int i = 0; i < info_list.getLength(); i++) {
            Node result = info_list.item(i);

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


    // Methode, welche die IP-Adresse aus einer look_up_plc_information_parameter_output.xml ausließt und zurückgibt.
    private ArrayList<String> ip_from_lpi(File file) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        NodeList info_list = document.getElementsByTagName("Info1"); //TODO: Sollte später für mehre Devices möglich sein.
        for (int i = 0; i < info_list.getLength(); i++) {
            Node device_node = info_list.item(i);

            if (device_node.getNodeType() == Node.ELEMENT_NODE) {
                Element device_Element = (Element) device_node;
                String device_string = getString("ADDRESS", device_Element);
                if((all_information.contains(device_string)) == false) {
                    all_information.add(device_string);
                }
            }
        }
        return all_information;
    }


    // Methode, welche die IP-Adresse aus einer hydra_parameter_output.xml ausließt und zurückgibt.
    private ArrayList<String> ip_from_hydra(File file) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<String> all_information = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));
        document.getDocumentElement().normalize();
        NodeList info_hydra_list = document.getElementsByTagName("results");
        for (int i = 0; i < info_hydra_list.getLength(); i++) {
            Node result = info_hydra_list.item(i);

            if (result.getNodeType() == Node.ELEMENT_NODE) {

                Element resultElement = (Element) result;
                String ip_address = getString("ADDRESS", resultElement);
                all_information.add(ip_address);
            }
        }

        return all_information;
    }


    // Methode, die einen String für den Value eines Tags einer XML-Datei zurückgibt.
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

