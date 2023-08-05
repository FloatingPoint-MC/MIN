package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class VillagerTradeTrigger implements ICriterionTrigger<VillagerTradeTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("villager_trade");
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners villagertradetrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (villagertradetrigger$listeners == null)
        {
            villagertradetrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, villagertradetrigger$listeners);
        }

        villagertradetrigger$listeners.add(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners villagertradetrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (villagertradetrigger$listeners != null)
        {
            villagertradetrigger$listeners.remove(listener);

            if (villagertradetrigger$listeners.isEmpty())
            {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.listeners.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("villager"));
        ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        return new Instance(entitypredicate, itempredicate);
    }

    public void trigger(EntityPlayerMP player, EntityVillager villager, ItemStack item)
    {
        Listeners villagertradetrigger$listeners = this.listeners.get(player.getAdvancements());

        if (villagertradetrigger$listeners != null)
        {
            villagertradetrigger$listeners.trigger(player, villager, item);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate villager;
        private final ItemPredicate item;

        public Instance(EntityPredicate villager, ItemPredicate item)
        {
            super(VillagerTradeTrigger.ID);
            this.villager = villager;
            this.item = item;
        }

        public boolean test(EntityPlayerMP player, EntityVillager villager, ItemStack item)
        {
            if (!this.villager.test(player, villager))
            {
                return false;
            }
            else
            {
                return this.item.test(item);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn)
        {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty()
        {
            return this.listeners.isEmpty();
        }

        public void add(Listener<Instance> listener)
        {
            this.listeners.add(listener);
        }

        public void remove(Listener<Instance> listener)
        {
            this.listeners.remove(listener);
        }

        public void trigger(EntityPlayerMP player, EntityVillager villager, ItemStack item)
        {
            List<Listener<Instance>> list = null;

            for (Listener<Instance> listener : this.listeners)
            {
                if (listener.getCriterionInstance().test(player, villager, item))
                {
                    if (list == null)
                    {
                        list = Lists.newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (Listener<Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}
