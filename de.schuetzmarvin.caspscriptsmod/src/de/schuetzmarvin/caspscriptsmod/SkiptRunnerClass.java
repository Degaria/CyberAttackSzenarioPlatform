package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspconvertermod.ConverterAdapterScripts;
import de.schuetzmarvin.caspconvertermod.IConverter;
import de.schuetzmarvin.caspprovidermod.IProvider;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

/*

public class SkiptRunnerClass implements IScriptrunner {
    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        SkiptRunnerClass skript_test = new SkiptRunnerClass();
        skript_test.run("lookup_plc_information.py");
        LookupPLCInformationScriptRunner s = new LookupPLCInformationScriptRunner();
    }
    @Override
    public boolean run(String script) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        IProvider provider = new ProviderScripts();
        IConverter converter = new ConverterAdapterScripts();
        String file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\nmap_complete_output.xml";
        String path_from_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.txt";
        String path_to_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.json";
        String final_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.xml";
        ArrayList<String> ip_adress_list = ((ProviderScripts) provider).get_ip_address(provider.getFilePath(new File("nmap_output.xml")));
        String arrayListString = String.join(" ",ip_adress_list);
        String command_python= "python " + script + " " + arrayListString;
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts & start cmd.exe /k " + command_python;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(5000);
        converter.toXmlfile(provider.getFilePath(new File("look_up_plc_information_output.txt")));
        return true;
    }
}

 */
