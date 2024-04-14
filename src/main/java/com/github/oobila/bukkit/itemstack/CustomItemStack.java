package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.effects.Attribute;
import com.github.oobila.bukkit.effects.AttributeManager;
import com.github.oobila.bukkit.effects.Effect;
import com.github.oobila.bukkit.itemstack.effects.ItemBehaviour;
import com.github.oobila.bukkit.itemstack.effects.ItemSlot;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.github.oobila.bukkit.common.ABCommon.log;
import static com.github.oobila.bukkit.itemstack.PluginUtil.getCorePlugin;

public class CustomItemStack extends ItemStack {

    private static final String CUSTOM_LORE_SECTION_DELIM = "//";
    private static final String CUSTOM_LORE_SECTION_DELIM_REPLACEMENT = "/ /";
    private static final String ITEM_EFFECTS_TAG = "itemEffects";
    private static final String ITEM_ATTRIBUTES_TAG = "itemAttributes";
    private static final String ITEM_BEHAVIOUR_TAG = "itemBehaviour";
    private static final String ITEM_BEHAVIOUR_SLOT_TAG = "itemBehaviourSlot";

    public CustomItemStack(Material material){
        super(material);
    }

    public CustomItemStack(ItemStack itemStack){
        super(itemStack.getType());
        setItemMeta(itemStack.getItemMeta());
        setAmount(itemStack.getAmount());
        setData(itemStack.getData());
    }

    protected void setDisplayName(String displayName){
        ItemMeta meta = Objects.requireNonNull(getItemMeta());
        meta.setDisplayName(displayName);
        setItemMeta(meta);
    }

    public void addMeta(String name, String value) {
        ItemMeta meta = Objects.requireNonNull(getItemMeta());
        PersistentMetaUtil.addString(meta, new NamespacedKey(getCorePlugin(), name), value);
        setItemMeta(meta);
    }

    public void addMeta(String name, UUID value) {
        ItemMeta meta = Objects.requireNonNull(getItemMeta());
        PersistentMetaUtil.addUUID(meta, new NamespacedKey(getCorePlugin(), name), value);
        setItemMeta(meta);
    }

    public void addMeta(String name, int value) {
        ItemMeta meta = Objects.requireNonNull(getItemMeta());
        PersistentMetaUtil.addInt(meta, new NamespacedKey(getCorePlugin(), name), value);
        setItemMeta(meta);
    }

    public void addMeta(String name, double value) {
        ItemMeta meta = Objects.requireNonNull(getItemMeta());
        PersistentMetaUtil.addDouble(meta, new NamespacedKey(getCorePlugin(), name), value);
        setItemMeta(meta);
    }

    public void addMeta(String name, LocalDateTime value) {
        ItemMeta meta = Objects.requireNonNull(getItemMeta());
        PersistentMetaUtil.addLocalDateTime(meta, new NamespacedKey(getCorePlugin(), name), value);
        setItemMeta(meta);
    }

    public String getMetaString(String name) {
        return PersistentMetaUtil.getString(getItemMeta(), new NamespacedKey(getCorePlugin(), name));
    }

    public UUID getMetaUUID(String name) {
        return PersistentMetaUtil.getUUID(getItemMeta(), new NamespacedKey(getCorePlugin(), name));
    }

    public int getMetaInt(String name) {
        return PersistentMetaUtil.getInt(getItemMeta(), new NamespacedKey(getCorePlugin(), name));
    }

    public double getMetaDouble(String name) {
        return PersistentMetaUtil.getDouble(getItemMeta(), new NamespacedKey(getCorePlugin(), name));
    }

    public LocalDateTime getMetaDate(String name) {
        return PersistentMetaUtil.getLocalDateTime(getItemMeta(), new NamespacedKey(getCorePlugin(), name));
    }

    public void removeMeta(String name){
        ItemMeta meta = getItemMeta();
        PersistentMetaUtil.remove(meta, new NamespacedKey(getCorePlugin(), name));
        setItemMeta(meta);
    }

