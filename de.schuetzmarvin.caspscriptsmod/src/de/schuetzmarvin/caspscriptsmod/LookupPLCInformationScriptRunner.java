package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspconvertermod.ConverterAdapterLookupPLCInformationScript;
import de.schuetzmarvin.caspconvertermod.IConverter;
import de.schuetzmarvin.caspprovidermod.Provider_LookupPLCInformation_Script;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LookupPLCInformationScriptRunner implements IScriptrunner<String> {
    Provider_LookupPLCInformation_Script provider;
    IConverter converter;

    public LookupPLCInformationScriptRunner(){
        this.provider = new Provider_LookupPLCInformation_Script();
        this.converter = new ConverterAdapterLookupPLCInformationScript();
    }

    @Override
    public boolean run(String ip) throws IOException, InterruptedException, ParserConfigurationException, SAXException, TransformerException {
        if(!new File("tool_outputs\\look_up_plc_information_output.txt").exists()){
            new File("tool_outputs\\look_up_plc_information_output.txt").createNewFile(); //TODO: Machs schöner!!!
        }
        Thread.sleep(60000);
        String command_python= "python lookup_plc_information.py " + ip;
        System.out.println(command_python);
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts & start cmd.exe /k " + command_python;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(30000); //Für den Converter
        this.converter.toXmlfile(this.provider.getFilePath(new File("tool_outputs\\look_up_plc_information_output.txt")));
        return true;
    }

    public Provider_LookupPLCInformation_Script getProvider(){
        return this.provider;
    }

    public boolean checkIFconfiguration() {
        if(this.getProvider().getNeededValuesManual().isEmpty()){
            return false;
        }

        return true;
    }

    public boolean run() throws InterruptedException, SAXException, TransformerException, ParserConfigurationException, IOException {
        if(!new File("tool_outputs\\look_up_plc_information_output.txt").exists()){
            new File("tool_outputs\\look_up_plc_information_output.txt").createNewFile(); //TODO: Machs schöner!!!
        }
        Thread.sleep(60000);
        //ArrayList<String> ip_adress_list = this.provider.get_ip_address(this.provider.getFilePath(new File("tool_outputs\\nmap_output.xml")));
        ArrayList<String> ip_adress_list = this.provider.getParametersforExecution();
        String arrayListString = String.join(" ",ip_adress_list);
        System.out.println(arrayListString);
        String command_python= "python lookup_plc_information.py " + arrayListString;
        System.out.println(command_python);
        //String command_python= "python " + script + " " + ip;
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts & start cmd.exe /k " + command_python;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(30000); //Für den Converter
        this.converter.toXmlfile(this.provider.getFilePath(new File("tool_outputs\\look_up_plc_information_output.txt")));
        return true;
    }
}
