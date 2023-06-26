package com.esotericsoftware.spine.loader;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.AttachmentLoader;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.esotericsoftware.spine.attachments.ClippingAttachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.PathAttachment;
import com.esotericsoftware.spine.attachments.PointAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;


public class DDAtlasAttachmentLoader implements AttachmentLoader {

    public static String prefix = "";

    public static TextureAtlas[] commonAtlas = null;

    public static void setCommonTextureAtlas(TextureAtlas[] commonAtlas) {
        DDAtlasAttachmentLoader.commonAtlas = commonAtlas;
    }

    public static TextureAtlas.AtlasRegion getAtlasRegionFromCommon(String name, String path) {
        TextureAtlas.AtlasRegion region = null;
        if (commonAtlas != null) {
            for (int i = 0; i < commonAtlas.length; i++) {
                if (commonAtlas[i] != null) {
                    if (prefix != null && !prefix.equals("")) {
                        region = commonAtlas[i].findRegion(prefix + path);
                        if (region == null) {
                            region = commonAtlas[i].findRegion(path);
                            if(region == null){
//                                throw new IllegalArgumentException("========================region is null===========================");
                            }
                        }
                    } else {
                        region = commonAtlas[i].findRegion(path);
                        if(region == null) {
//                            throw new IllegalArgumentException("========================region is null===========================");
                        }
                    }
                }
            }
        }
        return region;
    }

    public TextureAtlas.AtlasRegion findAtlasRegion(TextureAtlas atlas, String name, String path) {
        TextureAtlas.AtlasRegion region = null;
        if (atlas != null) {
            if (prefix != null && !prefix.equals("")) {
                region = atlas.findRegion(prefix + path);
                if (region == null) {
                    region = atlas.findRegion(path);
                }
            } else {
                region = atlas.findRegion(path);
            }
        }
        if (region == null) region = getAtlasRegionFromCommon(name, path);
        return region;
    }

    private TextureAtlas atlas;

    public DDAtlasAttachmentLoader(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
        TextureAtlas.AtlasRegion region = findAtlasRegion(atlas, name, path);
        if (region == null) {
//            throw new RuntimeException("Region not found in atlas: " + path + " (region attachment: " + name + ")");
//            new RuntimeException("Region not found in atlas: " + path + " (region attachment: " + name + ")").printStackTrace();
        }
        DDRegionAttachment attachment = new DDRegionAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    public MeshAttachment newMeshAttachment(Skin skin, String name, String path) {
        TextureAtlas.AtlasRegion region = findAtlasRegion(atlas, name, path);
        if (region == null) {
            throw new RuntimeException("Region not found in atlas: " + path + " (mesh attachment: " + name + ")");
        }
        MeshAttachment attachment = new MeshAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
        return new BoundingBoxAttachment(name);
    }

    public ClippingAttachment newClippingAttachment(Skin skin, String name) {
        return new ClippingAttachment(name);
    }

    public PathAttachment newPathAttachment(Skin skin, String name) {
        return new PathAttachment(name);
    }

    public PointAttachment newPointAttachment(Skin skin, String name) {
        return new PointAttachment(name);
    }
}
