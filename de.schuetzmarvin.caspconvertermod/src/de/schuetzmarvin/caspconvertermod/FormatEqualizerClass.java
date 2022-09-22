package de.schuetzmarvin.caspconvertermod;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;

public class FormatEqualizerClass implements IFormatEqualizer {
    public static void main(String[] args) throws IOException {
        FormatEqualizerClass f = new FormatEqualizerClass();
        f.txt_to_json("C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.txt", "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.json");
        f.hydra_json_to_xml("C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\hydra_output.json","C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\hydra_output.xml");
        f.json_to_xml("C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.json", "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.xml");
        //String path_from_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.txt";
        //String path_to_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.json";
        //f.txt_to_json(path_from_file, path_to_file);
        /*
        String str = "{\"menu\": {\n" +
                "  \"id\": \"file\",\n" +
                "  \"value\": \"File\",\n" +
                "  \"popup\": {\n" +
                "    \"menuitem\": [\n" +
                "      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n" +
                "      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n" +
                "      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n" +
                "    ]\n" +
                "  }\n" +
                "}}";
        JSONObject json = new JSONObject(str);
        FileWriter writer = new FileWriter("C:\\Users\\schue\\Desktop\\Eigene Projekte\\CyberAttackSzenarioPlatform\\tool_output\\test.xml");
        String xml = XML.toString(json);
        System.out.print(xml);
        writer.write(xml);
        writer.close();


        FileWriter writer2 = new FileWriter("C:\\Users\\schue\\Desktop\\Eigene Projekte\\CyberAttackSzenarioPlatform\\tool_output\\test.json");
        JSONObject json1 = XML.toJSONObject(xml);
        String str_json1 = json1.toString();
        System.out.print(str_json1);
        writer2.write(str_json1);
        writer2.close();

        f.json_to_xml(JSONParser.getJSONFromFile("C:\\Users\\schue\\Desktop\\Eigene Projekte\\CyberAttackSzenarioPlatform\\tool_output\\test.json"));
        */
    }

    @Override
    public void json_to_xml(String jsonFile, String path_to_xml_file) throws IOException {
        String json_string = new String(Files.readAllBytes(Paths.get(jsonFile)));
        FileWriter writer = new FileWriter(path_to_xml_file);
        JSONObject json = new JSONObject(json_string);
        String xml = XML.toString(json);
        String well_formed_xml = well_form_xml_with_root_element(xml);
        writer.write(well_formed_xml);
        writer.close();
    }

    public void hydra_json_to_xml(String file_from, String file_to) throws IOException {
        String json = new String(Files.readAllBytes((Paths.get(file_from))));
        json = json.replace("\\", "/"); //hat 3 h gedauert
        JSONObject obj = new JSONObject(json);
        String xml = XML.toString(obj);
        String well_formed_xml = well_form_xml_with_root_element(xml);
        FileWriter writer = new FileWriter(file_to);
        writer.write(well_formed_xml);
        writer.close();
    }

    public String well_form_xml_with_root_element(String xml_string){
        List<InputStream> stream = Arrays.asList(new ByteArrayInputStream("<root>".getBytes()), new ByteArrayInputStream(xml_string.getBytes(StandardCharsets.UTF_8)), new ByteArrayInputStream("</root>".getBytes()));
        InputStream container = new SequenceInputStream(Collections.enumeration(stream));
        String xml = new BufferedReader(new InputStreamReader(container, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        return xml;
    }

    @Override
    public String get_file_extension(String path_to_file) {
        return FilenameUtils.getExtension(path_to_file);
    }

    public void txt_to_json(String file_from, String file_to) {
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


        System.out.println("FERTIG!");
    }
}
