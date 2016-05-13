package me.jtjj222.trading;

import java.io.IOException;
import net.minecraft.server.v1_8_R3.*;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 
 * This software is part of VillagerTradingLib
 * 
 * @author Justin Michaud (jtjj222)
 * Email : Jtjj222@live.ca
 * Bukkit.org username: jtjj222
 * 
 * This Library allows plugin developers to quickly and easily
 * make  custom trades with  villagers, without mingling messy
 * netMincraftServer code into their plugins.
 * 
 * VillagerTradingLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * VillagerTradingLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VillagerTradingLib.  If not, see http://www.gnu.org/licenses/.
 * 
 */
public class VillagerTrading {
/*
	public static void openTrade(Player player, VillagerTradeOffer[] offers) throws IOException {
		CustomMerchant merch = new CustomMerchant(offers);
		merch.a_(((CraftPlayer) player).getHandle());
		((CraftPlayer) player).getHandle().openTrade(merch, "Buy");
	}

	public static class CustomMerchant implements IMerchant {

		private VillagerTradeOffer[] offers;
		private EntityHuman tradingWith = null;

		public CustomMerchant(VillagerTradeOffer[] offers) {
			this.offers = offers;
		}

		public void a(MerchantRecipe arg0) {

		}

		public void a_(EntityHuman arg0) {
			this.tradingWith = arg0;
		}

		public void a_(net.minecraft.server.v1_8_R3.ItemStack arg0) {

		}

		public MerchantRecipeList getOffers(EntityHuman arg0) {
			return getMerchantRecipeList(offers);
		}

		public EntityHuman m_() {
			return tradingWith;
		}

		private MerchantRecipeList getMerchantRecipeList(VillagerTradeOffer[] offers) {
			MerchantRecipeList list = new MerchantRecipeList();

			for (VillagerTradeOffer offer : offers) {
				list.a(offer.getMerchantRecipie());
			}
			return list;
		}

	}
	
	public static class VillagerTradeOffer {

		private final ItemStack offer, cost1, cost2;
		
		
		public VillagerTradeOffer(ItemStack cost, ItemStack offer) {
			this(cost, null, offer);
		}
		
		public VillagerTradeOffer(ItemStack cost1, ItemStack cost2, ItemStack offer) {
			this.offer = offer;
			this.cost1 = cost1;
			this.cost2 = cost2;
		}
		
		public MerchantRecipe getMerchantRecipie() {
			
			if (cost1 == null || offer == null) throw new IllegalArgumentException();
			
			if (cost2 == null) return new MerchantRecipe(getNMSCost1(), getNMSOffer());
			else  return new MerchantRecipe(getNMSCost1(), getNMSCost2(), getNMSOffer());
		}
		
		public net.minecraft.server.v1_8_R3.ItemStack getNMSCost1() {
			return convertItemStackToNMS(cost1);
		}
		

		public net.minecraft.server.v1_8_R3.ItemStack getNMSCost2() {
			return convertItemStackToNMS(cost2);
		}
		

		public net.minecraft.server.v1_8_R3.ItemStack getNMSOffer() {
			return convertItemStackToNMS(this.offer);
		}
		

		public static net.minecraft.server.v1_8_R3.ItemStack convertItemStackToNMS(ItemStack i) {
			int amount = i.getAmount();
			int id = i.getType().getId();
			short durability = i.getDurability();
			
			return new net.minecraft.server.v1_8_R3.ItemStack(id,amount, (int)durability);
		}	
	}*/
}
