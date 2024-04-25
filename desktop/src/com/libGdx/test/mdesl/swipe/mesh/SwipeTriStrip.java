package com.libGdx.test.mdesl.swipe.mesh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import com.libGdx.test.mdesl.swipe.SwipeHandler;

public class SwipeTriStrip extends Actor {
	SwipeHandler swipe;
	private Texture tex;
	Array<Vector2> texcoord = new Array<Vector2>();
	Array<Vector2> tristrip = new Array<Vector2>();
	int batchSize;
	Vector2 perp = new Vector2();
	public float thickness = 30f;
	public float endcap = 0.5f;
	public Color color = new Color(Color.WHITE);
	ImmediateModeRenderer20 gl20;
	
	public SwipeTriStrip() {
		swipe = new SwipeHandler(10);
		//minimum distance between two points
		swipe.minDistance = 10;

		//minimum distance between first and second point
		swipe.initialDistance = 10;
		tex = new Texture("data/gradient.png");
		tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		gl20 = new ImmediateModeRenderer20(false, true, 1);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (tristrip.size<=0)
			return;
		gl20.begin(batch.getProjectionMatrix(), GL20.GL_TRIANGLE_STRIP);
		for (int i=0; i<tristrip.size; i++) {
			if (i==batchSize) {
				gl20.end();
				gl20.begin(batch.getProjectionMatrix(), GL20.GL_TRIANGLE_STRIP);
			}
			Vector2 point = tristrip.get(i);
			Vector2 tc = texcoord.get(i);
			gl20.color(color.r, color.g, color.b, color.a);
			gl20.texCoord(tc.x, 0f);
			gl20.vertex(point.x, point.y, 0f);
		}
		gl20.end();
	}
	
//	private int generate(Array<Vector2> input, int mult) {
//		int c = tristrip.size;
//		if (endcap<=0) {
//			tristrip.add(input.get(0));
//		} else {
//			Vector2 p = input.get(0);
//			Vector2 p2 = input.get(1);
//			perp.set(p).sub(p2).mul(endcap);
//			tristrip.add(new Vector2(p.x+perp.x, p.y+perp.y));
//		}
//		texcoord.add(new Vector2(0f, 0f));
//
//
//
//		for (int i=1; i<input.size-1; i++) {
//			Vector2 p = input.get(i);
//			Vector2 p2 = input.get(i+1);
//			//get direction and normalize it
//			perp.set(p).sub(p2).nor();
//			//get perpendicular
//			perp.set(-perp.x,perp.y);
//			float thick = thickness ;
//			//move outward by thickness
//			perp.mul(thick/2f);
//
//			//decide on which side we are using
//			perp.mul(mult);
//			//add the tip of perpendicular
//			tristrip.add(new Vector2(p.x+perp.x, p.y+perp.y));
//			//0.0 -> end, transparent
//			texcoord.add(new Vector2(0f, 0f));
//			//add the center point
//			tristrip.add(new Vector2(p.x, p.y));
//			//1.0 -> center, opaque
//			texcoord.add(new Vector2(1f, 0f));
//		}
//
//		//final point
//		if (endcap<=0) {
//			tristrip.add(input.get(input.size-1));
//		} else {
//			Vector2 p = input.get(input.size-2);
//			Vector2 p2 = input.get(input.size-1);
//			perp.set(p2).sub(p).mul(endcap);
//			tristrip.add(new Vector2(p2.x+perp.x, p2.y+perp.y));
//		}
//		//end cap is transparent
//		texcoord.add(new Vector2(0f, 0f));
//		return tristrip.size-c;
//	}
private int generate(Array<Vector2> input, int mult) {
	if (input.size < 2) {
		return 0;
	}
	for (int i = 0; i < input.size/2-2; i++) {
		Vector2 p1 = input.get(i*2+1);
		Vector2 p2 = input.get(i*2+2);
		tristrip.add(new Vector2(p1.x, p1.y));
		tristrip.add(new Vector2(p1.x, p1.y+50));
		tristrip.add(new Vector2(p2.x, p2.y));
		tristrip.add(new Vector2(p2.x, p2.y+50));
		texcoord.add(new Vector2(0f, 0f));
		texcoord.add(new Vector2(0f, 1f));
		texcoord.add(new Vector2(1f, 0f));
		texcoord.add(new Vector2(1f, 1f));
	}
//	int c = tristrip.size;
//	if (endcap<=0) {
//		tristrip.add(input.get(0));
//	} else {
//		Vector2 p = input.get(0);
//		Vector2 p2 = input.get(1);
//		perp.set(p).sub(p2).mul(endcap);
//		tristrip.add(new Vector2(p.x, p.y));
//	}
//	texcoord.add(new Vector2(0f, 0f));
//
//
//
//	for (int i=1; i<input.size-1; i++) {
//		Vector2 p = input.get(i);
//		Vector2 p2 = input.get(i+1);
//		//get direction and normalize it
//		perp.set(p).sub(p2).nor();
//		//get perpendicular
//		perp.set(-perp.x,perp.y);
//		float thick = thickness ;
//		//move outward by thickness
//		perp.mul(thick/2f);
//
//		//decide on which side we are using
//		perp.mul(mult);
//		//add the tip of perpendicular
//		tristrip.add(new Vector2(p.x+perp.x, p.y+perp.y));
//		//0.0 -> end, transparent
//		texcoord.add(new Vector2(0f, 0f));
//		//add the center point
//		tristrip.add(new Vector2(p.x, p.y));
//		//1.0 -> center, opaque
//		texcoord.add(new Vector2(1f, 0f));
//	}
//
//	//final point
//	if (endcap<=0) {
//		tristrip.add(input.get(input.size-1));
//	} else {
//		Vector2 p = input.get(input.size-2);
//		Vector2 p2 = input.get(input.size-1);
//		perp.set(p2).sub(p).mul(endcap);
//		tristrip.add(new Vector2(p2.x , p2.y ));
//	}
//	//end cap is transparent
//	texcoord.add(new Vector2(0f, 0f));
	return 2;
}
	
	public void update(Array<Vector2> input) {
		tristrip.clear();
		texcoord.clear();
		
		if (input.size<2)
			return;
		batchSize = generate(input, 1);
		int b = generate(input, -1);
	}
	
}
