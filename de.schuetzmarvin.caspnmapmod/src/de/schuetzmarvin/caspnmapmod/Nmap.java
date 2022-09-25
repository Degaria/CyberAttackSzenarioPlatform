package de.schuetzmarvin.caspnmapmod;

import de.schuetzmarvin.caspconvertermod.ConverterAdapterNmap;
import de.schuetzmarvin.caspconvertermod.IConverter;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderNmap;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Nmap implements INmap {
    public final String TOOL_NAME;
    IProvider provider;
    IConverter converter;
    public Nmap(){
         this.TOOL_NAME ="Nmap";
         this.provider = new ProviderNmap();
         this.converter = new ConverterAdapterNmap();
    }

    @Override
    public boolean run(String ip_address) throws IOException, InterruptedException, TransformerException, SAXException, ParserConfigurationException {
        if(!new File("tool_outputs\\nmap_output.xml").exists()){
            new File("tool_outputs\\nmap_output.xml").createNewFile(); //TODO: Machs schöner!!!
        }
        String nmap_command= "nmap -oX " + this.provider.getFilePath(new File("tool_outputs\\nmap_output.xml")) + " -p 1-100 " + ip_address;
        String cmd_exec_command =  "cmd.exe /c cd  C:\\Program Files (x86)\\Nmap\\ & start cmd.exe /k " + nmap_command;
        Process proc = Runtime.getRuntime().exec(cmd_exec_command);
        while(check_if_is_done(this.provider.getFilePath(new File("tool_outputs\\nmap_output.xml"))) == true) {
            proc.waitFor();
        }
        proc.destroy();
        converter.toXmlfile(this.provider.getFilePath(new File("tool_outputs\\nmap_output.xml")));
        return true;
    }

    public boolean run() throws IOException, ParserConfigurationException, SAXException, InterruptedException, TransformerException {
        if(!new File("tool_outputs\\nmap_output.xml").exists()){
            new File("tool_outputs\\nmap_output.xml").createNewFile(); //TODO: Machs schöner!!!
        }
        ArrayList<String> ip_address_array = provider.getParametersforExecution();
        String ip_address="";
        for (String value:ip_address_array) {
            ip_address = ip_address + value + " ";
        }
        String nmap_command= "nmap -oX " + this.provider.getFilePath(new File("tool_outputs\\nmap_output.xml")) + " -p 1-100 " + ip_address;
        String cmd_exec_command =  "cmd.exe /c cd  C:\\Program Files (x86)\\Nmap\\ & start cmd.exe /k " + nmap_command;
        Process proc = Runtime.getRuntime().exec(cmd_exec_command);
        while(check_if_is_done(this.provider.getFilePath(new File("tool_outputs\\nmap_output.xml"))) == true) {
            proc.waitFor();
        }
        proc.destroy();
        converter.toXmlfile(this.provider.getFilePath(new File("tool_outputs\\nmap_output.xml")));
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

    public IProvider getProvider(){
        return this.provider;
    }

    public boolean checkIFconfiguration() {
        if(this.getProvider().getNeededValuesManual().isEmpty()){
            return false;
        }

        return true;
    }


}
