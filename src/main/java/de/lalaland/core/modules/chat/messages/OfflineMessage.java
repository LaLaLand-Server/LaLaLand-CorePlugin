package de.lalaland.core.modules.chat.messages;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfflineMessage {

  private UUID author;
  private long timeStamp;
  private String content;

}
