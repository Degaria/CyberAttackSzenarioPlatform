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

    // Klasse für die Erstellung der finalen Output-Datei.
public class Output implements IOutput {

    // Erzeugung eines Provider-Objektes über das Interface
    IProvider provider = new ProviderOutput();


    /*
        Hauptmethode der Klasse, die sich um die Erstellung der finalen Output-Datei (Final_output.xml) kümmert, indem sie einen XMl-String erstellt, der aus allen einzelnen XML-Strings der
        Output-Dateien der Tools besteht und diesen zusammen mit dem Verzeichnis für die Final_ouput.xml an den Provider übergibt.
     */
    @Override
    public void createReviewForAllOutputs() throws IOException {
        File finalFile = new File(provider.getFilePath(new File("CASPStorage\\tool_outputs\\Final_output.xml")));
        List<String> allFilePaths = getAllPaths();
        List<String> allXmlStrings = getAllXmlStrings(allFilePaths);
        String finalXmlString = createFinalXmlString(allXmlStrings);
        provider.saveFile(finalXmlString,finalFile.toString());
    }



    // Methode um aus allen XMl-Strings einen einzigen XMl-String zu erstellen.
    private String createFinalXmlString(List<String> allXmlStrings) {
        String mainXmlString="";
        for (String xml_string:allXmlStrings) {
            mainXmlString = mainXmlString + xml_string;
        }
        return mainXmlString;
    }



    // Methode, um die Inhalte jeder XML-Output-Dateie als String in einer Liste von Strings abzuspeichern und diese zurückzugeben.
    private List<String> getAllXmlStrings(List<String> allFilePaths) {
        StringBuilder builder = new StringBuilder();
        List<String> xmlStrings = new ArrayList<>();
        String xmlString;
        for (String string: allFilePaths) {
            try{
                BufferedReader buffer = new BufferedReader(new FileReader(string));


                while((xmlString = buffer.readLine()) != null){
                    builder.append(xmlString).append("\n");
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



    // Methode, um alle Pfade für die XML-Output-Dateien zu erhalten und zurückzugeben.
    public List<String> getAllPaths() throws IOException {
        List<String> allPaths = new ArrayList<>();
        List<String> allXmlFiles = gettAllXmlFiles();

        for (String string:allXmlFiles) {
            allPaths.add(provider.getFilePath(new File("CASPStorage\\tool_outputs\\" + string)));
        }
        return allPaths;
    }



    // Methode, um alle XML-Dateien im Verzeichnis CASPStorage/tool_outputs/ zu lokalisieren und deren Namen in einer String-liste abzulegen. Die Liste wird anschließend zurückgegeben.
    public List<String> gettAllXmlFiles(){
        File directory = new File("CASPStorage\\tool_outputs\\");
        File[] listOfFiles = directory.listFiles();
        List<String> mainList = new ArrayList<>();

        for (int i = 0; i <= listOfFiles.length-1; i++) {

            if(provider.isXml("CASPStorage\\tool_outputs\\" + listOfFiles[i].getName())){
                mainList.add(listOfFiles[i].getName());
            }
        }
        return mainList;
    }
}
