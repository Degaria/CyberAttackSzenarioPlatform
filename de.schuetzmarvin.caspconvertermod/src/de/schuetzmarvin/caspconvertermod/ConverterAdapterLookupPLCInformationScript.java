package de.schuetzmarvin.caspconvertermod;

import de.schuetzmarvin.caspprovidermod.ProviderStorage;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterAdapterLookupPLCInformationScript implements IConverter {
    private ProviderStorage provider = new ProviderStorage();
    @Override
    public void toXmlfile(String file) throws IOException {
        if(provider.isXml(file) == true){
            return;
        }
        txt_to_json(file, provider.getFilePath(new File("tool_outputs\\look_up_plc_information_output.json")));
    }

    public void txt_to_json(String file_from, String file_to) throws IOException {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_from), Charset.forName("UTF-8")))) {
            JSONObject main_obj = new JSONObject();
            JSONObject device = new JSONObject();
            int i = 1;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                if(device.isEmpty()) {
                    device.put(words[0], words[1]);
                }else{
                    if(device.has(words[0])){
                        main_obj.put("Device" + i, device);
                        device = new JSONObject();
                        device.put(words[0], words[1]); // hat 2 1/2 Stunden gedauert
                        i++;
                    }else{
                        device.put(words[0], words[1]);
                    }
                }
            }
            main_obj.put("Device" + i, device);
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

    public void json_to_xml(String jsonFile) throws IOException {
        String json_string = new String(Files.readAllBytes(Paths.get(jsonFile)));
        JSONObject json = new JSONObject(json_string);
        String xml = XML.toString(json);
        String well_formed_xml = well_form_xml_with_root_element(xml);
        provider.saveFile(well_formed_xml,"tool_outputs\\look_up_plc_information_output.xml");
    }

    public String well_form_xml_with_root_element(String xml_string){
        List<InputStream> stream = Arrays.asList(new ByteArrayInputStream("<root>".getBytes()), new ByteArrayInputStream(xml_string.getBytes(StandardCharsets.UTF_8)), new ByteArrayInputStream("</root>".getBytes()));
        InputStream container = new SequenceInputStream(Collections.enumeration(stream));
        String xml = new BufferedReader(new InputStreamReader(container, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        return xml;
    }
}
