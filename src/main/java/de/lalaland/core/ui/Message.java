package de.lalaland.core.ui;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.chat.channels.ChatChannel;
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
public class Message {

  public static void init(final CorePlugin plugin) {
    corePlugin = plugin;
  }

  private static CorePlugin corePlugin;

  private static final String MODULE_COLOR = "§9";
  private static final String CHAT_CHANNEL_COLOR = "§a";
  private static final String ERROR_COLOR = "§4";
  private static final String MESSAGE_COLOR = "§7";
  private static final String ELEMENT_COLOR = "§e";

  public static void send(final Player player, final Class<? extends IModule> clazz, final String message) {
    send(player, corePlugin.getModule(clazz).getModuleName(), message);
  }

  /**
   * Used to send a message to any player.
   *
   * @param player  the player
   * @param module  the prefix
   * @param message the message
   */
  public static void send(final Player player, final String module, final String message) {
    player.sendMessage(MODULE_COLOR + module + "> " + MESSAGE_COLOR + message);
  }

  /**
   * Used to format elements.
   *
   * @param input the input element.
   * @return a formated element.
   */
  public static String elem(final String input) {
    return ELEMENT_COLOR + input + MESSAGE_COLOR;
  }

  /**
   * Used to send error message to player.
   *
   * @param player  the player
   * @param module  the prefix
   * @param message the message
   */
  public static void error(final Player player, final String module, final String message) {
    player.sendMessage(ERROR_COLOR + module + "> " + MESSAGE_COLOR + message);
  }

  public static void error(final Player player, final Class<? extends IModule> clazz, final String message) {
    error(player, corePlugin.getModule(clazz).getModuleName(), message);
  }

  public static String chat(final Player player, final ChatChannel chatChannel, final String message){
    final String format = CHAT_CHANNEL_COLOR+"["+chatChannel.getDisplayName()+"] §7"+player.getName()
        +": §f"+message;
    return format;
  }

}
