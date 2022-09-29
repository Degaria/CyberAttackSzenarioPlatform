package de.schuetzmarvin.caspprovidermod;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


    // Provider Klasse für den CASPStorage. Sie stellt eine Reihe von Methoden Implementationen zur Verfügung, die für die Verwaltung des Storage nötig sind.
public class ProviderStorage implements IProvider{

    // gibt die Werte zurück, die zur Ausführung des Tools in der parameterfreien run-Methode zur Verfügung stehen müssen, um erfolreich ausgeführt werden zu können (hier nicht benötigt, aber aufgrund de interfaces mit aufgenommen).
    @Override
    public List<ValuesEnum> getNeededValuesAutomatic() {
        return null;
    }

    // gibt die Werte zurück, die zur Ausführung des Tools in der parametrierten run-Methode zur Verfügung stehen müssen, um erfolreich ausgeführt werden zu können (hier nicht benötigt, aber aufgrund de interfaces mit aufgenommen).
    @Override
    public List<ValuesEnum> getNeededValuesManual() {
        return null;
    }

    // gibt die Werte zurück, die nach der Ausführung des Tools, durch dessen output zur Verfügung gestellt werden (hier nicht benötigt, aber aufgrund de interfaces mit aufgenommen).
    @Override
    public List<ValuesEnum> getProvidedValues() {
        return null;
    }

    // gibt den absoluten Pfad einer Datei zurück.
    @Override
    public String getFilePath(File file) {
        return file.getAbsolutePath();
    }

    // speichert/schreibt den übergebenen Wert in der übergebenen Datei.
    @Override
    public void saveFile(String value, String filename) throws IOException {
        File file = new File(filename);
        if(file.exists() == false) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(getFilePath(file),false);
        writer.write(value);
        writer.close();

    }

    // gibt für eine Datei zurück, ob sie eine XML-Datei ist oder nicht.
    @Override
    public boolean isXml(String path_to_file) {
        String extension = FilenameUtils.getExtension(path_to_file);
        String xml_extension = "xml";
        if(extension.equals(xml_extension)) {
            return true;
        }
        return false;
    }

    // Methode, die auf vorhandene Informationsquellen vorheriger Tools zurückgreift (CASP/Storage/parameteriles) und aus diesen die benötigten informationen zurückgibt (hier nicht benötigt, aber aufgrund de interfaces mit aufgenommen).
    @Override
    public ArrayList<String> getParametersforExecution() throws ParserConfigurationException, IOException, SAXException {
        return null;
    }

    // Methode, welche im Falle das mehrere verschiedene Parameter für die Ausführung einer Funktion benötigt werden diese zurückgibt (hier nicht benötigt, aber aufgrund de interfaces mit aufgenommen).
    @Override
    public ArrayList<ArrayList<String>> getParametersforExecutionmultipleValues() throws ParserConfigurationException, IOException, SAXException {
        return null;
    }
}
