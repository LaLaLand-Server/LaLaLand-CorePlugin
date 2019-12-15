package de.lalaland.core.modules.combat.stats.buffs;

import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.resourcepack.skins.Model;
import java.util.function.BiFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum BuffType {

  MEELE_ATTACK_BOOST("Nahkampf Angriffs Boost", false, BuffEnvironment.COMBAT, Model.RED_X, (holder, lvl) -> null);

  @Getter
  private final String displayName;
  @Getter
  private final boolean defensive;
  @Getter
  private final BuffEnvironment buffEnvironment;
  @Getter
  private final Model model;
  @Getter
  private final BiFunction<CombatStatHolder, Integer, Buff> buffSupplier;

  public void addBuff(final CombatStatHolder holder, final int tier) {
    holder.addBuff(buffSupplier.apply(holder, tier));
  }

}
