package de.lalaland.core.modules.combat.stats;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.lalaland.core.modules.skills.skillimpl.Skill;
import de.lalaland.core.modules.skills.skillimpl.SkillTrigger;
import de.lalaland.core.modules.skills.skillimpl.SkillType;
import java.util.EnumMap;
import java.util.Map.Entry;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SkillContainer {

  protected SkillContainer(final CombatStatHolder holder) {
    this.holder = holder;
    skillTriggerMapings = new EnumMap<>(SkillTrigger.class);
    for (final SkillTrigger trigger : SkillTrigger.values()) {
      skillTriggerMapings.put(trigger, new EnumMap<>(SkillType.class));
    }
  }

  private final EnumMap<SkillTrigger, EnumMap<SkillType, Skill>> skillTriggerMapings;
  private final CombatStatHolder holder;

  public JsonObject serialize() {
    final JsonObject json = new JsonObject();
    final JsonObject skillJson = new JsonObject();
    for (final SkillTrigger trigger : SkillTrigger.values()) {
      for (final Skill skill : skillTriggerMapings.get(trigger).values()) {
        skillJson.addProperty(skill.getSkillType().toString(), skill.getLevel());
      }
    }
    json.add("Skills", skillJson);
    return json;
  }

  public void deserialize(final JsonObject json) {
    final JsonObject skillJson = json.get("Skills").getAsJsonObject();
    for (final Entry<String, JsonElement> entry : skillJson.entrySet()) {
      final SkillType skillType = SkillType.valueOf(entry.getKey());
      final int lvl = entry.getValue().getAsInt();
      addSkill(skillType, lvl);
    }
  }

  public void levelUp(final SkillType skillType) {
    skillTriggerMapings.get(skillType.getSkillTrigger()).get(skillType).levelUp();
  }

  public void addSkill(final SkillType skillType, final int lvl) {
    final Skill skillInstance = skillType.getInstanceCreator().apply(holder, lvl);
    skillTriggerMapings.get(skillType.getSkillTrigger()).put(skillType, skillInstance);
  }

  public void removeSkill(final SkillType skillType) {
    skillTriggerMapings.get(skillType.getSkillTrigger()).remove(skillType);
  }

  public void castAll(final SkillTrigger trigger) {
    for (final Skill skill : skillTriggerMapings.get(trigger).values()) {
      skill.cast();
    }
  }

  public int getSkillLevel(final SkillType skillType) {
    int lvl = 0;
    final Skill skill = skillTriggerMapings.get(skillType.getSkillTrigger()).get(skillType);
    if (skill != null) {
      lvl = skill.getLevel();
    }
    return lvl;
  }

  public boolean canLevelUp(final SkillType skillType) {
    boolean can = false;
    final Skill skill = skillTriggerMapings.get(skillType.getSkillTrigger()).get(skillType);
    if (skill != null) {
      can = skill.canLevelUp();
    }
    return can;
  }

}
