package com.libGdx.test.mdesl.swipe.simplify;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.libGdx.test.mdesl.swipe.SwipeResolver;

public class ResolverCopy implements SwipeResolver {
	
	@Override
	public void resolve(Array<Vector2> input, Array<Vector2> output) {
		output.clear(); 
		output.addAll(input);
	}

}
