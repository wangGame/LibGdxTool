package com.kw.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;

public class ModelUtils {
    public static ModelInstance createInstance() {
        Model modelUp = new ObjLoader().loadModel(Gdx.files.internal("model/Cube_0.obj"));
        Texture texture = new Texture(Gdx.files.internal("textures/1.png"));
        TextureAttribute diffuse = TextureAttribute.createDiffuse(texture);
        Material material1 = new Material(diffuse);
        Material material2 = new Material(TextureAttribute.createDiffuse(texture));
        ModelInstance shipInstance = new ModelInstance(modelUp);
        Node node1 = shipInstance.nodes.get(0);
        node1.parts.get(0).material = material1;
//        Node node2 = shipInstance.nodes.get(1);
//        node2.parts.get(0).material = material2;
        for (Node node : shipInstance.nodes) {
            node.scale.set(3*1500,6*1500,1*1500);
        }
        shipInstance.calculateTransforms();
//        shipInstance.transform.scale(1500, 1500, 1500f);
        return shipInstance;
    }
}
