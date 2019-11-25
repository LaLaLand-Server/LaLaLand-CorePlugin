package de.lalaland.core.modules.resourcepack;

import com.google.common.collect.ImmutableList;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.resourcepack.distribution.ResourcepackListener;
import de.lalaland.core.modules.resourcepack.distribution.ResourcepackManager;
import de.lalaland.core.modules.resourcepack.packing.ResourcepackZipper;
import de.lalaland.core.modules.resourcepack.skins.ModelItem;
import de.lalaland.core.modules.resourcepack.skins.ModelItemCommand;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;

public class ResourcepackModule implements IModule {

  private ResourcepackManager resourcepackManager;

  @Override
  public String getModuleName() {
    return "ResourcepackModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception, Exception {
    new ResourcepackZipper(plugin).zipResourcepack();
    plugin.getCommandManager().registerCommand(new ModelItemCommand());
    plugin.getCommandManager()
        .getCommandCompletions()
        .registerStaticCompletion("ModelItem",
            ImmutableList.copyOf(Arrays.stream(ModelItem.values()).map(Enum::toString).collect(Collectors.toList())));
    resourcepackManager = new ResourcepackManager(plugin);
    Bukkit.getPluginManager().registerEvents(new ResourcepackListener(plugin, resourcepackManager), plugin);
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {
    resourcepackManager.shutdown();
  }
}
