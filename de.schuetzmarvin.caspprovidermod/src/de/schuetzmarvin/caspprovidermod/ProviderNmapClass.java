package de.schuetzmarvin.caspprovidermod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProviderNmapClass implements IProvider{

    public boolean check_if_is_done(String file) throws IOException {
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

    @Override
    public ArrayList<String> get_ip_address(String file) {
        return null;
    }
}
