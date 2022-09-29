package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspconvertermod.ConverterAdapterChangePLCSettingsScriptRunner;
import de.schuetzmarvin.caspconvertermod.IConverter;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.Provider_ChangePLCSettings_Script;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

    // Klasse zur Ausführung des change_plc_settings.py Skripts
public class ChangePLCSettingsScriptRunner implements IScriptrunner<String> {

    // vordefinierte IP-Adresse
    String new_ip = "192.168.1.2";

    // Initialisierung eines Provider-Objekts
    IProvider provider;

    // Initialisierung eines Converter-Objekts
    IConverter converter;

    // Konstruktor der Klasse + deklaration des Provider- und Converter-Objekts
    public ChangePLCSettingsScriptRunner(){
        this.provider = new Provider_ChangePLCSettings_Script();
        this.converter = new ConverterAdapterChangePLCSettingsScriptRunner();
    }

        // gibt den Provider zurück
        public Provider_ChangePLCSettings_Script getProvider(){
            return (Provider_ChangePLCSettings_Script) this.provider;
        }

        // überprüft, ob das Skript manuell konfiguriert werden kann, indem es sich nach den benötigten Werten erkundigt.
        public boolean checkIFconfiguration(){
            if(this.getProvider().getNeededValuesManual().isEmpty()){
                return false;
            }
            return true;
        }


        /*
            run-Methode ohne Parameter, die bei der Ausführung des Skripts ohne Parameterübergabe durch den Nutzer verwendet wird und sich die benötigten Informationen
            aus den Output_Dateien vorheriger Tools besorgt. Die Parameter, die dem Skript übergeben werden sollen, werden aus den ermittelten Informationen bezogen
            und bei der Ausführung des Skripts über die Kommandozeile an dieses übergeben. Die Output-Datei wird anschließend dem Converter zur Übersetzung übergeben.
         */
    public boolean run() throws InterruptedException, IOException, SAXException, ParserConfigurationException, TransformerException {
        if (!new File("CASPStorage\\tool_outputs\\change_plc_settings_output.txt").exists()) {
            new File("CASPStorage\\tool_outputs\\change_plc_settings_output.txt").createNewFile();
        }
        Thread.sleep(10000);

        //ArrayList<String> information_array_list = this.provider.get_information_for_cps(this.provider.getFilePath(new File("tool_outputs\\hydra_output.xml")), this.provider.getFilePath(new File("tool_outputs\\look_up_plc_information_output.xml")));
        ArrayList<String> information_array_list = provider.getParametersforExecution();

        if (new_ip.equals("192.168.1.2")) {
            new_ip = "192.168.1.30";
        }else{
            information_array_list.set(2, "192.168.1.30");
            new_ip = "192.168.1.2";
        }
        String information_string = String.join(" ", information_array_list);
        information_string = information_string + " " + new_ip;
        String command_one_for_change_ip = "python change_plc_settings.py " + information_string;
        String command_two_for_change_ip = "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\CASPStorage\\scripts\\ & start cmd.exe /k " + command_one_for_change_ip;
        Runtime.getRuntime().exec(command_two_for_change_ip);
        Thread.sleep(40000);
        this.converter.toXmlfile(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\change_plc_settings_output.txt")));
        return true;
    }

        /*
            run-Methode mit Parameter, die bei der Ausführung des Skripts mit Parameterübergabe durch den Nutzer verwendet wird und sich die benötigten Informationen
            aus dessen Eingabe bezieht. Die Parameter werden bei der Ausführung des Skripts über die Kommandozeile an dieses übergeben.
            Die output-Datei wird anschließend dem Converter zur Übersetzung übergeben.
         */
    public boolean run(String tool_parameters) throws InterruptedException, IOException, TransformerException, SAXException, ParserConfigurationException {
        if (!new File("CASPStorage\\tool_outputs\\change_plc_settings_output.txt").exists()) {
            new File("CASPStorage\\tool_outputs\\change_plc_settings_output.txt").createNewFile();
        }
        Thread.sleep(10000);

        String command_one_for_change_ip = "python change_plc_settings.py " + tool_parameters;
        String command_two_for_change_ip = "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\CASPStorage\\scripts\\ & start cmd.exe /k " + command_one_for_change_ip;
        Runtime.getRuntime().exec(command_two_for_change_ip);
        Thread.sleep(60000);
        this.converter.toXmlfile(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\change_plc_settings_output.txt")));
        return true;
    }
}
