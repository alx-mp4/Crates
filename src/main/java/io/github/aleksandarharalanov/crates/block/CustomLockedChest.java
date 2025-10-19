package io.github.aleksandarharalanov.crates.block;

import net.minecraft.server.Block;
import net.minecraft.server.BlockLockedChest;
import net.minecraft.server.StepSound;
import net.minecraft.server.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class CustomLockedChest extends BlockLockedChest {

    public CustomLockedChest(int var1) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
        super(var1);

        Method m = Block.class.getDeclaredMethod("c", float.class);
        m.setAccessible(true);
        m.invoke(this, 0.0F);

        m = Block.class.getDeclaredMethod("a", float.class);
        m.setAccessible(true);
        m.invoke(this, 1.0F);

        m = Block.class.getDeclaredMethod("a", StepSound.class);
        m.setAccessible(true);
        m.invoke(this, Block.e);

        m = Block.class.getDeclaredMethod("a", String.class);
        m.setAccessible(true);
        m.invoke(this, "lockedchest");

        m = Block.class.getDeclaredMethod("a", boolean.class);
        m.setAccessible(true);
        m.invoke(this, Boolean.TRUE);

        m = Block.class.getDeclaredMethod("g");
        m.setAccessible(true);
        m.invoke(this);
    }

    @Override
    public void a(World var1, int var2, int var3, int var4, Random var5) {}
}