    public void setLore(List<String> lore) {
        removeMeta("lore");
        if (!lore.isEmpty()) {
            addMeta("lore", lore.stream()
                    .map(s -> s.replace(CUSTOM_LORE_SECTION_DELIM, CUSTOM_LORE_SECTION_DELIM_REPLACEMENT))
                    .collect(Collectors.joining(CUSTOM_LORE_SECTION_DELIM))
            );
        }
        updateLore();
    }

    public List<String> getLore() {
        String loreString = getMetaString("lore");
        if (loreString == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(loreString.split(CUSTOM_LORE_SECTION_DELIM)).toList();
    }

    private void updateLore() {
        List<String> currentLore = getLore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();
        }
        final List<String> lore = currentLore;

        getItemEffects().stream()
                .filter(effect -> effect.getDisplayName() != null && !effect.getDisplayName().isEmpty())
                .forEach(effect -> lore.add(effect.getDisplayName()));
        getAttributes().stream()
                .filter(attribute -> attribute.getDisplayName() != null && !attribute.getDisplayName().isEmpty())
                .forEach(attribute -> lore.add(attribute.getDisplayName()));

        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        setItemMeta(meta);
    }

    public <T> void addItemEffect(Effect<T> effect) {
        if (AttributeManager.effectOf(effect.getName()) == null) {
            log(Level.WARNING, "Effect \"{0}\" has not been registered!", effect.getName());
            return;
        }
        Set<Effect<?>> effects = new HashSet<>(getItemEffects());
        effects.add(effect);
        String effectString = effects.stream()
                .map(Effect::getName)
                .collect(Collectors.joining("|"));
        removeMeta(ITEM_EFFECTS_TAG);
        addMeta(ITEM_EFFECTS_TAG, effectString);
        updateLore();
    }

    public Set<Effect<?>> getItemEffects() {
        String effectString = getMetaString(ITEM_EFFECTS_TAG);
        if (effectString == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(effectString.split("\\|"))
                .map(AttributeManager::effectOf)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void addAttribute(Attribute attribute) {
        if (AttributeManager.attributeOf(attribute.getName()) == null) {
            log(Level.WARNING, "Attribute \"{0}\" has not been registered!", attribute.getName());
            return;
        }
        Set<Attribute> attributes = new HashSet<>(getAttributes());
        attributes.add(attribute);
        String attributeString = attributes.stream()
                .map(Attribute::getName)
                .collect(Collectors.joining("|"));
        removeMeta(ITEM_ATTRIBUTES_TAG);
        addMeta(ITEM_ATTRIBUTES_TAG, attributeString);
        updateLore();
    }

    public Set<Attribute> getAttributes() {
        String attributeString = getMetaString(ITEM_ATTRIBUTES_TAG);
        if (attributeString == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(attributeString.split("\\|"))
                .map(AttributeManager::attributeOf)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void setBehaviour(ItemBehaviour itemBehaviour, ItemSlot itemSlot) {
        removeMeta(ITEM_BEHAVIOUR_TAG);
        addMeta(ITEM_BEHAVIOUR_TAG, itemBehaviour.toString());
        removeMeta(ITEM_BEHAVIOUR_SLOT_TAG);
        addMeta(ITEM_BEHAVIOUR_SLOT_TAG, itemSlot.toString());
    }

    public ItemBehaviour getBehaviour() {
        return ItemBehaviour.valueOf(getMetaString(ITEM_BEHAVIOUR_TAG));
    }

    public ItemSlot getBehaviourSlot() {
        return ItemSlot.valueOf(getMetaString(ITEM_BEHAVIOUR_SLOT_TAG));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static CustomItemStackBuilder builder(Material material){
        return new CustomItemStackBuilder(material);
    }

    public static CustomItemStackBuilder builder(ItemStack itemStack){
        return new CustomItemStackBuilder(itemStack);
    }
}
