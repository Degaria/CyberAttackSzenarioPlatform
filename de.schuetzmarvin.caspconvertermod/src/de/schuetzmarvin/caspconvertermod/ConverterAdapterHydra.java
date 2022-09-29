package de.schuetzmarvin.caspconvertermod;

import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderStorage;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
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

    /*
        Diese Klasse ist dafür verantwortlich den Output des Hydra Tools in ein XML Format zu überführen.
        Außerdem ist Sie dafür zuständig unter Verwendung der XSL (hydra_output_template.xsl) und der Output-XML (hydra_output.xml) die XML-Datei (hydra_parameter_output)
        zu erstellen, die den folgenden Tools als Informationsquelle dient.
     */
public class ConverterAdapterHydra implements IConverter{

    // Provider der den XML-Format String und den Pfad zur XML-Datei entgegennimmt, um den String in diese zu schreiben.
    private IProvider provider = new ProviderStorage();



    /*
        Methode zur Umformung der Output-Datei in das XML-Format.
        Die Methode nimmt den Pfad Der Output-Datei des Tools (hydra_output.json) entgegen und prüft, ob diese bereits im XML-Format vorliegt. Falls dies der Fall ist führt sie lediglich die Methode changeTagName
        aus, andernfalls überführt sie den Inhalt der Datei in einen XML-String, welcher zusammen mit dem Dateipfad für die XML (hydra_output.xml) an den Provider übergeben wird.
        Des Weiteren übergibt sie die hydra_output.xml and die Methode changeTagName(File file).
     */
    @Override
    public void toXmlfile(String file) throws IOException, TransformerException, SAXException, ParserConfigurationException {
        if(provider.isXml(file) == true){
            changeTagName(new File(file));
            return;
        }
        String json = new String(Files.readAllBytes((Paths.get(file))));
        json = json.replace("\\", "/");
        JSONObject obj = new JSONObject(json);
        String xml = XML.toString(obj);
        String well_formed_xml = well_form_xml_with_root_element(xml);
        provider.saveFile(well_formed_xml, "CASPStorage\\tool_outputs\\hydra_output.xml");
        changeTagName(new File("CASPStorage\\tool_outputs\\hydra_output.xml"));
    }





    /*
         Die Methode ist dafür verantwortlich die überarbeiteten XML-Output-Dateien zu ertstellen, die den Tools als Informationsquelle dienen. Ihr wird die XML-Output-Datei
         übergeben. Mit dieser und der zum Tool gehörigen XSL-Datei (hydra_output_template.xsl) erstellt es eine neue Datei (hydra_parameter_output.xml),
         in welche der transformierte XML-Stream übertragen wird.
    */
    @Override
    public void changeTagName(File file) throws IOException, TransformerException {
        File XSLFILE = new File("CASPStorage\\xslTemplates\\hydra_output_template.xsl");
        File XMLFILE = file;
        File OUTPUT = new File("CASPStorage\\parameterFiles\\hydra_parameter_output.xml");
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





    /*
        Diese Methode nimmt einen String im XML-Format entgegen und sorgt dafür diesen in ein well-formed-xml-Format zu bringen.
     */
    public String well_form_xml_with_root_element(String xml_string){
        List<InputStream> stream = Arrays.asList(new ByteArrayInputStream("<root>".getBytes()), new ByteArrayInputStream(xml_string.getBytes(StandardCharsets.UTF_8)), new ByteArrayInputStream("</root>".getBytes()));
        InputStream container = new SequenceInputStream(Collections.enumeration(stream));
        String xml = new BufferedReader(new InputStreamReader(container, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        return xml;
    }
}
