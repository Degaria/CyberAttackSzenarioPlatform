package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspconvertermod.ConverterAdapterChangePLCSettingsScript;
import de.schuetzmarvin.caspconvertermod.IConverter;
import de.schuetzmarvin.caspprovidermod.Provider_ChangePLCSettings_Script;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChangePLCSettingsScriptRunner implements IScriptrunner<String> {
    String new_ip = "192.168.1.30";
    Provider_ChangePLCSettings_Script provider;
    IConverter converter;

    public ChangePLCSettingsScriptRunner(){
        this.provider = new Provider_ChangePLCSettings_Script();
        this.converter = new ConverterAdapterChangePLCSettingsScript();
    }

   /* @Override
    public boolean run(String script, String new_ip) throws IOException, InterruptedException, ParserConfigurationException, SAXException, TransformerException {




        String command_one_for_reset_ip = "python " + script + " admin wago 192.168.1.30 880 192.168.1.2";
        String command_two_for_reset_ip =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts\\ & start cmd.exe /k " + command_one_for_reset_ip;
        Runtime.getRuntime().exec(command_two_for_reset_ip);
        Thread.sleep(10000);

    }*/
        public Provider_ChangePLCSettings_Script getProvider(){
            return this.provider;
        }

        public boolean checkIFconfiguration(){
            if(this.getProvider().getNeededValuesManual().isEmpty()){
                return false;
            }
            return true;
        }

    public boolean run() throws InterruptedException, IOException, SAXException, ParserConfigurationException, TransformerException {
        if (!new File("tool_outputs\\change_plc_settings_output.txt").exists()) {
            new File("tool_outputs\\change_plc_settings_output.txt").createNewFile(); //TODO: Machs schöner!!!
        }
        Thread.sleep(10000);

        //ArrayList<String> information_array_list = this.provider.get_information_for_cps(this.provider.getFilePath(new File("tool_outputs\\hydra_output.xml")), this.provider.getFilePath(new File("tool_outputs\\look_up_plc_information_output.xml")));
        ArrayList<String> information_array_list = provider.getParametersforExecution();

        if (new_ip.equals("192.168.1.2")) {
            information_array_list.set(2, "192.168.1.30");
            new_ip = "192.168.1.30";
        }else{
            new_ip = "192.168.1.2";
        }
        System.out.println(information_array_list);
        String information_string = String.join(" ", information_array_list);
        information_string = information_string + " " + new_ip;
        System.out.println(information_string);
        String command_one_for_change_ip = "python change_plc_settings.py " + information_string;
        String command_two_for_change_ip = "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts\\ & start cmd.exe /k " + command_one_for_change_ip;
        Runtime.getRuntime().exec(command_two_for_change_ip);
        Thread.sleep(60000);
        this.converter.toXmlfile(this.provider.getFilePath(new File("tool_outputs\\change_plc_settings_output.txt")));
        return true;
    }

    public boolean run(String tool_parameters) throws InterruptedException, IOException, TransformerException, SAXException, ParserConfigurationException {
        if (!new File("tool_outputs\\change_plc_settings_output.txt").exists()) {
            new File("tool_outputs\\change_plc_settings_output.txt").createNewFile(); //TODO: Machs schöner!!!
        }
        Thread.sleep(10000);

        String command_one_for_change_ip = "python change_plc_settings.py " + tool_parameters;
        String command_two_for_change_ip = "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts\\ & start cmd.exe /k " + command_one_for_change_ip;
        Runtime.getRuntime().exec(command_two_for_change_ip);
        Thread.sleep(60000);
        this.converter.toXmlfile(this.provider.getFilePath(new File("tool_outputs\\change_plc_settings_output.txt")));
        return true;
    }
}
