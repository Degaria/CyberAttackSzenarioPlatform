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

    // Nmap Klasse mit run-Methoden zur Ausführung eines Nmap-Befehls mit übergebenen Parametern durch den Nutzer oder mittels Informationen vorheriger Tools.
public class Nmap implements INmap {

    // Initialisierung eines Provider Objekts über das IProvider Interface
    IProvider provider;

    // Initialisierung eines Converter Objekts über das IConverter Interface
    IConverter converter;

    // Konstruktor für die Erstellung einer Nmap Instanz + Deklaration von Provider und Converter
    public Nmap(){
         this.provider = new ProviderNmap();
         this.converter = new ConverterAdapterNmap();
    }



    /*
        Die run-Methode, welche genutzt wird, wenn Nmap verwendet wird und der Nutzer Parameter übergeben hat. In dieser wird die Output-Datei erstellt,
        sofern diese noch nicht vorhanden ist, der Befehl für Nmap auf Grundlage von informationen vorheriger Tools zusamengebaut und die Ausführung des Tools Nmap
        mit dem entsprechenden Befehl über die Kommandozeile ausgeführt. Abschließend wird das toXmlfile-Methode des Converters die Output-Datei zur Übersetzung in XMl übergeben.
    */
    @Override
    public boolean run(String ip_address) throws IOException, InterruptedException, TransformerException, SAXException, ParserConfigurationException {
        if(!new File("CASPStorage\\tool_outputs\\nmap_output.xml").exists()){
            new File("CASPStorage\\tool_outputs\\nmap_output.xml").createNewFile();
        }
        String nmap_command= "nmap -oX " + this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\nmap_output.xml")) + " -p 1-100 " + ip_address;
        String cmd_exec_command =  "cmd.exe /c cd  C:\\Program Files (x86)\\Nmap\\ & start cmd.exe /k " + nmap_command;
        Process proc = Runtime.getRuntime().exec(cmd_exec_command);
        while(check_if_is_done(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\nmap_output.xml"))) == true) {
            proc.waitFor();
        }
        proc.destroy();
        converter.toXmlfile(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\nmap_output.xml")));
        Thread.sleep(60000);
        return true;
    }




    /*
        Die run-Methode, welche genutzt wird, wenn Nmap verwendet wird, ohne das der Nutzer Parameter übergeben hat. In dieser wird die Output-Datei erstellt,
        sofern diese noch nicht vorhanden ist, der Befehl für Nmap auf Grundlage von informationen vorheriger Tools zusamengebaut und die Ausführung des Tools Nmap
        mit dem entsprechenden Befehl über die Kommandozeile ausgeführt. Abschließend wird das toXmlfile-Methode des Converters die Output-Datei zur Übersetzung in XMl übergeben.
     */
    public boolean run() throws IOException, ParserConfigurationException, SAXException, InterruptedException, TransformerException {
        if(!new File("CASPStorage\\tool_outputs\\nmap_output.xml").exists()){
            new File("CASPStorage\\tool_outputs\\nmap_output.xml").createNewFile();
        }
        ArrayList<String> ip_address_array = provider.getParametersforExecution();
        String ip_address="";
        for (String value:ip_address_array) {
            ip_address = ip_address + value + " ";
        }
        String nmap_command= "nmap -oX " + this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\nmap_output.xml")) + " -p 1-100 " + ip_address;
        String cmd_exec_command =  "cmd.exe /c cd  C:\\Program Files (x86)\\Nmap\\ & start cmd.exe /k " + nmap_command;
        Process proc = Runtime.getRuntime().exec(cmd_exec_command);
        while(check_if_is_done(this.provider.getFilePath(new File("tool_outputs\\nmap_output.xml"))) == true) {
            proc.waitFor();
        }
        proc.destroy();
        converter.toXmlfile(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\nmap_output.xml")));
        Thread.sleep(60000);
        return true;
    }




    /*
        Methode die prüft, ob Nmap mit dem schreiben in die nmap_output.xml fertig ist.
     */
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



    //gibt den Provider des Nmap-Objekts zurück
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
