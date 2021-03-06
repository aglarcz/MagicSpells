package com.nisovin.magicspells.spells.passive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spellbook;
import com.nisovin.magicspells.spells.PassiveSpell;
import com.nisovin.magicspells.util.OverridePriority;
import com.nisovin.magicspells.util.Util;

// trigger variable is optional
// if not specified, it will trigger on any entity type
// if specified, it should be a comma separated list of entity types to trigger on
public class KillListener extends PassiveListener {

	Map<EntityType, List<PassiveSpell>> entityTypes = new HashMap<EntityType, List<PassiveSpell>>();
	List<PassiveSpell> allTypes = new ArrayList<PassiveSpell>();
	
	@Override
	public void registerSpell(PassiveSpell spell, PassiveTrigger trigger, String var) {
		if (var == null || var.isEmpty()) {
			allTypes.add(spell);
		} else {
			String[] split = var.replace(" ", "").split(",");
			for (String s : split) {
				EntityType t = Util.getEntityType(s);
				if (t != null) {
					List<PassiveSpell> spells = entityTypes.get(t);
					if (spells == null) {
						spells = new ArrayList<PassiveSpell>();
						entityTypes.put(t, spells);
					}
					spells.add(spell);
				}
			}
		}
	}
	
	@OverridePriority
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		Player killer = event.getEntity().getKiller();
		if (killer != null) {
			Spellbook spellbook = MagicSpells.getSpellbook(killer);
			if (allTypes.size() > 0) {
				for (PassiveSpell spell : allTypes) {
					if (spellbook.hasSpell(spell)) {
						spell.activate(killer, event.getEntity());
					}
				}
			}
			if (entityTypes.containsKey(event.getEntityType())) {
				List<PassiveSpell> list = entityTypes.get(event.getEntityType());
				for (PassiveSpell spell : list) {
					if (spellbook.hasSpell(spell)) {
						spell.activate(killer, event.getEntity());
					}
				}
			}
		}
	}
	
}
