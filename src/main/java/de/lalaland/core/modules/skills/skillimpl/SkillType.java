package de.lalaland.core.modules.skills.skillimpl;

import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.resourcepack.skins.Model;
import java.util.function.BiFunction;
import java.util.function.Predicate;
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
public enum SkillType {

  FIREBALL("Feuer Ball", Model.RED_X, 10, SkillTreeType.FIRE_MAGIC, SkillTrigger.ACTIVE_CAST, holder -> true, (holder, level) -> {
    return null;
  });

  @Getter
  private final String displayName;
  @Getter
  private final Model model;
  @Getter
  private final int maxLevel;
  @Getter
  private final SkillTreeType skillTreeType;
  @Getter
  private final SkillTrigger skillTrigger;
  @Getter
  private final Predicate<CombatStatHolder> chooseCondition;
  @Getter
  private final BiFunction<CombatStatHolder, Integer, Skill> instanceCreator;

}
