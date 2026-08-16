package net.attikai.tag;

import net.attikai.EnduringTools;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final TagKey<Item> SHOULD_ENDURE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EnduringTools.MOD_ID, "should_endure"));
}
