package de.schuetzmarvin.caspconvertermod;

import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderHydra;
import de.schuetzmarvin.caspprovidermod.ProviderStorage;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterAdapterHydra implements IConverter{
    private ProviderStorage provider = new ProviderStorage();
    @Override
    public void toXmlfile(String file) throws IOException, TransformerException, SAXException, ParserConfigurationException {
        if(provider.isXml(file) == true){
            changeTagName(new File(file));
            return;
        }
        String json = new String(Files.readAllBytes((Paths.get(file))));
        json = json.replace("\\", "/"); //hat 3 h gedauert
        JSONObject obj = new JSONObject(json);
        String xml = XML.toString(obj);
        String well_formed_xml = well_form_xml_with_root_element(xml);
        provider.saveFile(well_formed_xml, "tool_outputs\\hydra_output.xml");
        changeTagName(new File("tool_outputs\\hydra_output.xml"));
        return;
    }

    @Override
    public void changeTagName(File file) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        File XSLFILE = new File("xslTemplates\\hydra_output_template.xsl");
        File XMLFILE = file;
        File OUTPUT = new File("parameterFiles\\hydra_parameter_output.xml");
        if(OUTPUT.exists() == false) {
            OUTPUT.createNewFile();
        }

        StreamSource xslcode = new StreamSource(XSLFILE);
        StreamSource inputfile = new StreamSource(XMLFILE);
        StreamResult outputfile = new StreamResult(OUTPUT);

        TransformerFactory transformer_factory = TransformerFactory.newInstance();

        Transformer transformer = transformer_factory.newTransformer(xslcode);

        transformer.transform(inputfile,outputfile);
    }


    public String well_form_xml_with_root_element(String xml_string){
        List<InputStream> stream = Arrays.asList(new ByteArrayInputStream("<root>".getBytes()), new ByteArrayInputStream(xml_string.getBytes(StandardCharsets.UTF_8)), new ByteArrayInputStream("</root>".getBytes()));
        InputStream container = new SequenceInputStream(Collections.enumeration(stream));
        String xml = new BufferedReader(new InputStreamReader(container, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        return xml;
    }
}
