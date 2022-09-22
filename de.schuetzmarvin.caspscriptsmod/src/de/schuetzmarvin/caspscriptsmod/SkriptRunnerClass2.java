package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspconvertermod.FormatEqualizerClass;
import de.schuetzmarvin.caspconvertermod.IFormatEqualizer;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderSkripte;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class SkriptRunnerClass2 implements IScriptrunner{
    private static final String IP_ADDRESSE_CHANGE = "192.168.1.30";
    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        SkriptRunnerClass2 s = new SkriptRunnerClass2();
        s.run("change_plc_settings.py");
    }

    public boolean run(String script) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        IProvider provider = new ProviderSkripte();
        IFormatEqualizer equalizer = new FormatEqualizerClass();
        String file_hydra = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\hydra_output.xml";
        String file_lupi = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.xml";
        String path_from_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\change_plc_settings_output.txt";
        String path_to_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\change_plc_settings_output.json";
        String final_file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\change_plc_settings_output.xml";
        ArrayList<String> information_array_list = provider.get_information(file_hydra,file_lupi);
        System.out.println(information_array_list);
        String information_string = String.join(" ",information_array_list);
        information_string = information_string + " " + IP_ADDRESSE_CHANGE;
        System.out.println(information_string);
        String command_one_for_change_ip = "python " + script + " " + information_string;
        String command_two_for_change_ip =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts\\ & start cmd.exe /k " + command_one_for_change_ip;
        String command_one_for_reset_ip = "python " + script + " admin wago 192.168.1.30 880 192.168.1.2";
        String command_two_for_reset_ip =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts\\ & start cmd.exe /k " + command_one_for_reset_ip;
        Runtime.getRuntime().exec(command_two_for_change_ip);
        Thread.sleep(60000);
        Runtime.getRuntime().exec(command_two_for_reset_ip);
        Thread.sleep(10000);
        equalizer.txt_to_json(path_from_file, path_to_file);
        equalizer.json_to_xml(path_to_file, final_file);
        return true;
    }
}
