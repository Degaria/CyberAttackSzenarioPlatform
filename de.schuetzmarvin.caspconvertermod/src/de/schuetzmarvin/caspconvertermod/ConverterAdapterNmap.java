package de.schuetzmarvin.caspconvertermod;

import de.schuetzmarvin.caspprovidermod.ProviderStorage;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;


public class ConverterAdapterNmap implements IConverter {
    private ProviderStorage provider = new ProviderStorage();
    public static void main(String[] args) throws IOException, TransformerException {
         ProviderStorage provider_main = new ProviderStorage();
        ConverterAdapterNmap converter = new ConverterAdapterNmap();
        converter.toXmlfile(provider_main.getFilePath(new File("tool_outputs\\TEST_TAGS.xml")));
    }


    @Override
    public void toXmlfile(String file_path) throws IOException, TransformerException {
        System.out.println(provider.isXml(file_path));
        if(provider.isXml(file_path)){
            changeTagName(new File(file_path));
            return;
        }
    }

    @Override
    public void changeTagName(File file) throws IOException, TransformerException {
        File XSLFILE = new File("xslTemplates\\nmap_output_template.xsl");
        File XMLFILE = file;
        File OUTPUT = new File("parameterFiles\\nmap_parameter_output.xml");

        //File XSLFILE = new File("tool_outputs/TEST_TAG_XSL.xsl");
        //File XMLFILE = file;
        //File OUTPUT = new File("tool_outputs/TEST_TAG_OUTPUT.xml");
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


