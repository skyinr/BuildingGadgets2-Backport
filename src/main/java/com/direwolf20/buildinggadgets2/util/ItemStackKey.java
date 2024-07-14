package com.direwolf20.buildinggadgets2.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class ItemStackKey {
    public final Item item;
    public final NBTTagCompound nbtTagCompound;
    private final int hash;


    public ItemStackKey(ItemStack stack, boolean compareNBT) {
        this.item = stack.getItem();
        this.nbtTagCompound = compareNBT ? stack.getTagCompound() : new NBTTagCompound();
        this.hash = Objects.hash(item, nbtTagCompound);
    }

    public ItemStack getStack() {
        ItemStack itemStack = new ItemStack(item, 1);
        itemStack.setTagCompound(nbtTagCompound);
        return itemStack;
    }

    public ItemStack getStack(int amt) {
        ItemStack itemStack = new ItemStack(item, amt);
        itemStack.setTagCompound(nbtTagCompound);
        return itemStack;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStackKey) {
            return (((ItemStackKey) obj).item == this.item) && Objects.equals(((ItemStackKey) obj).nbtTagCompound, this.nbtTagCompound);
        }
        return false;
    }
}
