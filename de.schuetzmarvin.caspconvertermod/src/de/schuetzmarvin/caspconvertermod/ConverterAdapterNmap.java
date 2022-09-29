package de.schuetzmarvin.caspconvertermod;

import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderStorage;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;

    /*
        Diese Klasse ist dafür verantwortlich den Output des Hydra Tools in ein XML Format zu überführen.
        Außerdem ist Sie dafür zuständig unter Verwendung der XSL (nmap_output_template.xsl) und der Output-XML (nmap_output.xml) die XML-Datei (nmap_parameter_output.xml)
        zu erstellen, die den folgenden Tools als Informationsquelle dient.
    */
public class ConverterAdapterNmap implements IConverter {
    private IProvider provider = new ProviderStorage();

    /*
       Methode zur Umformung der Output-Datei in das XML-Format. Da der Output des Nmap-Tools jedoch direkt als XML-Datei ausgegeben wird, ist eine Umformung in dieses Format
       nicht notwendig. Es wird lefiglich aus einheitlichen Gründen eine Überprüfung übernommen, um sicherzustellen, dass es sich um eine XML-Datei handelt.
       Des Weiteren übergibt sie die nmap_output.xml and die Methode changeTagName(File file).
     */
    @Override
    public void toXmlfile(String file_path) throws IOException, TransformerException {
        if(provider.isXml(file_path)){
            changeTagName(new File(file_path));
        }
    }

    /*
      Die Methode ist dafür verantwortlich die überarbeiteten XML-Output-Dateien zu ertstellen, die den Tools als Informationsquelle dienen. Ihr wird die XML-Output-Datei
      übergeben. Mit dieser und der zum Tool gehörigen XSL-Datei (nmap_output_template.xsl) erstellt es eine neue Datei (nmap_parameter_output.xml),
      in welche der transformierte XML-Stream übertragen wird.
   */
    @Override
    public void changeTagName(File file) throws IOException, TransformerException {
        File XSLFILE = new File("CASPStorage\\xslTemplates\\nmap_output_template.xsl");
        File XMLFILE = file;
        File OUTPUT = new File("CASPStorage\\parameterFiles\\nmap_parameter_output.xml");

        if(OUTPUT.exists() == false) {
            OUTPUT.createNewFile();
        }

        StreamSource xslcode = new StreamSource(XSLFILE);
        StreamSource inputfile = new StreamSource(XMLFILE);
        StreamResult outputfile = new StreamResult(OUTPUT);

        TransformerFactory transformer_factory = TransformerFactory.newInstance();

        Transformer transformer = transformer_factory.newTransformer(xslcode);

        transformer.transform(inputfile,outputfile);

    }

}


