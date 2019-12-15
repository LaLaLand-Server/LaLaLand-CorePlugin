package de.lalaland.core.user;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.io.IReader;
import de.lalaland.core.io.IWriter;
import de.lalaland.core.io.gson.GsonFileReader;
import de.lalaland.core.io.gson.GsonFileWriter;
import de.lalaland.core.io.mongodb.MongoDataReader;
import de.lalaland.core.io.mongodb.MongoDataWriter;
import de.lalaland.core.modules.chat.messages.OfflineMessage;
import de.lalaland.core.modules.economy.EconomyModule;
import de.lalaland.core.modules.protection.zones.WorldZone;
import de.lalaland.core.ui.Message;
import de.lalaland.core.user.data.UserData;
import de.lalaland.core.utils.common.UtilPlayer;
import de.lalaland.core.utils.tuples.Unit;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class User {

  public static int MAX_OFFLINE_MESSAGES = 10;

  private final CorePlugin corePlugin;
  @Getter
  private boolean updateCandidate;
  @Getter
  private final UUID uuid;
  @Getter
  private final UserData userData;
  @Getter
  private final File userDataDirectory;
  @Getter
  private final Unit<Player> onlinePlayer;

  public User(final CorePlugin corePlugin, final UUID uuid) {
    this.corePlugin = corePlugin;
    this.uuid = uuid;
    userDataDirectory = new File(corePlugin.getDataFolder() + File.separator + "userdatas");
    userData = loadData();
    onlinePlayer = new Unit<>(Bukkit.getPlayer(uuid));
    updateCandidate = false;
  }

  public void applyWhenOnline(final Consumer<Player> playerConsumer) {
    if (onlinePlayer.isPresent()) {
      playerConsumer.accept(onlinePlayer.getValue());
    }
  }

  public void setOnline() {
    onlinePlayer.setValue(Bukkit.getPlayer(uuid));
  }

  public void setOffline() {
    onlinePlayer.setValue(null);
  }

  public void save() {

    final IWriter writer;

    if (corePlugin.getCoreConfig().isSaveDataInDatabase()) {
      writer = new MongoDataWriter();
    } else {
      writer = new GsonFileWriter(corePlugin, userDataDirectory, uuid.toString());
    }
    writer.write(getUserData());
    updateCandidate = false;
  }

  private UserData loadData() {

    final IReader reader;

    if (corePlugin.getCoreConfig().isSaveDataInDatabase()) {
      reader = new MongoDataReader();
    } else {
      reader = new GsonFileReader(corePlugin, userDataDirectory, uuid.toString());
    }

    return reader.read(UserData.class, getDefaultUserData());
  }

  public void addExp(final long amount) {

    userData.addExp(amount);
    applyWhenOnline(player -> {
      UtilPlayer.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
      // TODO show +exp
    });

    if (userData.canLevelup()) {
      userData.increaseLevel();
      applyWhenOnline(player -> {
        UtilPlayer.playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
        player.sendTitle("§6Level Up", "§fLevel > " + userData.getLevel(), 20, 60, 20);
        if (userData.getLevel() % 10 == 0) {
          UtilPlayer.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE);
        }
      });
    }
    updateCandidate = true;
  }

  public void addMoney(final int amount, final boolean bank) {

    if (bank) {
      userData.setMoneyOnBank(userData.getMoneyOnBank() + amount);
      applyWhenOnline(player -> {
        Message.send(player, EconomyModule.class, "Deine Bank erhält §6" + amount + " Gold");
        // TODO custom sound
        UtilPlayer.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.75F, 1.45F);
      });
    } else {
      userData.setMoneyOnHand(userData.getMoneyOnHand() + amount);
      applyWhenOnline(player -> {
        Message.send(player, EconomyModule.class, "Du erhälst §e" + amount + " Gold");
        // TODO custom sound
        UtilPlayer.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.75F, 1.45F);
      });
    }
    updateCandidate = true;
  }


  public void removeMoney(final int amount, final boolean bank) {

    if (bank) {

      if (userData.getMoneyOnBank() - amount < 0) {
        userData.setMoneyOnBank(0);
      } else {
        userData.setMoneyOnBank(userData.getMoneyOnBank() - amount);
      }

    } else {

      if (userData.getMoneyOnHand() - amount < 0) {
        userData.setMoneyOnHand(0);
      } else {
        userData.setMoneyOnHand(userData.getMoneyOnHand() - amount);
      }

    }
    updateCandidate = true;
  }

  public boolean hasEnoughMoney(final int amount, final boolean bank) {
    if (bank) {
      return userData.getMoneyOnBank() >= amount;
    }

    return userData.getMoneyOnHand() >= amount;
  }

  //if handToBank = true transfers money from the hand to the bank
  //if handToBank = false transfers money from the bank to the hand
  public void transferMoney(final int amount, final boolean handToBank) {

    if (handToBank) {
      removeMoney(amount, false);
      addMoney(amount, true);
    } else {
      removeMoney(amount, true);
      addMoney(amount, false);
    }

  }

  //TODO implement and use format method
  public String addOfflineMessage(final OfflineMessage offlineMessage) {

    final List<OfflineMessage> offlineMessages = userData.getOfflineMessages();

    if (offlineMessages.size() >= MAX_OFFLINE_MESSAGES) {
      return "Der Spieler hat zu viele Nachrichten im Postfach.";
    }

    if (getOfflineMessagesFrom(offlineMessage.getAuthor()).size() >= 2) {
      return "Du hast diesem Spieler bereits 2 Nachrichten hinterlassen.";
    }

    userData.getOfflineMessages().add(offlineMessage);
    updateCandidate = true;
    return "Du hast dem Spieler deine Nachricht hinterlassen.";
  }

  public void clearOfflineMessages() {
    userData.getOfflineMessages().clear();
    updateCandidate = true;
  }

  private List<OfflineMessage> getOfflineMessagesFrom(final UUID target) {
    final List<OfflineMessage> offlineMessages = new ArrayList<>();
    for (final OfflineMessage offlineMessage : userData.getOfflineMessages()) {
      if (offlineMessage.getAuthor().equals(target)) {
        offlineMessages.add(offlineMessage);
      }
    }
    return offlineMessages;
  }

  private UserData getDefaultUserData() {
    return new UserData(1, 0, 0, 0, new ArrayList<>(), EnumSet.noneOf(WorldZone.class)); // everything set to 0 and new
  }

}
