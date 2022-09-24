package de.schuetzmarvin.caspmainmod;


import de.schuetzmarvin.casphydramod.Hydra;
import de.schuetzmarvin.caspnmapmod.Nmap;
import de.schuetzmarvin.caspprovidermod.ValuesEnum;
import de.schuetzmarvin.caspscriptsmod.ChangePLCSettingsScriptRunner;
import de.schuetzmarvin.caspscriptsmod.LookupPLCInformationScriptRunner;
import de.schuetzmarvin.caspvalidatormod.ValidatorCASP;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private static Scanner user_Input = new Scanner(System.in);
    private static String user_data;
    private static boolean correct_answer = false;
    static final Object lock2 = new Object();
    static boolean ready_lock_2;
    static final Object lock3 = new Object();
    static boolean ready_lock_3;
    static final Object lock4 = new Object();
    static boolean ready_lock_4;
    static final Object lock5 = new Object();
    static boolean ready_lock_5;
    static Nmap nmap = new Nmap();
    static Hydra hydra = new Hydra();
    static LookupPLCInformationScriptRunner lupisr = new LookupPLCInformationScriptRunner();
    static ChangePLCSettingsScriptRunner cpssr = new ChangePLCSettingsScriptRunner();
    static ValidatorCASP validator = new ValidatorCASP();
    static boolean validation_looper = false;


    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        Controller controller = new Controller();
        List<String> allPossibleToolsAndScripts = controller.getAllToolsandScripts();
        List<List<String>> tool_chain;

        while(validation_looper == false) {
            tool_chain = startProgramAndBuilToolChain(controller, allPossibleToolsAndScripts);
            if (validator.validateToolChain(tool_chain) == true) {
                System.out.println("Die Tool Chain wurde erfolgreich überprüft und enthält keine Probleme! Die Durchführung startet jetzt...");
                validation_looper = true;
            } else {
                System.out.println("Leider war die eingegebene Tool Chain nicht valide. Bitte fangen Sie nochmal von vorn an.");
            }
        }
    }

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

    private static void showAllToolsAndScripts(List<String> allPossibleToolsAndScripts) {
        int i =0;
        for (String string: allPossibleToolsAndScripts) {
            System.out.println(i+"."+string);
            i++;
        }
    }

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
                    tool_and_configuration.add(configureNmap(nmap));
                }else{
                    if(nmap.checkIFconfiguration() == true && askforconfiguration() == true) {
                        tool_and_configuration.add(configureNmap(nmap));
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
                    tool_and_configuration.add(configureHydra(hydra));
                }else{
                    if(hydra.checkIFconfiguration() == true && askforconfiguration() == true) {
                        tool_and_configuration.add(configureHydra(hydra));
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
                    tool_and_configuration.add(configureCPS(cpssr));
                }else{
                    if(cpssr.checkIFconfiguration() == true && askforconfiguration() == true) {
                        tool_and_configuration.add(configureCPS(cpssr));
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
                    tool_and_configuration.add(configureLPI(lupisr));
                }else{
                    if(lupisr.checkIFconfiguration() == true && askforconfiguration() == true) {
                        tool_and_configuration.add(configureLPI(lupisr));
                    }else{
                        tool_and_configuration.add(null);
                    }
                }
                break;
        }
    return tool_and_configuration;

    }

    private boolean askforconfiguration() {
        System.out.println("Wählen Sie eine der beiden Möglichkeiten. (Wenn dieses Tool das erste der Tool Chain ist, dann müssen sie die Parametervariante wählen)\n\n 1.Tool mit Parametern versehen\n2.Tool ohne Parametereingabe nutzen");
        if(user_Input.nextLine().equals("1")){
            return true;
        }
        return false;
    }

    private String configureLPI(LookupPLCInformationScriptRunner lupisr) {
        String parameters="";

        for (ValuesEnum value: lupisr.getProvider().getNeededValuesManual()) {
            System.out.println("lookup_plc_information.py braucht: " + value + ". Bitte geben Sie den notwendigen Parameter richtig ein und bestätigen Sie mit ENTER:");
            parameters = parameters + user_Input.nextLine() + " ";

        }
        return parameters;
    }

    private String configureCPS(ChangePLCSettingsScriptRunner cpssr) {
        String parameters="";

        for (ValuesEnum value: cpssr.getProvider().getNeededValuesManual()) {
            System.out.println("change_plc_settings.py braucht: " + value + ". Bitte geben Sie den notwendigen Parameter richtig ein und bestätigen Sie mit ENTER:");
            parameters = parameters + user_Input.nextLine()+ " ";

        }
        return parameters;
    }

    private String configureHydra(Hydra hydra) {
        String parameters="";

        for (ValuesEnum value: hydra.getProvider().getNeededValuesManual()) {
            System.out.println("Hydra braucht: " + value + ". Bitte geben Sie den notwendigen Parameter richtig ein und bestätigen Sie mit ENTER:");
            parameters = parameters + user_Input.nextLine()+ " ";

        }
        return parameters;
    }

    private String configureNmap(Nmap nmap) {
        String parameters="";

        for (ValuesEnum value: nmap.getProvider().getNeededValuesManual()) {
            System.out.println("Nmap braucht: " + value + ". Bitte geben Sie den notwendigen Parameter richtig ein und bestätigen Sie mit ENTER:");
            parameters = parameters + user_Input.nextLine()+ " ";

        }
        return parameters;
    }

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

    protected List<String> getScripts() {
        File directory = new File("scripts\\");
        File[] listOfFiles = directory.listFiles();
        List<String> mainList = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                mainList.add(listOfFiles[i].getName());
            }
        }
        return mainList;
    }

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


       /*

        Thread thread1 = new Thread(() -> {
            try {
                nmap.run("192.168.1.1-5");
                synchronized (lock2) {
                    ready_lock_2 = true;
                    lock2.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                while (!ready_lock_2) {
                    Thread.sleep(1000);
                }
                //lupisr.run("lookup_plc_information.py","192.168.1.1 192.168.1.2 192.168.1.3 192.168.1.4");
                lupisr.run("lookup_plc_information.py", null);
                synchronized (lock3) {
                    ready_lock_3 = true;
                    lock3.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                while (!ready_lock_3) {
                    Thread.sleep(1000);
                }
                hydra.run("192.168.1.2", "bla", "21", "admin");
                synchronized (lock4) {
                    ready_lock_4 = true;
                    lock4.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread4 = new Thread(() -> {
            try {
                while (!ready_lock_4) {
                    Thread.sleep(1000);
                }
                cpssr.run("change_plc_settings.py", "192.168.1.30");
                synchronized (lock5) {
                    ready_lock_5 = true;
                    lock5.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        });

        Thread thread5 = new Thread(() -> {
            try {
                while(!ready_lock_5){
                    Thread.sleep(1000);
                }
                cpssr.run("change_plc_settings.py", "192.168.1.2");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();


        */

        //nmap.run("192.168.1.1-5");
        //lupisr.run("lookup_plc_information.py",null);
        //lupisr.run("lookup_plc_information.py","192.168.1.1 192.168.1.2 192.168.1.3 192.168.1.4");
        //hydra.run("192.168.1.2", "bla","21", "admin");
        //cpssr.run("change_plc_settings.py","192.168.1.30");

    }
