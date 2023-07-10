package net.optifine.entity.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.optifine.Config;

public class CustomModelRegistry
{
    private static Map<String, ModelAdapter> mapModelAdapters = makeMapModelAdapters();

    private static Map<String, ModelAdapter> makeMapModelAdapters()
    {
        Map<String, ModelAdapter> map = new LinkedHashMap<String, ModelAdapter>();
        addModelAdapter(map, new ModelAdapterArmorStand());
        addModelAdapter(map, new ModelAdapterBat());
        addModelAdapter(map, new ModelAdapterBlaze());
        addModelAdapter(map, new ModelAdapterBoat());
        addModelAdapter(map, new ModelAdapterCaveSpider());
        addModelAdapter(map, new ModelAdapterChicken());
        addModelAdapter(map, new ModelAdapterCow());
        addModelAdapter(map, new ModelAdapterCreeper());
        addModelAdapter(map, new ModelAdapterDragon());
        addModelAdapter(map, new ModelAdapterDonkey());
        addModelAdapter(map, new ModelAdapterEnderCrystal());
        addModelAdapter(map, new ModelAdapterEnderCrystalNoBase());
        addModelAdapter(map, new ModelAdapterEnderman());
        addModelAdapter(map, new ModelAdapterEndermite());
        addModelAdapter(map, new ModelAdapterEvoker());
        addModelAdapter(map, new ModelAdapterEvokerFangs());
        addModelAdapter(map, new ModelAdapterGhast());
        addModelAdapter(map, new ModelAdapterGuardian());
        addModelAdapter(map, new ModelAdapterHorse());
        addModelAdapter(map, new ModelAdapterHusk());
        addModelAdapter(map, new ModelAdapterIronGolem());
        addModelAdapter(map, new ModelAdapterLeadKnot());
        addModelAdapter(map, new ModelAdapterLlama());
        addModelAdapter(map, new ModelAdapterMagmaCube());
        addModelAdapter(map, new ModelAdapterMinecart());
        addModelAdapter(map, new ModelAdapterMinecartTnt());
        addModelAdapter(map, new ModelAdapterMinecartMobSpawner());
        addModelAdapter(map, new ModelAdapterMooshroom());
        addModelAdapter(map, new ModelAdapterMule());
        addModelAdapter(map, new ModelAdapterOcelot());
        addModelAdapter(map, new ModelAdapterParrot());
        addModelAdapter(map, new ModelAdapterPig());
        addModelAdapter(map, new ModelAdapterPigZombie());
        addModelAdapter(map, new ModelAdapterPolarBear());
        addModelAdapter(map, new ModelAdapterRabbit());
        addModelAdapter(map, new ModelAdapterSheep());
        addModelAdapter(map, new ModelAdapterShulker());
        addModelAdapter(map, new ModelAdapterShulkerBullet());
        addModelAdapter(map, new ModelAdapterSilverfish());
        addModelAdapter(map, new ModelAdapterSkeleton());
        addModelAdapter(map, new ModelAdapterSkeletonHorse());
        addModelAdapter(map, new ModelAdapterSlime());
        addModelAdapter(map, new ModelAdapterSnowman());
        addModelAdapter(map, new ModelAdapterSpider());
        addModelAdapter(map, new ModelAdapterSquid());
        addModelAdapter(map, new ModelAdapterStray());
        addModelAdapter(map, new ModelAdapterVex());
        addModelAdapter(map, new ModelAdapterVillager());
        addModelAdapter(map, new ModelAdapterVindicator());
        addModelAdapter(map, new ModelAdapterWitch());
        addModelAdapter(map, new ModelAdapterWither());
        addModelAdapter(map, new ModelAdapterWitherSkeleton());
        addModelAdapter(map, new ModelAdapterWitherSkull());
        addModelAdapter(map, new ModelAdapterWolf());
        addModelAdapter(map, new ModelAdapterZombie());
        addModelAdapter(map, new ModelAdapterZombieHorse());
        addModelAdapter(map, new ModelAdapterZombieVillager());
        addModelAdapter(map, new ModelAdapterSheepWool());
        addModelAdapter(map, new ModelAdapterBanner());
        addModelAdapter(map, new ModelAdapterBed());
        addModelAdapter(map, new ModelAdapterBook());
        addModelAdapter(map, new ModelAdapterChest());
        addModelAdapter(map, new ModelAdapterChestLarge());
        addModelAdapter(map, new ModelAdapterEnderChest());
        addModelAdapter(map, new ModelAdapterHeadDragon());
        addModelAdapter(map, new ModelAdapterHeadHumanoid());
        addModelAdapter(map, new ModelAdapterHeadSkeleton());
        addModelAdapter(map, new ModelAdapterShulkerBox());
        addModelAdapter(map, new ModelAdapterSign());
        return map;
    }

    private static void addModelAdapter(Map<String, ModelAdapter> map, ModelAdapter modelAdapter)
    {
        addModelAdapter(map, modelAdapter, modelAdapter.getName());
        String[] astring = modelAdapter.getAliases();

        if (astring != null)
        {
            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                addModelAdapter(map, modelAdapter, s);
            }
        }

        ModelBase modelbase = modelAdapter.makeModel();
        String[] astring1 = modelAdapter.getModelRendererNames();

        for (int j = 0; j < astring1.length; ++j)
        {
            String s1 = astring1[j];
            ModelRenderer modelrenderer = modelAdapter.getModelRenderer(modelbase, s1);

            if (modelrenderer == null)
            {
                Config.warn("Model renderer not found, model: " + modelAdapter.getName() + ", name: " + s1);
            }
        }
    }

    private static void addModelAdapter(Map<String, ModelAdapter> map, ModelAdapter modelAdapter, String name)
    {
        if (map.containsKey(name))
        {
            Config.warn("Model adapter already registered for id: " + name + ", class: " + modelAdapter.getEntityClass().getName());
        }

        map.put(name, modelAdapter);
    }

    public static ModelAdapter getModelAdapter(String name)
    {
        return mapModelAdapters.get(name);
    }

    public static String[] getModelNames()
    {
        Set<String> set = mapModelAdapters.keySet();
        String[] astring = (String[])set.toArray(new String[set.size()]);
        return astring;
    }
}
