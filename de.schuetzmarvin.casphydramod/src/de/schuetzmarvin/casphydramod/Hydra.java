package de.schuetzmarvin.casphydramod;

import de.schuetzmarvin.caspconvertermod.*;
import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderHydra;

import java.io.File;
import java.io.IOException;

public class Hydra implements IHydra{
    public final String TOOL_NAME;
    IProvider provider;
    IConverter converter;

    public Hydra(){
        this.TOOL_NAME = "Hydra";
        this.provider = new ProviderHydra();
        this.converter  = new ConverterAdapterHydra();
    }

    public boolean run(String ip_address, String dictionary, String port, String username) throws IOException, InterruptedException {
        if(!new File("tool_outputs\\hydra_output.json").exists()){
            new File("tool_outputs\\hydra_output.json").createNewFile(); //TODO: Machs schöner!!!
        }
        Thread.sleep(10000);

        String hydra_command = "hydra -o " + this.provider.getFilePath(new File("tool_outputs\\hydra_output.json")) + " -b json -s " + port + " -l " + username + " -P " + this.provider.getFilePath(new File("documents\\dictionary.txt")) + " " + ip_address + " ftp";
        String command_exec_cmd =  "cmd.exe /c cd  C:\\Program Files (x86)\\thc-hydra-windows-master\\ & start cmd.exe /k " + hydra_command;
        Runtime.getRuntime().exec(command_exec_cmd);
        Thread.sleep(20000);
        this.converter.toXmlfile(this.provider.getFilePath(new File("tool_outputs\\hydra_output.json")));
        return true;
    }

    public IProvider getProvider(){
        return this.provider;
    }

    public boolean checkIFconfiguration() {
        if(this.getProvider().getNeededValuesManual().isEmpty()){
            return false;
        }

        return true;
    }

}
