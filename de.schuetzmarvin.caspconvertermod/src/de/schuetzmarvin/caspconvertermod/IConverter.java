package de.schuetzmarvin.caspconvertermod;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IConverter {
    void toXmlfile(String File) throws IOException, SAXException, ParserConfigurationException, TransformerException;
    void changeTagName(File file) throws ParserConfigurationException, IOException, SAXException, TransformerException;
}
