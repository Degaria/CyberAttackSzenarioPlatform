package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderSkripte;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;


public class SkiptRunnerClass implements IScriptrunner {
    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        SkiptRunnerClass skript_test = new SkiptRunnerClass();
        skript_test.run("bla");
    }
    @Override
    public boolean run(String script) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        IProvider provider = new ProviderSkripte();
        String file = "C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\tool_outputs\\nmap_complete_output.xml";
        ArrayList<String> ip_adress_list = provider.get_ip_address(file);
        for(int i=0; i <= ip_adress_list.size()-1; i++){
            System.out.println(ip_adress_list.get(i));
        }
        return true;
    }
}
