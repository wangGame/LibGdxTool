package com.kw.gdx.texturepacker;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * Desktop-only JPEG writer. Put this file in your desktop module, not Android/core.
 */
public class DesktopJpegPageWriter implements PageWriter {
    @Override
    public void write(FileHandle file, Pixmap pixmap, TexturePackerOptions options) throws Exception {
        if (!options.wantsJpeg()) {
            com.badlogic.gdx.graphics.PixmapIO.writePNG(file, pixmap);
            return;
        }

        BufferedImage image = new BufferedImage(pixmap.getWidth(), pixmap.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int c = pixmap.getPixel(x, y);
                int r = (c >>> 24) & 255;
                int g = (c >>> 16) & 255;
                int b = (c >>> 8) & 255;
                image.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) throw new IllegalStateException("No JPEG ImageIO writer found.");
        ImageWriter writer = writers.next();
        ImageOutputStream output = null;
        try {
            output = ImageIO.createImageOutputStream(file.file());
            writer.setOutput(output);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(Math.max(0f, Math.min(1f, options.jpegQuality)));
            }
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
            if (output != null) output.close();
        }
    }
}
