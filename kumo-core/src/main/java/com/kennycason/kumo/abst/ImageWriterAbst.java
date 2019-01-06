package com.kennycason.kumo.abst;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ImageWriterAbst {
    public abstract void write(ImageAbst image, String format, OutputStream outputStream) throws IOException;
}
