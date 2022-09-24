package de.schuetzmarvin.caspoutputmod;

import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderOutput;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Output implements IOutput {
    IProvider provider = new ProviderOutput();

    public static void main(String[] args) throws IOException {
        Output o = new Output();

        o.createReviewForAllOutputs();
    }
    @Override
    public void createReviewForAllOutputs() throws IOException {
        File finalFile = new File(provider.getFilePath(new File("tool_outputs\\Final_output.xml")));
        List<String> allFilePaths = getAllPaths();
        List<String> allXmlStrings = getAllXmlStrings(allFilePaths);
        String finalXmlString = createFinalXmlString(allXmlStrings);
        provider.saveFile(finalXmlString,finalFile.toString());
    }

    private String createFinalXmlString(List<String> allXmlStrings) {
        String mainXmlString="";
        for (String xml_string:allXmlStrings) {
            mainXmlString = mainXmlString + xml_string;
        }
        System.out.println(mainXmlString);
        return mainXmlString;
    }

    private List<String> getAllXmlStrings(List<String> allFilePaths) throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> xmlStrings = new ArrayList<>();
        String xmlString;
        for (String string: allFilePaths) {
            try{
                BufferedReader buffer = new BufferedReader(new FileReader(string));


                while((xmlString = buffer.readLine()) != null){
                    builder.append(xmlString).append("\n");
                    //builder.append(xmlString);
                }
                List<InputStream> stream = Arrays.asList(new ByteArrayInputStream("<NEUE_OUTPUT_FILE>".getBytes()), new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8)), new ByteArrayInputStream("</NEUE_OUTPUT_FILE>".getBytes()));
                InputStream container = new SequenceInputStream(Collections.enumeration(stream));
                String xml = new BufferedReader(new InputStreamReader(container, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                xmlStrings.add(xml);
                builder.setLength(0);

            }catch(Exception e){

            }


        }
        return xmlStrings;
    }

    public List<String> getAllPaths() throws IOException {
        List<String> allPaths = new ArrayList<>();
        List<String> allXmlFiles = gettAllXmlFiles();

        for (String string:allXmlFiles) {
            System.out.println(string);
            System.out.println(provider.getFilePath(new File("tool_outputs\\" + string)));
            allPaths.add(provider.getFilePath(new File("tool_outputs\\" + string)));
        }
        return allPaths;
    }

    public List<String> gettAllXmlFiles(){
        File directory = new File("tool_outputs\\");
        File[] listOfFiles = directory.listFiles();
        List<String> mainList = new ArrayList<>();

        for (int i = 0; i <= listOfFiles.length-1; i++) {

            if(provider.isXml("tool_outputs\\" + listOfFiles[i].getName())){
                mainList.add(listOfFiles[i].getName());
            }
        }
        return mainList;
    }
}
