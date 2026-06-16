package com.libGdx.test.textpacker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class PackerDemoSmoothness extends LibGdxTestMain {
    public static void main(String[] args) {
        PackerDemoSmoothness packerDemoSmoothness = new PackerDemoSmoothness();
        packerDemoSmoothness.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);


        TextureChannelPacker2.PackConfig config = new TextureChannelPacker2.PackConfig();

        config.inputs.put(
                TextureChannelPacker2.InputSlot.INPUT1,
                new TextureChannelPacker2.InputSource(Gdx.files.absolute("input/metallic.png"), "Metallic")
        );
        config.inputs.put(
                TextureChannelPacker2.InputSlot.INPUT2,
                new TextureChannelPacker2.InputSource(Gdx.files.absolute("input/ao.png"), "AO")
        );
        config.inputs.put(
                TextureChannelPacker2.InputSlot.INPUT3,
                new TextureChannelPacker2.InputSource(Gdx.files.absolute("input/roughness.png"), "Roughness")
        );

        config.mapping.put(
                TextureChannelPacker2.OutputChannel.R,
                TextureChannelPacker2.ChannelSource.fromInput(
                        TextureChannelPacker2.InputSlot.INPUT1,
                        TextureChannelPacker2.ColorChannel.R
                )
        );

        config.mapping.put(
                TextureChannelPacker2.OutputChannel.G,
                TextureChannelPacker2.ChannelSource.fromInput(
                        TextureChannelPacker2.InputSlot.INPUT2,
                        TextureChannelPacker2.ColorChannel.R
                )
        );

        config.mapping.put(
                TextureChannelPacker2.OutputChannel.B,
                TextureChannelPacker2.ChannelSource.constant(0)
        );

        config.mapping.put(
                TextureChannelPacker2.OutputChannel.A,
                TextureChannelPacker2.ChannelSource.fromInput(
                        TextureChannelPacker2.InputSlot.INPUT3,
                        TextureChannelPacker2.ColorChannel.R,
                        true,
                        1.0f
                )
        );

        config.sizeMode = TextureChannelPacker2.SizeMode.SCALE_TO_FIRST;

        TextureChannelPacker2.packToFile(
                config,
                Gdx.files.absolute("output/mask_packed.png")
        );

        System.out.println("Done: output/mask_packed.png");
    }
}