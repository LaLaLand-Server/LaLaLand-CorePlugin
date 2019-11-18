package de.lalaland.core.ui.gui.impl;


import de.lalaland.core.ui.gui.icon.impl.IIcon;
import org.bukkit.entity.Player;

import java.util.HashMap;

public interface IGui {

    String getTitle();

    HashMap<Integer, IIcon> getIcons();

    void fill();

    void open(Player player);

    void close(Player player);

    void setIcon(int slot, IIcon icon, boolean update);

    void removeIcon(int slot, boolean update);

    void changeIcon(int slot, IIcon icon);

    void update();

}