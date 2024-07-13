package com.direwolf20.buildinggadgets2.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;

public interface OurSounds {
    static void playSound(ISound sound, int pitch) {
        Minecraft.getMinecraft().getSoundHandler().playDelayedSound(sound, pitch);
    }

    static void playSound(ISound sound) {
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
    }
}
