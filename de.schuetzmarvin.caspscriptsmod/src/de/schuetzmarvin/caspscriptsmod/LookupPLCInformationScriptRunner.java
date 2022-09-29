package de.schuetzmarvin.caspscriptsmod;

import de.schuetzmarvin.caspconvertermod.ConverterAdapterLookupPLCInformationScriptRunner;
import de.schuetzmarvin.caspconvertermod.IConverter;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.Provider_LookupPLCInformation_Script;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


    // Klasse zur Ausführung des change_plc_settings.py Skripts
public class LookupPLCInformationScriptRunner implements IScriptrunner<String> {

    // Initialisierung eines Provider-Objekts
    IProvider provider;

    // Initialisierung eines Converter-Objekts
    IConverter converter;

    // Konstruktor der Klasse + deklaration des Provider- und Converter-Objekts
    public LookupPLCInformationScriptRunner(){
        this.provider = new Provider_LookupPLCInformation_Script();
        this.converter = new ConverterAdapterLookupPLCInformationScriptRunner();
    }

        /*
           run-Methode mit Parameter, die bei der Ausführung des Skripts mit Parameterübergabe durch den Nutzer verwendet wird und sich die benötigten Informationen
           aus dessen Eingabe bezieht. Die Parameter werden bei der Ausführung des Skripts über die Kommandozeile an dieses übergeben.
           Die output-Datei wird anschließend dem Converter zur Übersetzung übergeben.
        */
    @Override
    public boolean run(String ip) throws IOException, InterruptedException, ParserConfigurationException, SAXException, TransformerException {
        if(!new File("CASPStorage\\tool_outputs\\look_up_plc_information_output.txt").exists()){
            new File("CASPStorage\\tool_outputs\\look_up_plc_information_output.txt").createNewFile();
        }
        Thread.sleep(10000);
        String command_python= "python lookup_plc_information.py " + ip;
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\CASPStorage\\scripts\\ & start cmd.exe /k " + command_python;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(20000);
        this.converter.toXmlfile(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\look_up_plc_information_output.txt")));
        return true;
    }

    // gibt den Provider zurück
    public IProvider getProvider(){
        return this.provider;
    }

    // überprüft, ob das Skript manuell konfiguriert werden kann, indem es sich nach den benötigten Werten erkundigt.
    public boolean checkIFconfiguration() {
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
    public boolean run() throws InterruptedException, SAXException, TransformerException, ParserConfigurationException, IOException {
        if(!new File("CASPStorage\\tool_outputs\\look_up_plc_information_output.txt").exists()){
            new File("CASPStorage\\tool_outputs\\look_up_plc_information_output.txt").createNewFile();
        }
        Thread.sleep(60000);
        ArrayList<String> ip_adress_list = this.provider.getParametersforExecution();
        String arrayListString = String.join(" ",ip_adress_list);
        String command_python= "python lookup_plc_information.py " + arrayListString;
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Users\\mar20266\\Documents\\SVN\\BA\\CyberAttackSzenarioPlatform\\CASPStorage\\scripts\\ & start cmd.exe /k " + command_python;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(30000); //Für den Converter
        this.converter.toXmlfile(this.provider.getFilePath(new File("CASPStorage\\tool_outputs\\look_up_plc_information_output.txt")));
        return true;
    }
}
