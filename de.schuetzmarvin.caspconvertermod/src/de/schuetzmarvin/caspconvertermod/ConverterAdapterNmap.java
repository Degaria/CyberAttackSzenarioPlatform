package de.schuetzmarvin.caspconvertermod;

import de.schuetzmarvin.caspprovidermod.ProviderStorage;


public class ConverterAdapterNmap implements IConverter {
    private ProviderStorage provider = new ProviderStorage();

    @Override
    public void toXmlfile(String file_path) {
        if(provider.isXml(file_path)){
            return;
        }
    }
}
