package de.schuetzmarvin.caspformatequalizermod;

import java.io.*;

 class JSONParser {
    public static String getJSONFromFile(String file){
        String json_text = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            while((line = bufferedReader.readLine()) != null){
                json_text += line + "\n";
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json_text;
    }
}