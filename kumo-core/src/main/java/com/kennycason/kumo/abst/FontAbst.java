package com.kennycason.kumo.abst;

public abstract class FontAbst<IMPL> implements VaryingImpl<IMPL>{

    public abstract void registerIfNecessary();

    public abstract FontAbst withSize(float size);

    public enum Face {
        PLAIN,
        BOLD,
        ITALIC
    }
}
