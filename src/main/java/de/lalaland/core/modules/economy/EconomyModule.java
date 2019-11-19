package de.lalaland.core.modules.economy;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;

public class EconomyModule implements IModule {

    private CorePlugin corePlugin;

    public EconomyModule(CorePlugin corePlugin){
        this.corePlugin = corePlugin;
    }

    @Override
    public void enable(CorePlugin plugin) throws Exception {

    }

    @Override
    public void disable(CorePlugin plugin) throws Exception {

    }

    @Override
    public String getModuleName() {
        return "EconomyModule";
    }
}
