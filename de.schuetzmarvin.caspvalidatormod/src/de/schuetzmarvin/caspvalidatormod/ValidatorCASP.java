package de.schuetzmarvin.caspvalidatormod;

import de.schuetzmarvin.caspprovidermod.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


    // Klasse zur Validierung der Tool Chain.
public class ValidatorCASP implements IValidatorCASP{

    //Initialisierung und Deklaration eines ProviderNmap-Objekts.
    IProvider provider_nmap = new ProviderNmap();
    //Initialisierung und Deklaration eines ProviderHydra-Objekts.
    IProvider provider_hydra = new ProviderHydra();
    //Initialisierung und Deklaration eines ProviderLookupPLCInformationScriptRunner-Objekts.
    IProvider provider_lpi = new Provider_LookupPLCInformation_Script();
    //Initialisierung und Deklaration eines ProviderChangePLCSettingsScriptRunner-Objekts.
    IProvider provider_cps = new Provider_ChangePLCSettings_Script();
    //boolean für die Antwort an den Controller, ob die Tool Chain valide ist oder nicht
    boolean answer=true;
    //Set das die von Tools gelieferten Werte beinhaltet (ProvidedValues)
    Set<ValuesEnum> checkprovided = new HashSet<>();

    // Methode, die für jedes Element der Tool Chain prüft, ob alle notwendigen Informationen zum Zeitpunkt des Einsatzes vorliegen und entsprechend
    // true oder false in eine Boolean-List hinzufügt. Sofern nach der Überprüfung alle tools der Tool Chain ein False in diser liste auftaucht,
    // gilt die Tool Chain als nicht valide und es wird false zurückgegeben, andernfalls true.
    @Override
    public boolean validateToolChain(List<List<String>> tool_chain) {
        List<Boolean> tool_chain_check = new ArrayList<>();
        for (int i = 0; i < tool_chain.size(); i++) {
            if (tool_chain.get(i).get(0).equals("nmap")) {
                boolean valid_nmap = validate(tool_chain.get(i), provider_nmap);
                tool_chain_check.add(valid_nmap);
                //System.out.println("Provided: " +checkprovided);
            }
            if (tool_chain.get(i).get(0).equals("hydra")) {
                boolean valid_hydra = validate(tool_chain.get(i), provider_hydra);
                tool_chain_check.add(valid_hydra);
                //System.out.println("Provided: " +checkprovided);
            }
            if (tool_chain.get(i).get(0).equals("cps")) {
                boolean valid_cps = validate(tool_chain.get(i), provider_cps);
                tool_chain_check.add(valid_cps);
                //System.out.println("Provided: " +checkprovided);
            }
            if (tool_chain.get(i).get(0).equals("lpi")) {
                boolean valid_lpi = validate(tool_chain.get(i), provider_lpi);
                tool_chain_check.add(valid_lpi);
                //System.out.println("Provided: " +checkprovided);
            }
        }
        //System.out.println(tool_chain_check);
        if (tool_chain_check.contains(false) == false) {
            return true;
        }
        return false;
    }

    // Wenn das Tool ohne Parameterübergabe durch den Nutzer verwendet werden soll, wird überprüft, ob die momentan verfügbaren Informationen
    // durch vorherige Tools gedeckt werden können.
    // Wenn das Tool nach seiner Durchführung Werte zur Verfügung stellt, werden diese dem Set hinzugefügt.
    // Sofern das Tool alle benötigten Parameter beziehen kann wird true zurückgegeben, andernfalls false.
    private boolean validate(List<String> information, IProvider provider) {
        if(information.get(1) == null){
            answer = checkNowProvidedValues(provider.getNeededValuesAutomatic());
        }
        if(provider.getProvidedValues() != null) {
            for (ValuesEnum value : provider.getProvidedValues()) {
                checkprovided.add(value);
            }
        }
        if(answer == false){
            return false;
        }
        return true;
    }

    // übeprüft, ob auch alle benötigten Werte zum derzeitigen Zeitpunkt vorhanden sind.
    private boolean checkNowProvidedValues(List<ValuesEnum> neededValues) {
        String needed_string = "";
        for (ValuesEnum needed: neededValues) {
            needed_string = needed_string + needed;
            if(checkprovided.contains(needed) == false){
                return false;
            }
        }
        return true;
    }
}
