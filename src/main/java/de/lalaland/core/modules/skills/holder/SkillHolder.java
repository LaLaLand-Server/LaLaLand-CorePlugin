package de.lalaland.core.modules.skills.holder;

import com.google.gson.JsonObject;
import de.lalaland.core.modules.skills.skillimpl.SkillTrigger;
import de.lalaland.core.modules.skills.skillimpl.SkillType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface SkillHolder {

  // TODO Skill context übergeben
  public abstract void castSkills(SkillTrigger skillTrigger);

  public abstract void addSkill(SkillType skillType, int lvl);

  public abstract void removeSkill(SkillType skillType);

  public abstract void levelupSkill(SkillType skillType);

  public abstract int getSkillLevel(SkillType skillType);

  public abstract boolean canLevelUp(SkillType skillType);

  public abstract JsonObject serializeSkills();

  public abstract void deserializeSkills(JsonObject skillJson);

}
