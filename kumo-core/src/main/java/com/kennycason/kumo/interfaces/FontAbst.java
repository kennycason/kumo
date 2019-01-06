package com.kennycason.kumo.interfaces;

public abstract class FontAbst<IMPL> implements VaryingImpl<IMPL>{

    public abstract void registerIfNecessary();

    public abstract FontAbst withSize(float size);

    public enum Face {
        PLAIN,
        BOLD,
        ITALIC
    }
}
