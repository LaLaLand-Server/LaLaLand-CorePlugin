package de.lalaland.core.modules.structures.core;

import de.lalaland.core.modules.jobs.jobdata.JobType;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 27.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum ResourceType {

  OAK_TREE("Eichen Baum", 25, JobType.WOODCUTTER, 1, 4,
      5, 30000L,
      new Material[]{Material.OAK_LOG});

  private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
  @Getter
  private final String displayName;
  @Getter
  private final int maxHealth;
  @Getter
  private final JobType jobType;
  @Getter
  private final int jobLevel;
  @Getter
  private final int jobExp;
  @Getter
  private final int schematicCount;
  @Getter
  private final long respawnTimeMS;
  @Getter
  private final Material[] validMaterials;

  public int getRandomSchematicIndex() {
    return RANDOM.nextInt(schematicCount);
  }

}