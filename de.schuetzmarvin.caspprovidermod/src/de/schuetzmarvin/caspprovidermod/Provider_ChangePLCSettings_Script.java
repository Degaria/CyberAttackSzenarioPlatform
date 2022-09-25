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

public class Provider_ChangePLCSettings_Script implements IProvider {

    @Override
    public List<ValuesEnum> getNeededValuesAutomatic() {
        List<ValuesEnum> neede_values = List.of(ValuesEnum.USERNAME, ValuesEnum.PASSWORD,ValuesEnum.IP_ADDRESS,ValuesEnum.PLC_TYP);
        return neede_values;
    }

    @Override
    public List<ValuesEnum> getNeededValuesManual() {
        List<ValuesEnum> neede_values = List.of(ValuesEnum.USERNAME, ValuesEnum.PASSWORD,ValuesEnum.IP_ADDRESS,ValuesEnum.PLC_TYP,ValuesEnum.NEWIP_ADDRESS);
        return neede_values;
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
        ArrayList<String> all_information = new ArrayList<>();
        String information_plc="";
        ArrayList<String> information_user_pw_ip = new ArrayList<>();
        File dir = new File("parameterFiles\\");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    if(file.getName().equals("look_up_plc_information_parameter_output.xml")){
                        information_plc = plc_from_lpi(file);
                    }
                    if(file.getName().equals("hydra_parameter_output.xml")){
                        information_user_pw_ip = information_from_hydra(file);
                    }
                }
            }
        }
        all_information.add(information_user_pw_ip.get(0));
        all_information.add(information_user_pw_ip.get(1));
        all_information.add(information_user_pw_ip.get(2));
        all_information.add(information_plc);
        return all_information;
    }

    private String plc_from_lpi(File file) throws ParserConfigurationException, IOException, SAXException {
        String all_information = "";
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
                String device_string = getString("PLC_TYPE", device_Element);
                System.out.println(device_string);
                if((all_information.contains(device_string)) == false) {
                    all_information = device_string;
                }
            }
        }
        return all_information;
    }

    private ArrayList<String> information_from_hydra(File file) throws ParserConfigurationException, IOException, SAXException {
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
                String username = getString("USERNAME", resultElement);
                String password = getString("PASSWORD", resultElement);
                String ip = getString("ADDRESS", resultElement);
                all_information.add(username);
                all_information.add(password);
                all_information.add(ip);
            }
        }

        return all_information;
    }

    @Override
    public ArrayList<ArrayList<String>> getParametersforExecutionmultipleValues() throws ParserConfigurationException, IOException, SAXException {
        return null;
    }

    public ArrayList<String> get_information_for_cps(String file_hydra, String file_lupi) throws IOException, SAXException, ParserConfigurationException {
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
