package de.lalaland.core.utils.items;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	public ItemBuilder(final Material material) {
		this(new ItemStack(material));
	}
	
	public ItemBuilder(final ItemStack itemStack) {
		this.itemMeta = itemStack.getItemMeta();
		this.material = itemStack.getType();
		if(this.itemMeta.hasLore()) {
			this.loreLines = Lists.newArrayList(this.itemMeta.getLore());
		}else {
			this.loreLines = Lists.newArrayList();
		}
	}
	
	private Material material;
	private ItemMeta itemMeta;
	private final List<String> loreLines;
	
	public ItemBuilder meta(final ItemMeta meta) {
		this.itemMeta = meta;
		return this;
	}
	
	public ItemBuilder material(final Material material) {
		this.material = material;
		return this;
	}
	
	public ItemBuilder name(final String name) {
		this.itemMeta.setDisplayName(name);
		return this;
	}
	
	public ItemBuilder lore(final String line) {
		this.loreLines.add(line);
		return this;
	}
	
	public ItemBuilder lore(final String... lines) {
		for(String line : lines) {
			this.loreLines.add(line);
		}
		return this;
	}
	
	public ItemBuilder lore(final Collection<String> lines) {
		for(String line : lines) {
			this.loreLines.add(line);
		}
		return this;
	}
	
	public ItemBuilder attribute(final Attribute attribute, final AttributeModifier modifier) {
		this.itemMeta.addAttributeModifier(attribute, modifier);
		return this;
	}
	
	public ItemBuilder enchant(final Enchantment enchantmen, final int level) {
		this.itemMeta.addEnchant(enchantmen, level, true);
		return this;
	}
	
	public ItemBuilder flag(final ItemFlag... flags) {
		this.itemMeta.addItemFlags(flags);
		return this;
	}
	
	public ItemBuilder setUnbreakable(final boolean value) {
		this.itemMeta.setUnbreakable(value);
		return this;
	}
	
	public ItemBuilder modelData(final int customModelData) {
		this.itemMeta.setCustomModelData(customModelData);
		return this;
	}
	
	public NBTItemBuilder asNBTBuilder() {
		return new NBTItemBuilder(this.build());
	}
	
	public ItemStack build() {
		ItemStack item = new ItemStack(this.material);
		if(!this.loreLines.isEmpty()) this.itemMeta.setLore(this.loreLines);
		item.setItemMeta(this.itemMeta);
		return item;
	}
	
}
