package de.schuetzmarvin.caspnmapmod;

import de.schuetzmarvin.caspconvertermod.ConverterAdapterNmap;
import de.schuetzmarvin.caspconvertermod.IConverter;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderNmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Nmap implements INmap {
    public final String TOOL_NAME = "Nmap";
    @Override
    public boolean run(String ip_address) throws IOException, InterruptedException {
        if(!new File("tool_outputs\\nmap_output.xml").exists()){
            new File("tool_outputs\\nmap_output.xml").createNewFile(); //TODO: Machs schöner!!!
        }
        IProvider provider = new ProviderNmap();
        IConverter converter = new ConverterAdapterNmap();
        String nmap_command= "nmap -oX " + provider.getFilePath(new File("tool_outputs\\nmap_output.xml")) + " -p 1-100 " + ip_address;
        String cmd_exec_command =  "cmd.exe /c cd  C:\\Program Files (x86)\\Nmap\\ & start cmd.exe /k " + nmap_command;
        Process proc = Runtime.getRuntime().exec(cmd_exec_command);
        while(check_if_is_done(provider.getFilePath(new File("tool_outputs\\nmap_output.xml"))) == true) {
            proc.waitFor();
        }
        proc.destroy();
        converter.toXmlfile(provider.getFilePath(new File("tool_outputs\\nmap_output.xml")));
        return true;
    }

    private boolean check_if_is_done(String file) throws IOException {
        BufferedReader checker_empty_file = new BufferedReader(new FileReader(file));
        String lastline = "";
        String lastlinereminder = "";
        while ((lastline = checker_empty_file.readLine()) != null) {
            lastlinereminder = lastline;
        }
        if(lastlinereminder.equals("</nmaprun>")){
            return false;
        }
        return true;
    }

}
