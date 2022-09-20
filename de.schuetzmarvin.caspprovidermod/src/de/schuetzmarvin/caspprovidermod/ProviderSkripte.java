package de.schuetzmarvin.caspprovidermod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import org.jsoup.*;
//import org.jsoup.nodes.Document;
//import org.jsoup.parser.Parser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class ProviderSkripte implements IProvider {
    @Override
    public boolean check_if_is_done(String file) throws IOException {
        return false;
    }

    @Override
    public boolean is_xml(String path_to_file) {
        return false;
    }

    @Override
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



