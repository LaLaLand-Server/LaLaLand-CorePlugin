package de.lalaland.core.modules.mobs.implementations;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.combat.stats.CombatStatManager;
import de.lalaland.core.modules.mobs.custommobs.CustomMobManager;
import de.lalaland.core.tasks.TaskManager;
import de.lalaland.core.utils.UtilModule;
import de.lalaland.core.utils.common.UtilMath;
import de.lalaland.core.utils.holograms.infobars.AbstractInfoBar;
import de.lalaland.core.utils.holograms.infobars.InfoBarManager;
import de.lalaland.core.utils.holograms.infobars.InfoLineSpacing;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MobManager {

  public MobManager(final CombatStatManager combatStatManager, final CustomMobManager customMobManager, final CorePlugin plugin) {
    this.combatStatManager = combatStatManager;
    this.customMobManager = customMobManager;
    Bukkit.getPluginManager().registerEvents(new MobListener(), plugin);
    infoBarManager = plugin.getModule(UtilModule.class).getInfoBarManager();
    taskManager = plugin.getTaskManager();
  }

  private final TaskManager taskManager;
  private final CustomMobManager customMobManager;
  private final CombatStatManager combatStatManager;
  private final InfoBarManager infoBarManager;
  // TODO Skill linker

  public LivingEntity createMob(final GameMobType type, final int level, final Location location) {
    final GameMob mob = type.getGameMobFunction().apply(level);
    final LivingEntity bukkitEntity = mob.spawn(location, customMobManager);
    final CombatStatHolder holder = combatStatManager.initEntity(bukkitEntity);
    mob.initCombatStats(holder);
    holder.recalculate();
    bukkitEntity.setHealth(bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    bukkitEntity.getScoreboardTags().add("TOKEN_HOLDER");

    if (!type.isTokenMounted()) {
      final ArmorStand token = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
      token.setVisible(false);
      token.setGravity(false);
      token.setMarker(true);
      bukkitEntity.addPassenger(token);
    }

    final AbstractInfoBar infoBar = infoBarManager.createInfoBar(bukkitEntity.getPassengers().get(0));
    infoBar.addLine("" + UtilMath.getHPBar(100, 100), mob.getBottomLineSpacing());
    infoBar.addLine(mob.getName() + " §e[§f" + mob.level + "§e]", InfoLineSpacing.MEDIUM);
    infoBar.addLine("", InfoLineSpacing.MEDIUM);

    return bukkitEntity;
  }

}
