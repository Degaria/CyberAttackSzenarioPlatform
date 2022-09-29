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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

    /*
        Diese Klasse ist dafür verantwortlich den Output des lookup_plc_information.py Skripts in ein XML Format zu überführen.
        Außerdem ist Sie dafür zuständig unter Verwendung der XSL (look_up_plc_information_output_template.xsl) und der Output-XML (look_up_plc_information_output.xml) die
        XML-Datei (look_up_plc_information_parameter_output.xml) zu erstellen, die den folgenden Tools als Informationsquelle dient.
     */
public class ConverterAdapterLookupPLCInformationScriptRunner implements IConverter {

    //Provider der den XML-Format String und den Pfad zur XML-Datei entgegennimmt, um den String in diese zu schreiben.
    private IProvider provider = new ProviderStorage();



    /*
        Methode zur Umformung der Output-Datei in das XML-Format.
        Die Methode nimmt den Pfad Der Output-Datei des Skripts entgegen und prüft, ob diese bereits im XML-Format vorliegt. Falls dies der Fall ist führt sie lediglich die Methode changeTagName
        aus, andernfalls leitet Sie die Übersetzung ein.
     */
    @Override
    public void toXmlfile(String file) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        if(provider.isXml(file) == true){
            return;
        }
        txt_to_json(file, provider.getFilePath(new File("CASPStorage\\tool_outputs\\look_up_plc_information_output.json")));
    }




    /*
        Die Methode ist dafür verantwortlich die überarbeiteten XML-Output-Dateien zu ertstellen, die den Tools als Informationsquelle dienen. Ihr wird die XML-Output-Datei
        übergeben. Mit dieser und der zum Tool gehörigen XSL-Datei (look_up_plc_information_output_template.xsl) erstellt es eine neue Datei (look_up_plc_information_parameter_output.xml),
        in welche der transformierte XML-Stream übertragen wird.
     */
    @Override
    public void changeTagName(File file) throws IOException, TransformerException {
        File XSLFILE = new File("CASPStorage\\xslTemplates\\look_up_plc_information_output_template.xsl");
        File XMLFILE = file;
        File OUTPUT = new File("CASPStorage\\parameterFiles\\look_up_plc_information_parameter_output.xml");
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
       Die Methode ist dafür verantwortlich die TXT-output-Datei des Tools in ein Json-Format zu überführen und dieses in einer Json-Datei abzulegen (look_up_plc_information_output.json)
       Abschließend übergibt sie die den Pfad zu Json-Datei an die Methode json_to_xml(String file).
    */
    public void txt_to_json(String file_from, String file_to) throws IOException, ParserConfigurationException, TransformerException, SAXException {
        String line;
        int i = 1;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_from), Charset.forName("UTF-8")))) {
            JSONObject main_obj = new JSONObject();
            JSONObject device = new JSONObject();
            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                if(device.isEmpty()) {
                    device.put(words[0], words[1]);
                }else{
                    if(device.has(words[0])){
                        main_obj.append("Info" + i, device);
                        device = new JSONObject();
                        device.put(words[0], words[1]);
                        i++;
                    }else{
                        device.put(words[0], words[1]);
                    }
                }
            }
            main_obj.append("Info" + i, device);
            FileWriter file_writer = new FileWriter(file_to);
            file_writer.write(main_obj.toString());
            file_writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        json_to_xml(file_to);
    }




    /*
        Diese Methode nimmt den Pfad einer Json-Datei entgegen und überführt den Inhalt dieser in ein XML-Format, welches anschließend zusammen mit dem Verzeichnispfad zur
        entsprechenden XML-Datei (look_up_plc_information_output.xml) and das Provider-Objekt übergeben wird.  Des Weiteren übergibt sie die hydra_output.xml and die Methode
        changeTagName(File file).
     */
    public void json_to_xml(String jsonFile) throws IOException, TransformerException {
        String json_string = new String(Files.readAllBytes(Paths.get(jsonFile)));
        JSONObject json = new JSONObject(json_string);
        String xml = XML.toString(json);
        String well_formed_xml = well_form_xml_with_root_element(xml);
        provider.saveFile(well_formed_xml,"CASPStorage\\tool_outputs\\look_up_plc_information_output.xml");
        changeTagName(new File("CASPStorage\\tool_outputs\\look_up_plc_information_output.xml"));
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
