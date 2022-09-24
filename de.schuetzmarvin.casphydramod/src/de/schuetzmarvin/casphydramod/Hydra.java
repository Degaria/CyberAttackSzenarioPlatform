package de.schuetzmarvin.casphydramod;

import de.schuetzmarvin.caspconvertermod.*;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderHydra;

import java.io.File;
import java.io.IOException;

public class Hydra implements IHydra{
    public final String TOOL_NAME = "Hydra";
    public static void main(String[] args) throws IOException, InterruptedException {

        Hydra h = new Hydra();
        h.run("192.168.1.2", "bla","21", "admin");
    }

    public boolean run(String ip_address, String dictionary, String port, String username) throws IOException, InterruptedException {
        if(!new File("tool_outputs\\hydra_output.json").exists()){
            new File("tool_outputs\\hydra_output.json").createNewFile(); //TODO: Machs schöner!!!
        }
        Thread.sleep(10000);
        IProvider provider = new ProviderHydra();
        IConverter converter = new ConverterAdapterHydra();
        String hydra_command = "hydra -o " + provider.getFilePath(new File("tool_outputs\\hydra_output.json")) + " -b json -s " + port + " -l " + username + " -P " + provider.getFilePath(new File("documents\\dictionary.txt")) + " " + ip_address + " ftp";
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Program Files (x86)\\thc-hydra-windows-master\\ & start cmd.exe /k " + hydra_command;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(20000);
        converter.toXmlfile(provider.getFilePath(new File("tool_outputs\\hydra_output.json")));
        return true;
    }

}
