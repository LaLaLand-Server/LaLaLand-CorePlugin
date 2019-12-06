package de.lalaland.core.modules.protection.zones;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 06.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum WorldZone {

  MAIN_CITY("Hauptstadt", null, 10),
  HIGH_LANDS("Hochland", null, 20);

  @Getter
  private final String displayName;
  @Getter
  private final UUID regionID;
  @Getter
  private final int discoveryExp;

}
