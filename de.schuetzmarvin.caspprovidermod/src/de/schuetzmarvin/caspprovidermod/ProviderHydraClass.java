package de.schuetzmarvin.caspprovidermod;

import java.io.IOException;
import java.util.ArrayList;

public class ProviderHydraClass implements IProvider {
    @Override
    public boolean check_if_is_done(String file) throws IOException {
        return false;
    }

    @Override
    public boolean is_xml(String path_to_file) {
        return false;
    }

    @Override
    public ArrayList<String> get_ip_address(String file) {
        return null;
    }
}
