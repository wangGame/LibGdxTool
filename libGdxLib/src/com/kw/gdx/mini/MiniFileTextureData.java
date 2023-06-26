package com.kw.gdx.mini;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MiniFileTextureData implements TextureData {
    static public boolean copyToPOT;

    final FileHandle file;
    int width = 0;
    int height = 0;
    Format format;
    Pixmap pixmap;
    boolean useMipMaps;
    boolean isPrepared = false;

    final float scale;

    public MiniFileTextureData(FileHandle file, Pixmap preloadedPixmap, Format format, boolean useMipMaps, float scale) {
        this.file = file;
        this.pixmap = preloadedPixmap;
        this.format = format;
        this.useMipMaps = useMipMaps;
        this.scale = scale;
        if (pixmap != null) {
            pixmap = ensurePot(pixmap);
            width = pixmap.getWidth();
            height = pixmap.getHeight();
            if (format == null)
                this.format = pixmap.getFormat();
        }
    }

    @Override
    public boolean isPrepared() {
        return isPrepared;
    }

    @Override
    public void prepare() {
        if (isPrepared)
            throw new GdxRuntimeException("Already prepared");
        if (pixmap == null) {
            if (file.extension().equals("cim"))
                pixmap = PixmapIO.readCIM(file);
            else
                pixmap = ensurePot(new Pixmap(file));
            width = pixmap.getWidth();
            height = pixmap.getHeight();
            if (format == null)
                format = pixmap.getFormat();
        }
        isPrepared = true;
    }

    private Pixmap ensurePot(Pixmap pixmap) {
//		if (Gdx.gl20 == null && copyToPOT) {
//			int pixmapWidth = pixmap.getWidth();
//			int pixmapHeight = pixmap.getHeight();
//			int potWidth = MathUtils.nextPowerOfTwo(pixmapWidth);
//			int potHeight = MathUtils.nextPowerOfTwo(pixmapHeight);
//			if (pixmapWidth != potWidth || pixmapHeight != potHeight) {
//				Pixmap tmp = new Pixmap(potWidth, potHeight, pixmap.getFormat());
//				tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmapWidth, pixmapHeight);
//				pixmap.dispose();
//				return tmp;
//			}
//		}

        int pixmapWidth = pixmap.getWidth();
        int pixmapHeight = pixmap.getHeight();
        int scaleWidth = Math.round(pixmapWidth * scale);
        int scaleHeight = Math.round(pixmapHeight * scale);

        int potWidth;
        int potHeight;
        if (Gdx.gl20 == null && copyToPOT) {

            potWidth = MathUtils.nextPowerOfTwo(scaleWidth);
            potHeight = MathUtils.nextPowerOfTwo(scaleHeight);
        } else {
            potWidth = scaleWidth;
            potHeight = scaleHeight;
        }

        Pixmap tmp = new Pixmap(potWidth, potHeight, Format.RGBA4444);
        tmp.setBlending(Pixmap.Blending.None);
        tmp.drawPixmap(pixmap, 0, 0, pixmapWidth, pixmapHeight, 0, 0, potWidth, potHeight);
        pixmap.dispose();
        return tmp;
    }

    @Override
    public Pixmap consumePixmap() {
        if (!isPrepared)
            throw new GdxRuntimeException("Call prepare() before calling getPixmap()");
        isPrepared = false;
        Pixmap pixmap = this.pixmap;
        this.pixmap = null;
        return pixmap;
    }

    @Override
    public boolean disposePixmap() {
        return true;
    }

    @Override
    public int getWidth() {
        return MathUtils.round(width / scale);
    }

    @Override
    public int getHeight() {
        return MathUtils.round(height / scale);
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public boolean useMipMaps() {
        return useMipMaps;
    }

    @Override
    public boolean isManaged() {
        return true;
    }

    public FileHandle getFileHandle() {
        return file;
    }

    @Override
    public TextureDataType getType() {
        return TextureDataType.Pixmap;
    }

    public void consumeCustomData(int target) {
        throw new GdxRuntimeException("This TextureData implementation does not upload data itself");
    }


//	@Override
//	public void consumeCompressedData(int target) {
//		// TODO Auto-generated method stub
//
//	}
}
