package net.attikai.tag;

import net.attikai.EnduringTools;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<Item> SHOULD_ENDURE = TagKey.of(RegistryKeys.ITEM, Identifier.of(EnduringTools.MOD_ID, "should_endure"));
}
