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


    //Provider Klasse für die ChangePLCSettingsScriptRunner Klasse. Sie stellt eine Reihe von Methoden Implementationen zur Verfügung, die die Instanzen der ChangePLCSettingsScriptRunner Klasse mit den notwendigen Informationen versorgen.
public class Provider_ChangePLCSettings_Script implements IProvider {



    // gibt die Werte zurück, die zur Ausführung des Skripts in der parameterfreien run-Methode zur Verfügung stehen müssen, um erfolreich ausgeführt werden zu können.
    @Override
    public List<ValuesEnum> getNeededValuesAutomatic() {
        List<ValuesEnum> neede_values = List.of(ValuesEnum.USERNAME, ValuesEnum.PASSWORD,ValuesEnum.IP_ADDRESS,ValuesEnum.PLC_TYP);
        return neede_values;
    }



    // gibt die Werte zurück, die zur Ausführung des Skripts in der parametrierten run-Methode zur Verfügung stehen müssen, um erfolreich ausgeführt werden zu können.
    @Override
    public List<ValuesEnum> getNeededValuesManual() {
        List<ValuesEnum> neede_values = List.of(ValuesEnum.USERNAME, ValuesEnum.PASSWORD,ValuesEnum.IP_ADDRESS,ValuesEnum.PLC_TYP,ValuesEnum.NEWIP_ADDRESS);
        return neede_values;
    }



    // gibt die Werte zurück, die nach der Ausführung des Skripts, durch dessen output zur Verfügung gestellt werden.
    @Override
    public List<ValuesEnum> getProvidedValues() {
        return null;
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



    // Methode, die auf vorhandene Informationsquellen vorheriger Tools zurückgreift (CASP/Storage/parameteriles) und aus diesen die benötigten informationen zurückgibt.
    @Override
    public ArrayList<String> getParametersforExecution() throws ParserConfigurationException, IOException, SAXException {
        ArrayList<String> all_information = new ArrayList<>();
        String information_plc="";
        ArrayList<String> information_user_pw_ip = new ArrayList<>();
        File dir = new File("CASPStorage\\parameterFiles\\");
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



    // Methode, welche den PLC-Typ aus einer look_up_plc_information_parameter_output.xml auslesen kann.
    private String plc_from_lpi(File file) throws ParserConfigurationException, IOException, SAXException {
        String all_information = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        NodeList info_list = document.getElementsByTagName("Info1");
        for (int i = 0; i < info_list.getLength(); i++) {
            Node device_node = info_list.item(i);

            if (device_node.getNodeType() == Node.ELEMENT_NODE) {
                Element device_Element = (Element) device_node;
                String device_string = getString("PLC_TYPE", device_Element);
                if((all_information.contains(device_string)) == false) {
                    all_information = device_string;
                }
            }
        }
        return all_information;
    }



    // Methode, welche in der Lage ist Informationen aus einer hydra_parameter-output.xml auszulesen und zu übergeben.
    private ArrayList<String> information_from_hydra(File file) throws ParserConfigurationException, IOException, SAXException {
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



    // Methode, welche im Falle das mehrere verschiedene Parameter für die Ausführung einer Funktion benötigt werden diese zurückgibt (hier nicht benötigt, aber durch das gemeinsame Interface implementiert).
    @Override
    public ArrayList<ArrayList<String>> getParametersforExecutionmultipleValues(){
        return null;
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
