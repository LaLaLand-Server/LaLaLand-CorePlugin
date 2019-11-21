package de.lalaland.core.modules.chat.channels;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ChatChannel {

  GLOBAL("Global"),
  TRADING("Handel");

  @Getter
  private final String displayName;


}
