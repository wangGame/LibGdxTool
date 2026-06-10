package com.libGdx.test.textpacker;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class PackerDemo extends LibGdxTestMain {

    public static void main(String[] args) {
        PackerDemo packerDemo = new PackerDemo();
        packerDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Gdx.files = new LwjglFiles();

        TextureChannelPacker.PackConfig config = new TextureChannelPacker.PackConfig();

        config.inputFiles.put("metallic", Gdx.files.absolute("input/metallic.png"));
        config.inputFiles.put("roughness", Gdx.files.absolute("input/roughness.png"));
        config.inputFiles.put("ao", Gdx.files.absolute("input/ao.png"));

        config.outputMapping.put(
                TextureChannelPacker.OutputChannel.R,
                TextureChannelPacker.ChannelSource.fromInput("metallic", TextureChannelPacker.InputChannel.R)
        );
        config.outputMapping.put(
                TextureChannelPacker.OutputChannel.G,
                TextureChannelPacker.ChannelSource.fromInput("roughness", TextureChannelPacker.InputChannel.R)
        );
        config.outputMapping.put(
                TextureChannelPacker.OutputChannel.B,
                TextureChannelPacker.ChannelSource.fromInput("ao", TextureChannelPacker.InputChannel.R)
        );
        config.outputMapping.put(
                TextureChannelPacker.OutputChannel.A,
                TextureChannelPacker.ChannelSource.constant(255)
        );

        config.sizeMode = TextureChannelPacker.SizeMode.STRICT;

        TextureChannelPacker.packToFile(
                config,
                Gdx.files.absolute("output/packed_mask.png")
        );

        System.out.println("Packed texture written to output/packed_mask.png");
    }
}