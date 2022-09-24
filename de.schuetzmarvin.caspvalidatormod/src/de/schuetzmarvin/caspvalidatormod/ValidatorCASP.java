package de.schuetzmarvin.caspvalidatormod;

import de.schuetzmarvin.caspprovidermod.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidatorCASP implements IValidatorCASP{
    IProvider provider_nmap = new ProviderNmap();
    IProvider provider_hydra = new ProviderHydra();
    IProvider provider_lpi = new Provider_LookupPLCInformation_Script();
    IProvider provider_cps = new Provider_ChangePLCSettings_Script();
    boolean answer=true;
    Set<ValuesEnum> checkprovided = new HashSet<>();

    @Override
    public boolean validateToolChain(List<List<String>> tool_chain) {
        List<Boolean> tool_chain_check = new ArrayList<>();
        for (int i = 0; i < tool_chain.size(); i++) {
            if (tool_chain.get(i).get(0).equals("nmap")) {
                boolean valid_nmap = validate(tool_chain.get(i), provider_nmap);
                tool_chain_check.add(valid_nmap);
                System.out.println("Provided: " +checkprovided);
            }
            if (tool_chain.get(i).get(0).equals("hydra")) {
                boolean valid_hydra = validate(tool_chain.get(i), provider_hydra);
                tool_chain_check.add(valid_hydra);
                System.out.println("Provided: " +checkprovided);
            }
            if (tool_chain.get(i).get(0).equals("cps")) {
                boolean valid_cps = validate(tool_chain.get(i), provider_cps);
                tool_chain_check.add(valid_cps);
                System.out.println("Provided: " +checkprovided);
            }
            if (tool_chain.get(i).get(0).equals("lpi")) {
                boolean valid_lpi = validate(tool_chain.get(i), provider_lpi);
                tool_chain_check.add(valid_lpi);
                System.out.println("Provided: " +checkprovided);
            }
        }
        System.out.println(tool_chain_check);
        if (tool_chain_check.contains(false) == false) {
            return true;
        }
        return false;
    }

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

    private boolean checkNowProvidedValues(List<ValuesEnum> neededValues) {
        String needed_string = "";
        for (ValuesEnum needed: neededValues) {
            needed_string = needed_string + needed;
            if(checkprovided.contains(needed) == false){
                return false;
            }
        }
        System.out.println("Needed: " +needed_string);
        return true;
    }
}
