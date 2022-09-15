package de.schuetzmarvin.caspformatequalizermod;

import org.json.JSONObject;
import org.json.XML;

import java.io.FileWriter;
import java.io.IOException;

 class FormatEqualizerClass implements IFormatEqualizer {
    public static void main(String[] args) throws IOException {
        FormatEqualizerClass f = new FormatEqualizerClass();

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

    }

    @Override
    public void json_to_xml(String jsonText) throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\schue\\Desktop\\Eigene Projekte\\CyberAttackSzenarioPlatform\\tool_output\\test_json.xml");
        JSONObject json = new JSONObject(jsonText);
        String xml = XML.toString(json);
        writer.write(xml);
        writer.close();
    }
}
