package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspconvertermod.ConverterAdapterLookupPLCInformationScript;
import de.schuetzmarvin.caspconvertermod.IConverter;
import de.schuetzmarvin.caspprovidermod.Provider_LookupPLCInformation_Script;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LookupPLCInformationScriptRunner implements IScriptrunner<String> {

    @Override
    public boolean run(String script, String ip) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        if(!new File("tool_outputs\\look_up_plc_information_output.txt").exists()){
            new File("tool_outputs\\look_up_plc_information_output.txt").createNewFile(); //TODO: Machs schöner!!!
        }
        Thread.sleep(60000);
        Provider_LookupPLCInformation_Script provider = new Provider_LookupPLCInformation_Script();
        IConverter converter = new ConverterAdapterLookupPLCInformationScript();
        ArrayList<String> ip_adress_list = provider.get_ip_address(provider.getFilePath(new File("tool_outputs\\nmap_output.xml")));
        String arrayListString = String.join(" ",ip_adress_list);
        System.out.println(arrayListString);
        String command_python= "python " + script + " " + arrayListString;
        System.out.println(command_python);
        //String command_python= "python " + script + " " + ip;
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\scripts & start cmd.exe /k " + command_python;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(30000); //Für den Converter
        converter.toXmlfile(provider.getFilePath(new File("tool_outputs\\look_up_plc_information_output.txt")));
        return true;
    }
}
