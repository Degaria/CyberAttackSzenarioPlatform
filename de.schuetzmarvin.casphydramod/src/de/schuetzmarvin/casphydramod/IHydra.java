package de.schuetzmarvin.casphydramod;

import java.io.IOException;

public interface IHydra {
    boolean run(String ip_address, String dictionary, String port, String username) throws IOException, InterruptedException;
}
