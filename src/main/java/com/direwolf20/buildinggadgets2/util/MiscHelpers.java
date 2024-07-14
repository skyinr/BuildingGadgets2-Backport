package com.direwolf20.buildinggadgets2.util;

import net.minecraft.entity.player.EntityPlayerMP;

public class MiscHelpers {
    public static void playSound(EntityPlayerMP player, String  soundName) {
        // Get player's position

        double x = player.serverPosX;
        double y = player.serverPosY;
        double z = player.serverPosZ;

        player.getServerForPlayer().playSound(
            x,y,z,soundName,1,1,true
        );

        //—————Old Code—————
//        // Create the packet
//        ClientboundSoundPacket packet = new ClientboundSoundPacket(
//                soundEventHolder, // The sound event
//                SoundSource.MASTER, // The sound category
//                x, y, z, // The sound location
//                1, // The volume, 1 is normal, higher is louder
//                1, // The pitch, 1 is normal, higher is higher pitch
//                1 // A random for some reason? (Some sounds have different variants, like the enchanting table success
//        );

        // Send the packet to the player
//        player.connection.send(packet);
    }
}
