package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspconvertermod.FormatEqualizerClass;
import de.schuetzmarvin.caspconvertermod.IFormatEqualizer;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderSkripte;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class SkiptRunnerClass implements IScriptrunner {
    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        SkiptRunnerClass skript_test = new SkiptRunnerClass();
        skript_test.run("lookup_plc_information.py");
    }
    @Override
    public boolean run(String script) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        IProvider provider = new ProviderSkripte();
        IFormatEqualizer equalizer = new FormatEqualizerClass();
        String file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\nmap_complete_output.xml";
        String path_from_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.txt";
        String path_to_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.json";
        String final_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.xml";
        ArrayList<String> ip_adress_list = provider.get_ip_address(file);
        String arrayListString = String.join(" ",ip_adress_list);
        String command3= "python " + script + " " + arrayListString;
        String command2 =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts & start cmd.exe /k " + command3;
        Runtime.getRuntime().exec(command2);
        Thread.sleep(5000);
        equalizer.txt_to_json(path_from_file, path_to_file);
        equalizer.json_to_xml(path_to_file, final_file);
        return true;
    }
}
