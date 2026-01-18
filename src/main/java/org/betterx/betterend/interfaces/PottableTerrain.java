package org.betterx.betterend.interfaces;

public interface PottableTerrain {
    default boolean canBePotted() {
        return true;
    }
}
