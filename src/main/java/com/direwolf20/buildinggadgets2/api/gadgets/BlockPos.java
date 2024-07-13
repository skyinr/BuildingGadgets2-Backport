package com.direwolf20.buildinggadgets2.api.gadgets;

public record BlockPos(int x, int y, int z) {
    public static final BlockPos ZERO = new BlockPos(0, 0, 0);

    public BlockPos moveX(int x) {
        return new BlockPos(x() + x, y(), z());
    }

    public BlockPos moveY(int y) {
        return new BlockPos(x(), y() + y, z());
    }

    public BlockPos moveZ(int z) {
        return new BlockPos(x(), y(), z() + z);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockPos blockPos) {
            return x() == blockPos.x() &&
                y() == blockPos.y() &&
                z() == blockPos.z();
        }
        return false;
    }
}
