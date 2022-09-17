package de.schuetzmarvin.caspnmapmod;

import java.io.IOException;

public interface INmap {

    boolean run(String ip_adressen) throws IOException, InterruptedException;
}
