package com.kennycason.kumo.draw;

import java.io.IOException;
import java.io.OutputStream;

public interface IImageWriter {

    void write(IImage image, String format, OutputStream outputStream) throws IOException;

}
