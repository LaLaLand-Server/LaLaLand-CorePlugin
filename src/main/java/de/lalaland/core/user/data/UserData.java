package de.lalaland.core.user.data;

import com.google.common.collect.Lists;
import de.lalaland.core.modules.chat.messages.OfflineMessage;
import de.lalaland.core.modules.protection.zones.WorldZone;
import java.util.EnumSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

//TODO Klasse komplett verwerfen und ordentliche Subklassen serialisieren
@Data
@AllArgsConstructor
public class UserData {

  private int moneyOnHand;
  private int moneyOnBank;
  private List<OfflineMessage> offlineMessages = Lists.newArrayList();
  private EnumSet<WorldZone> discovoredZones = EnumSet.noneOf(WorldZone.class);

  public boolean hasDiscovered(final WorldZone zone) {
    return discovoredZones.contains(zone);
  }

  public void addZoneDiscovery(final WorldZone zone) {
    discovoredZones.add(zone);
  }

}
