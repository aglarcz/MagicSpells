package com.nisovin.magicspells.spells.passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spellbook;
import com.nisovin.magicspells.spells.PassiveSpell;
import com.nisovin.magicspells.util.OverridePriority;

// no trigger variable is used here
public class ShootListener extends PassiveListener {

	List<PassiveSpell> spells = new ArrayList<PassiveSpell>();

	@Override
	public void registerSpell(PassiveSpell spell, PassiveTrigger trigger, String var) {
		spells.add(spell);
	}
	
	@OverridePriority
	@EventHandler
	public void onShoot(final EntityShootBowEvent event) {
		if (spells.size() > 0 && event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			Spellbook spellbook = MagicSpells.getSpellbook(player);
			for (PassiveSpell spell : spells) {
				if (!isCancelStateOk(spell, event.isCancelled())) continue;
				if (spellbook.hasSpell(spell)) {
					boolean casted = spell.activate(player, event.getForce());
					if (PassiveListener.cancelDefaultAction(spell, casted)) {
						event.setCancelled(true);
						event.getProjectile().remove();
					}
				}
			}
		}
	}
}
