package com.direwolf20.buildinggadgets2.client.screen;

import com.direwolf20.buildinggadgets2.client.KeyBindings;
import com.direwolf20.buildinggadgets2.client.screen.widgets.GuiIconActionable;
import com.direwolf20.buildinggadgets2.client.screen.widgets.IncrementalSliderWidget;
import com.direwolf20.buildinggadgets2.common.network.data.*;
import com.direwolf20.buildinggadgets2.util.GadgetNBT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class DestructionGUI extends GuiScreen {
    private final Set<IncrementalSliderWidget> sliders = new HashSet<>();

    private IncrementalSliderWidget left;
    private IncrementalSliderWidget right;
    private IncrementalSliderWidget up;
    private IncrementalSliderWidget down;
    private IncrementalSliderWidget depth;

    private int originalLeft;
    private int originalRight;
    private int originalUp;
    private int originalDown;
    private int originalDepth;

    private int confirm;

    private String sizeString = "";
    private boolean isValidSize = true;
    private boolean keyDown = false;

    private final ItemStack destructionGadget;

    private int renderTypeButton;
    private GadgetNBT.RenderTypes renderType;

    public DestructionGUI(ItemStack tool, boolean keyDown) {
        this.destructionGadget = tool;
        this.keyDown = keyDown;
        renderType = GadgetNBT.getRenderType(tool);
    }


    @Override
    public void initGui() {
        super.initGui();

        int x = width / 2;
        int y = height / 2;

        /*if (!keyDown)
            this.addRenderableWidget(confirm = Button.builder(Component.translatable("buildinggadgets2.screen.close"), b -> {
                        this.onClose();
                    })
                    .pos((x - 30) - 32, y + 65)
                    .size(60, 20)
                    .build());*/

        this.drawGradientRect(originalLeft,
                originalUp,
                originalRight,
                confirm,

                depth.setValue(originalDepth);
                right.setValue(originalRight);
                left.setValue(originalLeft);
                up.setValue(originalUp);
                down.setValue(originalDown);
                sendPacket();
            )
            .pos((x - 30) + 32, y + 65)
            .size(60, 20)
            .build());

        Button undo_button = new GuiIconActionable(x - 55, y - 75, "undo", Component.translatable("buildinggadgets2.radialmenu.undo"), false, send -> {
            if (send) {
                PacketDistributor.sendToServer(new UndoPayload());
            }

            return false;
        });
        this.addRenderableWidget(undo_button);

        Button anchorButton = new GuiIconActionable(x - 25, y - 75, "anchor", Component.translatable("buildinggadgets2.radialmenu.anchor"), true, send -> {
            if (send) {
                PacketDistributor.sendToServer(new AnchorPayload());
            }

            return !GadgetNBT.getAnchorPos(destructionGadget).equals(GadgetNBT.nullPos);
        });
        this.addRenderableWidget(anchorButton);

        Button affectTiles = new GuiIconActionable(x + 5, y - 75, "affecttiles", Component.translatable("buildinggadgets2.screen.affecttiles"), true, send -> {
            if (send) {
                PacketDistributor.sendToServer(new ToggleSettingPayload(GadgetNBT.ToggleableSettings.AFFECT_TILES.getName()));
            }

            return GadgetNBT.getSetting(destructionGadget, GadgetNBT.ToggleableSettings.AFFECT_TILES.getName());
        });
        this.addRenderableWidget(affectTiles);

        Button rayTrace = new GuiIconActionable(x + 35, y - 75, "raytrace_fluid", Component.translatable("buildinggadgets2.radialmenu.raytracefluids"), true, send -> {
            if (send) {
                PacketDistributor.sendToServer(new ToggleSettingPayload(GadgetNBT.ToggleableSettings.RAYTRACE_FLUID.getName()));
            }

            return GadgetNBT.getSetting(destructionGadget, GadgetNBT.ToggleableSettings.RAYTRACE_FLUID.getName());
        });
        this.addRenderableWidget(rayTrace);

        renderTypeButton = new GuiIconActionable(x + 65, y - 75, "raytrace_fluid", Component.translatable(renderType.getLang()), false, send -> {
            if (send) {
                renderType = renderType.next();
                renderTypeButton.setMessage(Component.translatable(renderType.getLang()));
                PacketDistributor.sendToServer(new RenderChangePayload(renderType.getPosition()));
            }

            return false;
        });
        this.addRenderableWidget(renderTypeButton);

        sliders.clear();
        sliders.add(depth = this.createSlider(x - (70 / 2), y - (14 / 2), Component.translatable("buildinggadgets2.screen.depth"), GadgetNBT.getToolValue(destructionGadget, GadgetNBT.IntSettings.DEPTH.getName())));
        sliders.add(right = this.createSlider(x + (70 + 5), y - (14 / 2), Component.translatable("buildinggadgets2.screen.right"), GadgetNBT.getToolValue(destructionGadget, GadgetNBT.IntSettings.RIGHT.getName())));
        sliders.add(left = this.createSlider(x - (70 * 2) - 5, y - (14 / 2), Component.translatable("buildinggadgets2.screen.left"), GadgetNBT.getToolValue(destructionGadget, GadgetNBT.IntSettings.LEFT.getName())));
        sliders.add(up = this.createSlider(x - (70 / 2), y - 35, Component.translatable("buildinggadgets2.screen.up"), GadgetNBT.getToolValue(destructionGadget, GadgetNBT.IntSettings.UP.getName())));
        sliders.add(down = this.createSlider(x - (70 / 2), y + 20, Component.translatable("buildinggadgets2.screen.down"), GadgetNBT.getToolValue(destructionGadget, GadgetNBT.IntSettings.DOWN.getName())));

        originalDepth = depth.getValueInt();
        originalLeft = left.getValueInt();
        originalRight = right.getValueInt();
        originalUp = up.getValueInt();
        originalDown = down.getValueInt();

        updateSizeString();
        updateIsValid();

        // Adds their buttons to the gui
        sliders.forEach(gui -> gui.getComponents().forEach(this::addRenderableWidget));
    }

    public IncrementalSliderWidget createSlider(int x, int y, MutableComponent prefix, int value) {
        return new IncrementalSliderWidget(x, y, 70, 14, 0D, 16D, prefix.append(": "), value, this::onSliderUpdate);
    }

    public void onSliderUpdate(IncrementalSliderWidget widget) {
        this.updateSizeString();
        this.updateIsValid();
        sendPacket();
    }

    private boolean isWithinBounds() {
        int x = left.getValueInt() + right.getValueInt();
        int y = up.getValueInt() + down.getValueInt();
        int z = depth.getValueInt();
        int dim = 16;

        return x <= 32 && y <= 32 && z <= 16;
    }

    private String getSizeString() {
        int x = 1 + left.getValueInt() + right.getValueInt();
        int y = 1 + up.getValueInt() + down.getValueInt();
        int z = depth.getValueInt();

        return String.format("%d x %d x %d", x, y, z);
    }

    private void updateIsValid() {
        this.isValidSize = isWithinBounds();
    }

    private void updateSizeString() {
        this.sizeString = getSizeString();
    }

    private void sendPacket() {
        if (isWithinBounds()) {
            PacketDistributor.sendToServer(new DestructionRangesPayload(left.getValueInt(), right.getValueInt(), up.getValueInt(), down.getValueInt(), depth.getValueInt()));
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawCenteredString(font, this.sizeString, width / 2, (height / 2) + 40, this.isValidSize ? 0x00FF00 : 0xFF2000);
        if (!this.isValidSize) {
            drawCenteredString(font, "buildinggadgets2.screen.destructiontoolarge", width / 2, (height / 2) + 50, 0xFF2000);
        }
    }

    @Override
    public void updateScreen() {
        if (keyDown && !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeyBindings.menuSettings.getKey().getValue())) {
            onClose();
        }
        super.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


//    @Override
//    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
//        if (keyDown)
//            return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
//        InputConstants.Key mouseKey = InputConstants.getKey(p_keyPressed_1_, p_keyPressed_2_);
//        if (p_keyPressed_1_ == 256 || minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
//            onClose();
//            return true;
//        }
//
//        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
//    }

}
