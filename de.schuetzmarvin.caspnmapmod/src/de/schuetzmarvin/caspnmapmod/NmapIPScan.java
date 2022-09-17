package de.schuetzmarvin.caspnmapmod;

import java.io.*;

import de.schuetzmarvin.caspformatequalizermod.FormatEqualizerClass;
import de.schuetzmarvin.caspprovidermod.ProviderNmapClass;
import org.xml.sax.SAXException;
import de.schuetzmarvin.caspformatequalizermod.IFormatEqualizer;
import javax.xml.parsers.ParserConfigurationException;
import de.schuetzmarvin.caspprovidermod.IProvider;


public class NmapIPScan implements INmap {

    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        NmapIPScan nmaptest = new NmapIPScan();


        //nmaptest.run("192.168.1.1-5");

    }
    @Override
    public boolean run(String ip_adress) throws IOException, InterruptedException {
        IProvider provider = new ProviderNmapClass();
        IFormatEqualizer formatter = new FormatEqualizerClass();
        String command3= "nmap -oX C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\nmap_complete_output.xml " + ip_adress;
        String command2 =  "cmd.exe /c cd  C:\\Program Files (x86)\\Nmap\\ & start cmd.exe /k " + command3;
        String file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\nmap_complete_output.xml";
        ProcessBuilder builder = new ProcessBuilder("nmap", command2);
        builder.directory(new File("C:\\Program Files (x86)\\Nmap\\ "));
        Process proc = Runtime.getRuntime().exec(command2);
        while(provider.check_if_is_done(file) == true) {
            proc.waitFor();
        }
        proc.destroy();
        System.out.println(formatter.is_xml(file));
        return true;
    }



}
