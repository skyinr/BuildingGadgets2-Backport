package com.direwolf20.buildinggadgets2.client.events;

import com.direwolf20.buildinggadgets2.client.KeyBindings;
import com.direwolf20.buildinggadgets2.client.screen.DestructionGUI;
import com.direwolf20.buildinggadgets2.client.screen.ModeRadialMenu;
import com.direwolf20.buildinggadgets2.common.items.BaseGadget;
import com.direwolf20.buildinggadgets2.common.items.GadgetDestruction;
import com.direwolf20.buildinggadgets2.common.network.data.AnchorPayload;
import com.direwolf20.buildinggadgets2.common.network.data.RangeChangePayload;
import com.direwolf20.buildinggadgets2.common.network.data.UndoPayload;
import com.direwolf20.buildinggadgets2.util.GadgetNBT;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class EventKeyInput {

    public static void handleEventInput(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;

        ItemStack tool = BaseGadget.getGadget(mc.thePlayer);
        if (tool == null)
            return;

        KeyMapping mode = KeyBindings.menuSettings;
        if (!(mc.screen instanceof ModeRadialMenu) && mode.consumeClick() && ((mode.getKeyModifier() == KeyModifier.NONE
            && KeyModifier.getActiveModifier() == KeyModifier.NONE) || mode.getKeyModifier() != KeyModifier.NONE)) {
            if (tool.getItem() instanceof GadgetDestruction)
                mc.setScreen(new DestructionGUI(tool, true));
            else
                mc.setScreen(new ModeRadialMenu(tool));
        } else if (KeyBindings.undo.consumeClick()) {
            PacketDistributor.sendToServer(new UndoPayload());
        } else if (KeyBindings.anchor.consumeClick()) {
            PacketDistributor.sendToServer(new AnchorPayload());
        } else if (KeyBindings.range.consumeClick()) {
            int oldRange = GadgetNBT.getToolRange(tool);
            int newRange = oldRange + 1 > 15 ? 1 : oldRange + 1;
            PacketDistributor.sendToServer(new RangeChangePayload(newRange));
        }
    }
}
