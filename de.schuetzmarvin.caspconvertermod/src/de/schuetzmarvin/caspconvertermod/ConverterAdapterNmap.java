package de.schuetzmarvin.caspconvertermod;

import de.schuetzmarvin.caspprovidermod.IProvider;
import de.schuetzmarvin.caspprovidermod.ProviderStorage;


public class ConverterAdapterNmap implements IConverter {
    private IProvider provider = new ProviderStorage();

    @Override
    public void toXmlfile(String file_path) {
        if(provider.isXml(file_path)){
            return;
        }
    }
}
