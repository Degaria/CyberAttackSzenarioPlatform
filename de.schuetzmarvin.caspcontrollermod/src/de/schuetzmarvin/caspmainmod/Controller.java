package de.schuetzmarvin.caspmainmod;


import de.schuetzmarvin.casphydramod.Hydra;
import de.schuetzmarvin.casphydramod.IHydra;
import de.schuetzmarvin.caspnmapmod.INmap;
import de.schuetzmarvin.caspnmapmod.Nmap;
import de.schuetzmarvin.caspoutputmod.IOutput;
import de.schuetzmarvin.caspoutputmod.Output;
import de.schuetzmarvin.caspprovidermod.ValuesEnum;
import de.schuetzmarvin.caspscriptsmod.ChangePLCSettingsScriptRunner;
import de.schuetzmarvin.caspscriptsmod.IScriptrunner;
import de.schuetzmarvin.caspscriptsmod.LookupPLCInformationScriptRunner;
import de.schuetzmarvin.caspvalidatormod.IValidatorCASP;
import de.schuetzmarvin.caspvalidatormod.ValidatorCASP;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    // Ein Scanner Objekt zur Kommunikation mit dem Nutzer.
    private static Scanner user_Input = new Scanner(System.in);
    // Ein String in dem die Nutzereingaben die über user_Input aufgenommen werden abgelegt werden können.
    private static String user_data;
    // Ein boolean der die korrektheit einer Nutzereinageb prüft.
    private static boolean correct_answer = false;
    // Ein Objekt der Klasse Nmap.
    static INmap nmap = new Nmap();
    // Ein Objekt der Klasse Hydra.
    static IHydra hydra = new Hydra();
    // Ein Objekt der Klasse LookupPLCInformationScriptRunner
    static IScriptrunner lupisr = new LookupPLCInformationScriptRunner();
    // Ein Objekt der Klasse ChangePLCSettingsScriptRunner
    static IScriptrunner cpssr = new ChangePLCSettingsScriptRunner();
    // Ein objekt der Klasse Validator
    static IValidatorCASP validator = new ValidatorCASP();
    // Ein boolean zur Überprüfung ob eine neue Tool Chain erstellt werden muss, oder ob die Tool Chain valide ist
    static boolean validation_looper = false;
    // Ein Objekt der Output Klasse
    static IOutput output = new Output();


    // Main Methode des Programms, die dieses startet.
    public static void main(String[] args) throws IOException, InterruptedException, TransformerException, ParserConfigurationException, SAXException {
        Controller controller = new Controller();
        List<String> allPossibleToolsAndScripts = controller.getAllToolsandScripts();
        List<List<String>> tool_chain;

        while(validation_looper == false) {
            tool_chain = startProgramAndBuilToolChain(controller, allPossibleToolsAndScripts);
            if (validator.validateToolChain(tool_chain) == true) {
                System.out.println("Die Tool Chain wurde erfolgreich überprüft und enthält keine Probleme! Die Durchführung startet jetzt...");
                validation_looper = true;
                if(controller.buildAndStartCyberAttackSzenario(tool_chain) == true){
                    output.createReviewForAllOutputs();
                    System.out.println("Das Angriffsszenario wurde erfolgreich durchgeführt! Eine Finale Abschlussdatei im XML Format wurde im Storage abgelegt.\n\n Möchten Sie ein neues Cyberangriffszenario zusammenstellen?(ja/nein)\n");
                    String answer= user_Input.nextLine();
                    if(answer.equals("ja")){
                        validation_looper = false;
                    }
                    if(answer.equals("nein")){
                        System.out.println("Danke und bis zum nächsten mal!");
                        System.exit(0);
                    }

                }

            } else {
                System.out.println("Leider war die eingegebene Tool Chain nicht valide. Bitte fangen Sie nochmal von vorn an.");
            }
        }
    }

    // Methode, die die Tool Chain als Parameter entgegennimmt und anschließend die start(Tool-Methode für jedes einzelne Tool der Chain aufruft.
    private boolean buildAndStartCyberAttackSzenario(List<List<String>> tool_chain) throws IOException, InterruptedException, ParserConfigurationException, TransformerException, SAXException {
        for (int i = 0; i < tool_chain.size(); i++) {
             startTool(tool_chain.get(i));
        }
        return true;
    }

    // Methode, welche eine Liste bestehend aus einem String für das Tool und einem String für die Parameter entgegennimmt und auf Grundlage dessen die entsprechende run-Methode
    // des Tools ausführt.
    private void startTool(List<String> tool_information) throws IOException, InterruptedException, TransformerException, SAXException, ParserConfigurationException {
        String tool_name = tool_information.get(0);
        String tool_parameters = tool_information.get(1);
        switch (tool_name) {
            case "nmap":
                if (tool_parameters == null) {
                            nmap.run();
                }else{
                            nmap.run(tool_parameters);
                }
                break;
            case "hydra":
                if (tool_parameters == null) {
                            hydra.run();
                }else{
                            hydra.run(tool_parameters);

                }
                break;
            case "lpi":
                if (tool_parameters == null) {
                            lupisr.run();
                }else{
                            lupisr.run(tool_parameters);
                }
                break;
            case "cps":
                if (tool_parameters == null) {
                            cpssr.run();
                }else{
                            cpssr.run(tool_parameters);
                }
                break;
        }
    }


    // Methode, die dem Nutzer begrüßt und ihm seine Möglichkeiten bis hin zur Erstellung der Tool Chain aufzeigt.
    private static List<List<String>> startProgramAndBuilToolChain(Controller controller, List<String> allPossibleToolsAndScripts) {
        List<List<String>> tool_chain;
        System.out.println("Cyber Attack Szenario Platform (CASP) \n \n Herzlich Willkommen! \n\n Mit s + ENTER gelangen Sie zum Tool Chain Abteil. \n Mit e + ENTER beenden Sie CASP. \n \n Bitte entscheiden Sie sich für eine Eingabe (s oder e) und bestätigen Sie diese mit ENTER: ");
        user_data = user_Input.nextLine();
        while (correct_answer == false) {
            if (user_data.equals("s") || user_data.equals("e")) {
                if(user_data.equals("s")) {
                    controller.evaluation_first_answer(user_data, allPossibleToolsAndScripts);
                    correct_answer = true;
                }else{
                    System.exit(0);
                }
            } else {
                System.out.println(new Exception("Falsche Eingabe. Bitte erneut versuchen! \n\n Bitte entscheiden Sie sich für eine Eingabe (s oder e) und bestätigen Sie diese mit Enter:"));
                user_data = user_Input.nextLine();
            }
        }
        System.out.println("\nZum Frotfahren bitte ENTER drücken...");
        user_data = user_Input.nextLine();
        System.out.println("\nUm ein ool der Tool Chain hinzuzufügen müssen Sie lediglich die davor geschriebene Nummer eingeben und Ihre Eingabe mit ENTER bestätigen.\nBitte wählen Sie eines der Tools als erstes Tool aus:");
        showAllToolsAndScripts(allPossibleToolsAndScripts);
        tool_chain = controller.buildToolChain();
        System.out.println(tool_chain);
        return tool_chain;
    }

    // Methode, die alle verfügbaren Tools und Skripte an der Kommandozeile für den nutzer auflistet.
    private static void showAllToolsAndScripts(List<String> allPossibleToolsAndScripts) {
        int i =0;
        for (String string: allPossibleToolsAndScripts) {
            System.out.println(i+"."+string);
            i++;
        }
    }

    // Methode, die dem Nutzer ermöglicht die Tool Chain zusammenzustelllen.
    private List<List<String>> buildToolChain() {
        List<List<String>> tool_chain_builder = new ArrayList<>();
        boolean add_checker = true;
        String decide_for_another_tool;

        System.out.println("Bitte geben Sie nun eine der Zahlen ein, um mit der Zusammenstellung der Tool-Chain zu beginnen und bestätigen Sie die Eingabe mit ENTER: ");
        while(add_checker == true){
            String userdata = user_Input.nextLine();
            int length_tool_chain = tool_chain_builder.size();
            tool_chain_builder.add(choseAndConfigureTool(userdata, length_tool_chain));
            System.out.println("Wollen Sie noch weitere Tools der Tool Chain hinzufügen? (ja/nein)");
            decide_for_another_tool = user_Input.nextLine();
            if(decide_for_another_tool.equals("ja")) {
                showAllToolsAndScripts(getAllToolsandScripts());
                System.out.println("Bitte geben Sie nun eine der Zahlen ein, um die zusammenstellung der Tool Chain fortzusetzen und bestätigen Sie die Eingabe mit ENTER: ");
            }
            if(decide_for_another_tool.equals("nein")) {
                System.out.println("Ok, die Tool Chain wird nun überprüft...");
                add_checker = false;
            }
        }

        return tool_chain_builder;
    }

    // Methode, in welcher der Nutzer entscheiden kann welches Tool er wählt und ob er diesem Parameter übergeben möchte oder nicht. Für das erste
    // Tool ist es hierbei Pflicht Parameter zu übergeben.
    private List<String> choseAndConfigureTool(String user_data, int length_tool_chain) {
        List<String> tool_and_configuration = new ArrayList<>();

        switch (user_data) {
            case "0":
                tool_and_configuration.add("nmap");
                if(length_tool_chain == 0) {
                    while(askforconfiguration() == false){
                        System.out.println("Dies ist das erste Tool. Sie müssen eine Eingabe über Parameter vornehmen");
                        askforconfiguration();
                    }
                    tool_and_configuration.add(configureNmap((Nmap) nmap));
                }else{
                    if(nmap.checkIFconfiguration() == true && askforconfiguration() == true) {
                        tool_and_configuration.add(configureNmap((Nmap) nmap));
                    }else{
                        tool_and_configuration.add(null);
                    }
                }
                break;
            case "1":
                tool_and_configuration.add("hydra");
                if(length_tool_chain == 0) {
                    while(askforconfiguration() == false){
                        System.out.println("Dies ist das erste Tool. Sie müssen eine Eingabe über Parameter vornehmen");
                        askforconfiguration();
                    }
                    tool_and_configuration.add(configureHydra((Hydra) hydra));
                }else{
                    if(hydra.checkIFconfiguration() == true && askforconfiguration() == true) {
                        tool_and_configuration.add(configureHydra((Hydra) hydra));
                    }else{
                        tool_and_configuration.add(null);
                    }
                }
                break;
            case "2":
                tool_and_configuration.add("cps");
                if(length_tool_chain == 0) {
                    while(askforconfiguration() == false){
                        System.out.println("Dies ist das erste Tool. Sie müssen eine Eingabe über Parameter vornehmen");
                        askforconfiguration();
                    }
                    tool_and_configuration.add(configureCPS((ChangePLCSettingsScriptRunner) cpssr));
                }else{
                    if(cpssr.checkIFconfiguration() == true && askforconfiguration() == true) {
                        tool_and_configuration.add(configureCPS((ChangePLCSettingsScriptRunner) cpssr));
                    }else{
                        tool_and_configuration.add(null);
                    }
                }
                break;
            case "3":
                tool_and_configuration.add("lpi");
                if(length_tool_chain == 0) {
                    while(askforconfiguration() == false){
                        System.out.println("Dies ist das erste Tool. Sie müssen eine Eingabe über Parameter vornehmen");
                        askforconfiguration();
                    }
                    tool_and_configuration.add(configureLPI((LookupPLCInformationScriptRunner) lupisr));
                }else{
                    if(lupisr.checkIFconfiguration() == true && askforconfiguration() == true) {
                        tool_and_configuration.add(configureLPI((LookupPLCInformationScriptRunner) lupisr));
                    }else{
                        tool_and_configuration.add(null);
                    }
                }
                break;
        }
    return tool_and_configuration;

    }

    //Methode, die sich darüber erkundigt, ob der Nutzer Parameter manuell übergeben möchte oder nicht.
    private boolean askforconfiguration() {
        System.out.println("Wählen Sie eine der beiden Möglichkeiten. (Wenn dieses Tool das erste der Tool Chain ist, dann müssen sie die Parametervariante wählen)\n\n 1.Tool mit Parametern versehen\n2.Tool ohne Parametereingabe nutzen");
        if(user_Input.nextLine().equals("1")){
            return true;
        }
        return false;
    }

    // Methode, die dem Nutzer jeden benötigten Parameter für das lookup_plc_information.py Skript aufzeigt und eine eingabe von diesem erwartet.
    private String configureLPI(LookupPLCInformationScriptRunner lupisr) {
        String parameters="";

        for (ValuesEnum value: lupisr.getProvider().getNeededValuesManual()) {
            System.out.println("lookup_plc_information.py braucht: " + value + ". Bitte geben Sie den notwendigen Parameter richtig ein und bestätigen Sie mit ENTER:");
            parameters = parameters + user_Input.nextLine() + " ";

        }
        return parameters;
    }

    // Methode, die dem Nutzer jeden benötigten Parameter für das change_plc_settings.py Skript aufzeigt und eine eingabe von diesem erwartet.
    private String configureCPS(ChangePLCSettingsScriptRunner cpssr) {
        String parameters="";

        for (ValuesEnum value: cpssr.getProvider().getNeededValuesManual()) {
            System.out.println("change_plc_settings.py braucht: " + value + ". Bitte geben Sie den notwendigen Parameter richtig ein und bestätigen Sie mit ENTER:");
            parameters = parameters + user_Input.nextLine()+ " ";

        }
        return parameters;
    }

    // Methode, die dem Nutzer jeden benötigten Parameter für das Tool Hydra aufzeigt und eine eingabe von diesem erwartet.
    private String configureHydra(Hydra hydra) {
        String parameters="";

        for (ValuesEnum value: hydra.getProvider().getNeededValuesManual()) {
            System.out.println("Hydra braucht: " + value + ". Bitte geben Sie den notwendigen Parameter richtig ein und bestätigen Sie mit ENTER:");
            parameters = parameters + user_Input.nextLine()+ " ";

        }
        return parameters;
    }

    // Methode, die dem Nutzer jeden benötigten Parameter für das Tool Nmap aufzeigt und eine eingabe von diesem erwartet.
    private String configureNmap(Nmap nmap) {
        String parameters="";

        for (ValuesEnum value: nmap.getProvider().getNeededValuesManual()) {
            System.out.println("Nmap braucht: " + value + ". Bitte geben Sie den notwendigen Parameter richtig ein und bestätigen Sie mit ENTER:");
            parameters = parameters + user_Input.nextLine()+ " ";

        }
        return parameters;
    }

    // Methode, die feststellt, ob der Nutzer mit der Erstellung der Tool Chain beginnen, oder das Programm beenden möchte.
    private void evaluation_first_answer(String user_answer, List<String> listAllTandS) {

        switch (user_answer) {
            case "s":
                System.out.print("Für den Zusammenbau des Cyberangriffszenarios stehen folgende Tools zur Verfügung: \n\nTools\n________\n" + listAllTandS.get(0) + "\n" + listAllTandS.get(1) + " \n\n\nSkripte\n_______\n");
                for (int i = 2; i < listAllTandS.size() ; i++) {
                    System.out.println(listAllTandS.get(i));
                }
                break;
            case "e":
                System.out.println("CASP wird beendet. Bis zum nächsten Mal!");
                System.exit(0);
                break;
        }

    }

    // Methode, die alle Namen der Dateien in CASPStoragescripts/ aufführt und die daraus entstehende Liste zurückgibt.
    protected List<String> getScripts() {
        File directory = new File("CASPStorage\\scripts\\");
        File[] listOfFiles = directory.listFiles();
        List<String> mainList = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                mainList.add(listOfFiles[i].getName());
            }
        }
        return mainList;
    }

    // Methode, in welcher alle Tools, die zur Verfügung stehen gesammelt und zurückgegeben werden.
    protected List<String> getAllToolsandScripts(){
        List<String> scriptFileNames = getScripts();
        List<String> tools = List.of(nmap.TOOL_NAME,hydra.TOOL_NAME);
        List<String> alltoolsandscripts = new ArrayList<>();

        for (String string: tools) {
            alltoolsandscripts.add(string);
        }
        for (String string: scriptFileNames) {
            alltoolsandscripts.add(string);
        }

        return alltoolsandscripts;
    }

    }
