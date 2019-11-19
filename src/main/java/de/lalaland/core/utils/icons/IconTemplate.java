package de.lalaland.core.utils.icons;

import de.lalaland.core.utils.items.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public enum IconTemplate {

  NEXT_PAGE(null, -1),
  PREVIOUS_PAGE(null, -1),
  SEND_MONEY(null, -1),
  MONEY_ON_HAND(null, -1);

  private final ItemStack item;
  @Getter
  private final int modelId;

  public ItemStack getDisplayItem() {
    final ItemStack builder = new ItemBuilder(item).modelData(getModelId()).build();
    return builder;
  }

}
