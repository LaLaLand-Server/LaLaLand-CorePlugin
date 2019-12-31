package de.lalaland.core.modules.combat.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import de.lalaland.core.modules.combat.stats.buffs.Buff;
import de.lalaland.core.modules.combat.stats.buffs.BuffEnvironment;
import de.lalaland.core.modules.combat.stats.buffs.BuffType;
import de.lalaland.core.modules.combat.stats.buffs.CombatBuff;
import de.lalaland.core.modules.combat.stats.buffs.StatBuff;
import de.lalaland.core.modules.jobs.JobModule;
import de.lalaland.core.modules.skills.holder.SkillHolder;
import de.lalaland.core.modules.skills.skillimpl.SkillTrigger;
import de.lalaland.core.modules.skills.skillimpl.SkillType;
import de.lalaland.core.utils.Message;
import de.lalaland.core.utils.common.UtilPlayer;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CombatStatHolder implements SkillHolder {

  protected CombatStatHolder(final LivingEntity bukkitEntity, CombatStatManager combatStatManager) {
    maxExp = combatStatManager.getMinExpOfLevel(CombatStatManager.LEVEL_HARD_CAP);
    this.combatStatManager = combatStatManager;
    this.bukkitEntity = bukkitEntity;
    human = bukkitEntity instanceof Player;
    if (human) {
      ((Player) bukkitEntity).setHealthScale(20D);
    }
    combatStatMappings = Maps.newEnumMap(CombatStat.getEmptyMap());
    baseValues = Maps.newEnumMap(CombatStat.getBaseMap(human));
    recalculatingSheduled = false;
    this.combatStatCalculator = combatStatManager.getCombatStatCalculator();
    combatBuffMap = new EnumMap<>(BuffType.class);
    statBuffMap = new EnumMap<>(BuffType.class);
    skillContainer = new SkillContainer(this);
  }

  private final CombatStatManager combatStatManager;
  @Setter(AccessLevel.PROTECTED)
  @Getter(AccessLevel.PROTECTED)
  private boolean recalculatingSheduled;
  @Getter
  private final LivingEntity bukkitEntity;
  @Getter
  private final boolean human;
  private final EnumMap<CombatStat, Double> combatStatMappings;
  private final EnumMap<CombatStat, Double> baseValues;
  private final CombatStatCalculator combatStatCalculator;
  private final EnumMap<BuffType, CombatBuff> combatBuffMap;
  private final EnumMap<BuffType, StatBuff> statBuffMap;
  private final SkillContainer skillContainer;
  @Getter
  private int mana = 0;
  @Getter
  private int level;
  private final long maxExp;
  @Getter
  private long exp;
  private long nextLevelExp;
  private long lastLevelExp;
  @Getter
  private long manaTickCount;
  @Getter
  @Setter
  private int manaPerUpdate = CombatStatManager.MANA_PER_UPDATE;

  void tickMana() {
    manaTickCount--;
    if (manaTickCount < 1) {
      manaTickCount = CombatStatManager.TICKS_PER_MANA;
      addMana(manaPerUpdate);
    }
  }

  public double getLevelProgress() {
    return (1.0D / getExpDelta()) * getExpFromLastLevel();
  }

  public long getExpDelta() {
    return nextLevelExp - lastLevelExp;
  }

  public long getExpFromLastLevel() {
    return exp - lastLevelExp;
  }

  public long getExpToNextLevel() {
    return nextLevelExp - exp;
  }

  public void addLevel(int amount) {
    while (amount-- > 0) {
      incrementLevel(true);
    }
  }

  public boolean hasLevel(final int lvl) {
    return level >= lvl;
  }

  public void addExp(final long value) {
    if (exp + value >= maxExp) {
      exp = maxExp;
      if (level != CombatStatManager.LEVEL_HARD_CAP) {
        incrementLevel(false);
      }
      return;
    }

    exp += value;
    applyIfPlayerOnline(player -> {
      final String expAdd = Message.elem("" + value + " Kampf Exp");
      Message.send(player, JobModule.class, "Du erhälst " + expAdd);
      UtilPlayer.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, 1.35F);
    });

    if (exp >= nextLevelExp) {
      incrementLevel(false);
    }
  }

  private void incrementLevel(final boolean updateExp) {
    level++;
    lastLevelExp = combatStatManager.getMinExpOfLevel(level);
    nextLevelExp = combatStatManager.getMinExpOfLevel(level + 1);
    if (updateExp) {
      exp = lastLevelExp + 1;
    }
    // TODO onLevelUp
  }

  public void removeExp(final long value) {
    exp -= value;
    if (exp <= 0) {
      exp = 0;
    }
  }

  public void removeMana(final int amount) {
    mana -= amount;
    if (mana < 0) {
      mana = 0;
    }
  }

  public void addMana(final int amount) {
    mana += amount;
    final int maxMana = (int) getStatValue(CombatStat.MANA);
    if (mana > maxMana) {
      mana = maxMana;
    }
  }

  public void applyIfPlayerOnline(final Consumer<Player> playerConsumer) {
    if (isHuman()) {
      final Player player = (Player) bukkitEntity;
      if (player.isOnline()) {
        playerConsumer.accept(player);
      }
    }
  }

  public void applyCombatBuffs(final CombatContext combatContext, final boolean asAttacker) {
    for (final CombatBuff cBuff : combatBuffMap.values()) {
      if (asAttacker) {
        if (!cBuff.getBuffType().isDefensive()) {
          cBuff.apply(combatContext);
        }
      } else {
        if (cBuff.getBuffType().isDefensive()) {
          cBuff.apply(combatContext);
        }
      }
    }
  }

  protected void applyStatBuffs(final Map<CombatStat, Double> statMap) {
    for (final StatBuff statBuff : statBuffMap.values()) {
      statBuff.apply(statMap);
    }
  }

  public void addBuff(final Buff buff) {
    if (buff.getBuffType().getBuffEnvironment() == BuffEnvironment.COMBAT) {
      final BuffType buffType = buff.getBuffType();
      final Buff currentBuff = combatBuffMap.get(buffType);
      if (buff.getBuffTier() >= currentBuff.getBuffTier()) {
        combatBuffMap.put(buffType, (CombatBuff) buff);
      }
    } else {
      final BuffType buffType = buff.getBuffType();
      final Buff currentBuff = statBuffMap.get(buffType);
      if (buff.getBuffTier() >= currentBuff.getBuffTier()) {
        statBuffMap.put(buffType, (StatBuff) buff);
      }
    }
  }

  public void removeBuff(final Buff buff) {
    if (buff.getBuffType().getBuffEnvironment() == BuffEnvironment.COMBAT) {
      final Buff oldBuff = combatBuffMap.get(buff.getBuffType());
      if (oldBuff.equals(buff)) {
        combatBuffMap.remove(buff.getBuffType());
      }
    } else {
      final Buff oldBuff = statBuffMap.get(buff.getBuffType());
      if (oldBuff.equals(buff)) {
        statBuffMap.remove(buff.getBuffType());
      }
    }
  }

  /**
   * Gets the complete value of a stat.
   *
   * @param stat the stat type
   * @return
   */
  public double getStatValue(final CombatStat stat) {
    return combatStatMappings.get(stat) + baseValues.get(stat);
  }

  public double getStatExtraValue(final CombatStat stat) {
    return combatStatMappings.get(stat);
  }

  /**
   * Gets only the base value of a stat.
   *
   * @param stat
   * @return
   */
  public double getStatBaseValue(final CombatStat stat) {
    return baseValues.getOrDefault(stat, 0D);
  }

  /**
   * Sets the base value of a stat.
   *
   * @param stat
   * @param value
   */
  public void setStatBaseValue(final CombatStat stat, final double value) {
    baseValues.put(stat, value + stat.getBaseValue(human));
  }

  public void recalculate() {
    combatStatCalculator.recalculateValues(this);
  }

  /**
   * Rebases a new value of a stat.
   *
   * @param stat  the stat
   * @param value the value
   * @return the old value.
   */
  protected double setExtraValue(final CombatStat stat, final double value) {
    return combatStatMappings.put(stat, value);
  }

  /**
   * Resets the stat map with base value imports.
   */
  protected void resetStatMap() {
    for (final CombatStat stat : CombatStat.values()) {
      combatStatMappings.put(stat, getStatBaseValue(stat));
    }
  }

  protected void tickBuffs() {
    final Set<Buff> removers = Sets.newHashSet();
    for (final Buff buff : combatBuffMap.values()) {
      if (buff.tickDuration()) {
        removers.add(buff);
      }
    }
    for (final Buff buff : statBuffMap.values()) {
      if (buff.tickDuration()) {
        removers.add(buff);
      }
    }
    for (final Buff remover : removers) {
      remover.remove();
    }
  }

  @Override
  public void castSkills(final SkillTrigger skillTrigger) {
    skillContainer.castAll(skillTrigger);
  }

  @Override
  public void addSkill(final SkillType skillType, final int lvl) {
    skillContainer.addSkill(skillType, lvl);
  }

  @Override
  public void removeSkill(final SkillType skillType) {
    skillContainer.removeSkill(skillType);
  }

  @Override
  public void levelupSkill(final SkillType skillType) {
    skillContainer.levelUp(skillType);
  }

  @Override
  public int getSkillLevel(final SkillType skillType) {
    return skillContainer.getSkillLevel(skillType);
  }

  @Override
  public boolean canLevelUp(final SkillType skillType) {
    return skillContainer.canLevelUp(skillType);
  }

  @Override
  public JsonObject serializeSkills() {
    final JsonObject json = skillContainer.serialize();

    return json;
  }

  @Override
  public void deserializeSkills(final JsonObject skillJson) {
    skillContainer.deserialize(skillJson);
  }

  public JsonObject serialize() {
    JsonObject json = new JsonObject();
    json.addProperty("Mana", mana);
    json.addProperty("Exp", exp);
    json.add("Skills", serializeSkills());
    return json;
  }

  public void deserialize(JsonObject json) {
    mana = json.get("Mana").getAsInt();
    exp = json.get("Exp").getAsLong();
    level = combatStatManager.getMaxLevelOfExp(exp);
    lastLevelExp = combatStatManager.getMinExpOfLevel(level - 1);
    nextLevelExp = combatStatManager.getMinExpOfLevel(level + 1);
    deserializeSkills(json.get("Skills").getAsJsonObject());
  }

}