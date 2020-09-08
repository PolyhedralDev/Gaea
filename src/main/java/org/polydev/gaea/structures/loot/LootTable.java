package org.polydev.gaea.structures.loot;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class representation of a Loot Table to populate chest loot.
 */
public class LootTable {
    private final List<Pool> pools = new ArrayList<>();

    /**
     * Instantiates a LootTable from a JSON String.
     *
     * @param json The JSON String representing the loot table.
     * @throws ParseException if malformed JSON is passed.
     */
    public LootTable(String json) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        Object tableJSON = jsonParser.parse(json);
        JSONArray poolArray = (JSONArray) ((JSONObject) tableJSON).get("pools");
        for(Object pool : poolArray) {
            pools.add(new Pool((JSONObject) pool));
        }
    }

    /**
     * Fetches a list of ItemStacks from the loot table using the given Random instance.
     *
     * @param r The Random instance to use.
     * @return List&lt;ItemStack&gt; - The list of loot fetched.
     */
    public List<ItemStack> getLoot(Random r) {
        List<ItemStack> itemList = new ArrayList<>();
        for(Pool pool : pools) {
            itemList.addAll(pool.getItems(r));
        }
        return itemList;
    }

    /**
     * Fills an Inventory with loot.
     *
     * @param i The Inventory to fill.
     * @param r The The Random instance to use.
     */
    public void fillInventory(Inventory i, Random r) {
        List<ItemStack> loot = getLoot(r);
        for(ItemStack stack : loot) {
            while(stack.getAmount() != 0) {
                int deposit = Math.min(r.nextInt(2) + 1, stack.getAmount());
                ItemStack newStack = stack.clone();
                newStack.setAmount(deposit);
                i.setItem(r.nextInt(i.getSize()), newStack);
                stack.setAmount(stack.getAmount() - deposit);
            }
        }
    }
}
