package org.polydev.gaea.structures.loot;

import net.jafama.FastMath;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.polydev.gaea.structures.loot.functions.AmountFunction;
import org.polydev.gaea.structures.loot.functions.DamageFunction;
import org.polydev.gaea.structures.loot.functions.EnchantWithLevelsFunction;
import org.polydev.gaea.structures.loot.functions.Function;
import org.polydev.gaea.util.GlueList;

import java.util.List;
import java.util.Random;

/**
 * Representation of a single item entry within a Loot Table pool.
 */
public class Entry {
    private final Material item;
    private final long weight;
    private final List<Function> functions = new GlueList<>();

    /**
     * Instantiates an Entry from a JSON representation.
     *
     * @param entry The JSON Object to instantiate from.
     */
    public Entry(JSONObject entry) {

        String id = entry.get("name").toString();
        if(id.contains(":")) this.item = Material.matchMaterial(id);
        else this.item = Material.valueOf(entry.get("name").toString().toUpperCase());

        long weight1;
        try {
            weight1 = (long) entry.get("weight");
        } catch(NullPointerException e) {
            weight1 = 1;
        }

        this.weight = weight1;
        if(entry.containsKey("functions")) {
            for(Object function : (JSONArray) entry.get("functions")) {
                switch(((String) ((JSONObject) function).get("function"))) {
                    case "minecraft:set_count":
                    case "set_count":
                        long max = (long) ((JSONObject) ((JSONObject) function).get("count")).get("max");
                        long min = (long) ((JSONObject) ((JSONObject) function).get("count")).get("min");
                        functions.add(new AmountFunction(FastMath.toIntExact(min), FastMath.toIntExact(max)));
                        break;
                    case "minecraft:set_damage":
                    case "set_damage":
                        long maxDamage = (long) ((JSONObject) ((JSONObject) function).get("damage")).get("max");
                        long minDamage = (long) ((JSONObject) ((JSONObject) function).get("damage")).get("min");
                        functions.add(new DamageFunction(FastMath.toIntExact(minDamage), FastMath.toIntExact(maxDamage)));
                        break;
                    case "minecraft:enchant_with_levels":
                    case "enchant_with_levels":
                        long maxEnchant = (long) ((JSONObject) ((JSONObject) function).get("levels")).get("max");
                        long minEnchant = (long) ((JSONObject) ((JSONObject) function).get("levels")).get("min");
                        JSONArray disabled = null;
                        if(((JSONObject) function).containsKey("disabled_enchants"))
                            disabled = (JSONArray) ((JSONObject) function).get("disabled_enchants");
                        functions.add(new EnchantWithLevelsFunction(FastMath.toIntExact(minEnchant), FastMath.toIntExact(maxEnchant), disabled));
                        break;
                }
            }
        }
    }

    /**
     * Fetches a single ItemStack from the Entry, applying all functions to it.
     *
     * @param r The Random instance to apply functions with
     * @return ItemStack - The ItemStack with all functions applied.
     */
    public ItemStack getItem(Random r) {
        ItemStack item = new ItemStack(this.item, 1);
        for(Function f : functions) {
            item = f.apply(item, r);
        }
        return item;
    }

    /**
     * Gets the weight attribute of the Entry.
     *
     * @return long - The weight of the Entry.
     */
    public long getWeight() {
        return this.weight;
    }
}
