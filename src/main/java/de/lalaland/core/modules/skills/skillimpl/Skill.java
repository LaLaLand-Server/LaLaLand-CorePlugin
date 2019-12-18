package de.lalaland.core.modules.skills.skillimpl;

import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.skills.SkillModule;
import de.lalaland.core.utils.Message;
import java.util.List;
import lombok.Getter;
import net.crytec.libs.commons.utils.UtilMath;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class Skill {

  public Skill(final SkillType skillType, final CombatStatHolder caster, final int level) {
    this.skillType = skillType;
    this.caster = caster;
    this.level = level;
  }

  private long lastCastUnix;
  @Getter
  private int level;
  private final CombatStatHolder caster;
  @Getter
  private final SkillType skillType;

  public void levelUp() {
    if (canLevelUp()) {
      level++;
    }
  }

  public boolean canLevelUp() {
    return level < skillType.getMaxLevel();
  }

  private long getCooldownLeft() {
    return lastCastUnix + getCooldownms() - System.currentTimeMillis();
  }

  protected void onCooldownCast(final long cooldownLeft) {
    caster.applyIfPlayerOnline(player -> {
      final double left = UtilMath.unsafeRound((cooldownLeft / 1000D), 1);
      final String time = Message.elem("" + left + "s");
      Message.error(player, SkillModule.class, "Cooldown: " + time);
    });
  }

  protected void onNoManaCast(final int manaLeft) {
    caster.applyIfPlayerOnline(player -> {
      final String mana = Message.elem("" + Math.abs(manaLeft) + " Mana");
      Message.error(player, SkillModule.class, "Noch weitere " + mana + " benötigt.");
    });
  }

  protected void onPreventCast() {

  }

  protected boolean canCast() {
    return true;
  }

  public void cast() {
    if (canCast()) {
      final long cdLeft = getCooldownLeft();
      if (cdLeft < 0) {
        final int manaCost = getManaCost();
        final int manaLeft = caster.getMana() - manaCost;
        if (manaLeft >= 0) {
          onCast();
          lastCastUnix = System.currentTimeMillis();
          caster.removeMana(manaCost);
        } else {
          onNoManaCast(manaLeft);
        }
      } else {
        onCooldownCast(cdLeft);
      }
    } else {
      onPreventCast();
    }
  }

  public abstract long getCooldownms();

  public abstract int getManaCost();

  protected abstract void onCast();

  public abstract List<String> getDescription(final CombatStatHolder caster);

}