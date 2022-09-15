package de.schuetzmarvin.caspmainmod;


import java.util.Scanner;

public class Controller {
    private static Scanner user_Input = new Scanner(System.in);
    private static String user_data;
    private static boolean correct_answer = false;

    public static void main(String[] args) {
        System.out.println("Cyber Attack Szenario Platform (CASP) \n \n Herzlich Willkommen! \n\n Mit i + ENTER erhalten Sie genauere Informationen zur Nutzung. \n Mit s + ENTER gelangen Sie zum Tool Chain Abteil. \n Mit e + ENTER beenden Sie CASP. \n \n Bitte entscheiden Sie sich für eine Eingabe (s oder e) und bestätigen Sie diese mit ENTER: ");
        user_data = user_Input.nextLine();
        while(correct_answer == false)
        if(user_data.equals("s") || user_data.equals("e")) {
            evaluation_first_answer(user_data);
            correct_answer = true;
        }else{
           System.out.println(new Exception("Falsche Eingabe. Bitte erneut versuchen! \n\n Bitte entscheiden Sie sich für eine Eingabe (s oder e) und bestätigen Sie diese mit Enter:"));
            user_data = user_Input.nextLine();
        }
    }

    private static void evaluation_first_answer(String user_answer){
        switch (user_answer){
            case "s" :
                System.out.print("Für den Zusammenbau des Cyberangriffszenarios stehen folgende Tools zur Verfügung: \n\n Tools\n ________ \n nmap \n hydra \n\n Skripte\n _______ \n ...");
                break;
            case "e":
                System.out.println("CASP wird beendet. Bis zum nächsten Mal!");
                System.exit(0);
                break;
        }
    }
}
