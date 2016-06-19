package net.eiveo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Mapping {
	private static final Map<String, Map<Integer, String>> mapping = new HashMap<>();

	static {
		Map<Integer, String> map;
		
		map = new HashMap<>();
		map.put(1, "floor"); // TODO verify this for indy!
		map.put(2, "obstacle"); // TODO verify this for indy!
		map.put(6, "pushable"); // TODO verify this for indy!
		map.put(8, "ceiling"); // TODO verify this for indy!
		map.put(16, "map"); // TODO verify this for indy!
		map.put(32, "weapon"); // TODO verify this for indy!
		map.put(64, "item"); // TODO verify this for indy!
		map.put(128, "character"); // TODO verify this for indy!
		mapping.put("tile.type", map);

		map = new HashMap<>();
		map.put(0, "");
		map.put(1, "UNK1"); // TODO Might be ai related, verify if AI does not move on this floor.
		mapping.put("tile.subtype-floor", map);
		
		map = new HashMap<>();
		map.put(0, "");
		mapping.put("tile.subtype-obstacle", map);

		map = new HashMap<>();
		map.put(0, "");
		mapping.put("tile.subtype-pushable", map);

		map = new HashMap<>();
		map.put(0, "");
		mapping.put("tile.subtype-ceiling", map);

		map = new HashMap<>(); // TODO teleporter is missing...
		map.put(1, "empty");
		map.put(2, "town");
		map.put(4, "puzzle_unsolved");
		map.put(8, "puzzle_solved");
		map.put(16, "transport_unsolved");
		map.put(32, "transport_solved");
		map.put(64, "blockade_north_unsolved");
		map.put(128, "blockade_south_unsolved");
		map.put(256, "blockade_west_unsolved");
		map.put(512, "blockade_east_unsolved");
		map.put(1024, "blockade_north_solved");
		map.put(2048, "blockade_south_solved");
		map.put(4096, "blockade_west_solved");
		map.put(8192, "blockade_east_solved");
		map.put(16384, "final"); // unsolved and solved are sharing this one
		map.put(-32768, "current");
		mapping.put("tile.subtype-map", map);

		map = new HashMap<>();
		map.put(1, "light");
		map.put(2, "heavy");
		map.put(4, "melee");
		map.put(8, "special"); // TODO force and whip - can_trigger ?
		mapping.put("tile.subtype-weapon", map);

		map = new HashMap<>(); // TODO verify these for indy!
		map.put(1, "player");
		map.put(2, "enemy");
		map.put(4, "ally");
		mapping.put("tile.subtype-character", map);

		map = new HashMap<>();
		map.put(1, "key");
		map.put(2, "tool");
		map.put(4, "part");
		map.put(8, "UNK8"); // TODO is this default? Or is this a category?
		map.put(16, "map");
		map.put(32, "UNK32"); // TODO Check what whiskey does in indy. Check if it works in yoda too. poison?
		map.put(64, "health");
		mapping.put("tile.subtype-item", map);

		map = new HashMap<>();
		map.put(1, "empty");
		map.put(2, "blockade_north");
		map.put(3, "blockade_south");
		map.put(4, "blockade_east");
		map.put(5, "blockade_west");
		map.put(6, "transport_start");
		map.put(7, "transport_end");
		map.put(8, "is_subzone");
		map.put(9, "static_loading");
		map.put(10, "final");
		map.put(11, "town");
		map.put(13, "static_win");
		map.put(14, "static_loose");
		map.put(15, "puzzle_trade");
		map.put(16, "puzzle_use");
		map.put(17, "puzzle_find");
		map.put(18, "puzzle_force");
		mapping.put("zone.type", map);

		map = new HashMap<>();
		map.put(1, "tatooine");
		map.put(2, "neshtab");
		map.put(3, "endor");
		map.put(5, "dagobah");
		mapping.put("zone.world", map);
    }
	
	public static String getType(String map, int type) {
		return mapping.get(map).get(type);
	}
	
	public static Short getType(String map, String type) {
		for (Entry<Integer, String> entry : mapping.get(map).entrySet()) {
			if (entry.getValue().equals(type)) {
				return entry.getKey().shortValue();
			}
		}
		
		return null;
	}
}
