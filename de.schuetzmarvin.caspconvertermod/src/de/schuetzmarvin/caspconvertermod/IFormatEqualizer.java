package de.schuetzmarvin.caspconvertermod;


import java.io.IOException;

public interface IFormatEqualizer {

    void json_to_xml(String jsonText) throws IOException;
    boolean is_xml(String path_to_file);
    String get_file_extension(String path_to_file);
}
