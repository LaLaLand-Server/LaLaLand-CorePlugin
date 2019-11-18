package de.lalaland.core.ui.gui.impl;

import de.lalaland.core.ui.gui.icon.impl.IIcon;
import de.lalaland.core.utils.items.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGui implements IGui {

    @Getter
    private final String title;
    @Getter
    private final ItemStack placeHolder;
    private Inventory bukkitInventory;
    @Getter
    private HashMap<Integer, IIcon> icons;
    @Getter
    private final boolean filled;

    public AbstractGui(String title, InventoryType type, int raws, boolean filled){
        this.title = title;
        this.placeHolder = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();
        this.icons = new HashMap<Integer, IIcon>();
        this.filled = filled;
        buildBukkitInventory(type,raws);
        update();
    }

    public AbstractGui(String title, InventoryType type, boolean filled){
        this(title,type,0,filled);
    }

    public AbstractGui(String title, int raws, boolean filled){
        this(title,InventoryType.CHEST,raws, filled);
    }

    private void buildBukkitInventory(InventoryType type, int raws){
        Inventory inventory;

        if(!type.equals(InventoryType.CHEST)){
            inventory = Bukkit.createInventory(null,type,title);
        }else{
            inventory = Bukkit.createInventory(null,9*raws,title);
        }

        this.bukkitInventory = inventory;
    }

    public void removeIcon(int slot, boolean update) {

        icons.remove(slot);

        if(update){
            this.bukkitInventory.setItem(slot,placeHolder);
        }

    }

    public void setIcon(int slot, IIcon icon, boolean update) {

        this.icons.put(slot,icon);

        if(update){
            this.bukkitInventory.setItem(slot,icon.getDisplayItem());
        }

    }

    public void changeIcon(int slot, IIcon icon) {
        removeIcon(slot,false);
        setIcon(slot,icon, true);
    }

    public void update() {

        this.bukkitInventory.clear();

        if(filled){
            fill();
        }

        for(Map.Entry<Integer, IIcon> entry : this.icons.entrySet()){
            this.bukkitInventory.setItem(entry.getKey(),entry.getValue().getDisplayItem());
        }

    }

    public void fill() {

        for(int i = 0; i < this.bukkitInventory.getSize(); i++){
            this.bukkitInventory.setItem(i,placeHolder);
        }

    }
}