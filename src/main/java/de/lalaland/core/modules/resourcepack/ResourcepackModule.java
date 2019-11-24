package de.lalaland.core.modules.resourcepack;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.resourcepack.packing.ResourcepackZipper;

public class ResourcepackModule implements IModule {

  @Override
  public String getModuleName() {
    return "ResourcepackModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception, Exception {
    new ResourcepackZipper(plugin).zipResourcepack();
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }
}
