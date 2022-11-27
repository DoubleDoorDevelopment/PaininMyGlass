package net.doubledoordev.paininmyglass;

import java.util.List;
import java.util.Random;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod("paininmyglass")
public class Paininmyglass
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    Random random;

    public Paininmyglass()
    {

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PaininMyGlassConfig.spec);
        MinecraftForge.EVENT_BUS.register(this);

        random = new Random();

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () ->
                new IExtensionPoint.DisplayTest(() -> "Forge making stupid decisions 101. Morons", (remote, isServer) -> true));
    }

    @SubscribeEvent
    public void closeContainer(PlayerContainerEvent.Close event)
    {
        if (PaininMyGlassConfig.SERVER.isItOn.get())
        {
            NonNullList<Slot> slots = event.getContainer().slots;
            List<? extends String> nameOptions = PaininMyGlassConfig.SERVER.itemNames.get();

            for (Slot slot : slots)
            {
                if (!slot.hasItem() && random.nextDouble() < PaininMyGlassConfig.SERVER.chanceForPane.get())
                    slot.set(new ItemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE).setHoverName(Component.literal(nameOptions.get(random.nextInt(nameOptions.size())))));
            }
        }
    }
}
