package io.github.aleksandarharalanov.crates.ui;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

public final class SimpleInventory implements IInventory {

    private final String title;
    private final ItemStack[] contents;
    private final int maxStack = 64;

    public SimpleInventory(String title, int size) {
        this.title = title;
        this.contents = new ItemStack[size];
    }

    public void a() {}

    public boolean a(EntityHuman human) { return true; }

    public int getSize() { return contents.length; }

    public ItemStack getItem(int i) { return (i >= 0 && i < contents.length) ? contents[i] : null; }

    public ItemStack splitStack(int i, int amount) {
        if (i < 0 || i >= contents.length) return null;
        ItemStack s = contents[i];
        if (s == null) return null;
        if (s.count <= amount) {
            contents[i] = null;
            return s;
        }
        ItemStack part = new ItemStack(s.id, amount, s.getData());
        s.count -= amount;
        if (s.count <= 0) contents[i] = null;
        return part;
    }

    public void setItem(int i, ItemStack s) {
        if (i < 0 || i >= contents.length) return;
        contents[i] = s;
        if (s != null && s.count > maxStack) s.count = maxStack;
    }

    public String getName() { return title; }

    public int getMaxStackSize() { return maxStack; }

    @Override
    public void update() {}

    @Override
    public boolean a_(EntityHuman entityHuman) {
        return false;
    }

    @Override
    public ItemStack[] getContents() {
        return new ItemStack[0];
    }
}
