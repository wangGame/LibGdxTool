package com.libGdx.test.textpacker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class ORM extends LibGdxTestMain {
    public static void main(String[] args) {
        ORM orm = new ORM();
        orm.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        TextureChannelPacker2.PackConfig config = new TextureChannelPacker2.PackConfig();

        config.inputs.put(
                TextureChannelPacker2.InputSlot.INPUT1,
                new TextureChannelPacker2.InputSource(Gdx.files.absolute("input/ao.png"), "AO")
        );
        config.inputs.put(
                TextureChannelPacker2.InputSlot.INPUT2,
                new TextureChannelPacker2.InputSource(Gdx.files.absolute("input/roughness.png"), "Roughness")
        );
        config.inputs.put(
                TextureChannelPacker2.InputSlot.INPUT3,
                new TextureChannelPacker2.InputSource(Gdx.files.absolute("input/metallic.png"), "Metallic")
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
                TextureChannelPacker2.ChannelSource.fromInput(
                        TextureChannelPacker2.InputSlot.INPUT3,
                        TextureChannelPacker2.ColorChannel.R
                )
        );
        config.mapping.put(
                TextureChannelPacker2.OutputChannel.A,
                TextureChannelPacker2.ChannelSource.constant(255)
        );

        config.sizeMode = TextureChannelPacker2.SizeMode.STRICT;

        TextureChannelPacker2.packToFile(
                config,
                Gdx.files.absolute("output/orm.png")
        );

        System.out.println("Done: output/orm.png");
    }
}
