package de.schuetzmarvin.casphydramod;

import de.schuetzmarvin.caspconvertermod.FormatEqualizerClass;
import de.schuetzmarvin.caspconvertermod.IFormatEqualizer;
import de.schuetzmarvin.caspconvertermod.JSONParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Hydra {

    private static final String DICT_PATH = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\documents\\dictionary.txt" ;
    private static final String FILE_PATH = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\hydra_output.json";
    private static final String XML_PATH =  "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\hydra_output.xml";
    private JSONParser parser = new JSONParser();

    public static void main(String[] args) throws IOException, InterruptedException {

        Hydra h = new Hydra();
        h.run("192.168.1.1", "bla","21", "admin");
    }

    public void run(String ip_address, String dictionary, String port, String username) throws IOException, InterruptedException {
        IFormatEqualizer converter = new FormatEqualizerClass();
        String hydra_command = "hydra -o " + FILE_PATH + " -b json -s " + port + " -l " + username + " -P " + DICT_PATH + " " + ip_address + " ftp";
        String command =  "cmd.exe /c cd  C:\\Program Files (x86)\\thc-hydra-windows-master\\ & start cmd.exe /k " + hydra_command;
        //Process proc = Runtime.getRuntime().exec(command);
        //Thread.sleep(20000);
        //converter.json_to_xml(JSONParser.getJSONFromFile(FILE_PATH),XML_PATH);
        converter.hydra_json_to_xml(FILE_PATH,XML_PATH);
    }

}
