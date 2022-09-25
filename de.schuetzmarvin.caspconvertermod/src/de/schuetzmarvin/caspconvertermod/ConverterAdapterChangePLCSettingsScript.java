package de.schuetzmarvin.caspconvertermod;

import de.schuetzmarvin.caspprovidermod.ProviderStorage;
import org.json.JSONObject;
import org.json.XML;

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

public class ConverterAdapterChangePLCSettingsScript implements IConverter {
    public static void main(String[] args) throws IOException, TransformerException {
        ProviderStorage provider2 = new ProviderStorage();
        ConverterAdapterChangePLCSettingsScript converter = new ConverterAdapterChangePLCSettingsScript();
        File file = new File("tool_outputs\\change_plc_settings_output.txt");
        String file_string = provider2.getFilePath(file);
        converter.toXmlfile(file_string);
    }
    private ProviderStorage provider = new ProviderStorage();
    @Override
    public void toXmlfile(String file) throws IOException, TransformerException {
        if(provider.isXml(file) == true){
            changeTagName(new File(file));
            return;
        }

        txt_to_json(file,provider.getFilePath(new File("tool_outputs\\change_plc_settings_output.json")));
        //txt_to_json(file,provider.getFilePath(new File("tool_outputs\\Test.json")));
    }

    @Override
    public void changeTagName(File file) throws IOException, TransformerException {
        File XSLFILE = new File("xslTemplates\\change_plc_settings_template.xsl");
        File XMLFILE = file;
        File OUTPUT = new File("parameterFiles\\change_plc_settings_parameter_output.xml");
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

    public void txt_to_json(String file_from, String file_to) throws IOException, TransformerException {
        String line;
        int i = 1;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_from), Charset.forName("UTF-8")))) {
            JSONObject main_obj = new JSONObject();
            JSONObject device = new JSONObject();
            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                if(device.isEmpty()) {
                    device.put(words[0], words[1]);
                    //main_obj.put("INFORMATION", true);
                }else{
                    if(device.has(words[0])){
                        main_obj.append("Info" + i, device);
                        device = new JSONObject();
                        device.put(words[0], words[1]); // hat 2 1/2 Stunden gedauert
                        i++;
                    }else{
                        device.put(words[0], words[1]);
                    }
                }
            }
            main_obj.append("Info" + i, device);
            System.out.println("Object: " + main_obj);
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

    public void json_to_xml(String jsonFile) throws IOException, TransformerException {
        String json_string = new String(Files.readAllBytes(Paths.get(jsonFile)));
        JSONObject json = new JSONObject(json_string);
        String xml = XML.toString(json);
        String well_formed_xml = well_form_xml_with_root_element(xml);
        provider.saveFile(well_formed_xml,"tool_outputs\\change_plc_settings_output.xml");
        //changeTagName(new File("tool_outputs\\change_plc_settings_output.xml"));
    }

    public String well_form_xml_with_root_element(String xml_string){
        List<InputStream> stream = Arrays.asList(new ByteArrayInputStream("<root>".getBytes()), new ByteArrayInputStream(xml_string.getBytes(StandardCharsets.UTF_8)), new ByteArrayInputStream("</root>".getBytes()));
        InputStream container = new SequenceInputStream(Collections.enumeration(stream));
        String xml = new BufferedReader(new InputStreamReader(container, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        return xml;
    }
}
