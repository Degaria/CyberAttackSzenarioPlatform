package de.schuetzmarvin.caspnmapmod;

import java.io.*;

import de.schuetzmarvin.caspprovidermod.ProviderNmapClass;
import de.schuetzmarvin.caspprovidermod.IProvider;


public class NmapIPScan implements INmap {

    public static void main(String[] args) {
        NmapIPScan nmaptest = new NmapIPScan();


        //nmaptest.run("192.168.1.1-5");

    }
    @Override
    public boolean run(String ip_adress) throws IOException, InterruptedException {
        IProvider provider = new ProviderNmapClass();
        String command3= "nmap -oX C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\nmap_complete_output.xml " + ip_adress;
        String command2 =  "cmd.exe /c cd  C:\\Program Files (x86)\\Nmap\\ & start cmd.exe /k " + command3;
        String file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\nmap_complete_output.xml";
        //ProcessBuilder builder = new ProcessBuilder("nmap", command2);
        //builder.directory(new File("C:\\Program Files (x86)\\Nmap\\ "));
        Process proc = Runtime.getRuntime().exec(command2);
        while(check_if_is_done(file) == true) {
            proc.waitFor();
        }
        proc.destroy();
        System.out.println(provider.is_xml(file));
        return true;
    }


    public boolean check_if_is_done(String file) throws IOException {
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
