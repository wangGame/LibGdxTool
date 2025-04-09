package com.ui.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

import net.mwplay.cocostudio.ui.model.CColor;

public class NUtils {
	public static Color getColor (CColor c, int alpha) {
		Color color = null;
		if (c == null) {// || c.R + c.G + c.B == 0
			color = new Color(Color.WHITE);
		} else {
			color = new Color();
			color.a = 1;
			color.r = c.R / 255f;
			color.g = c.G / 255f;
			color.b = c.B / 255f;
		}

		if (alpha != 0) {
			color.a = alpha / 255f;
		}

		return color;
	}

	public static Interpolation getInterpolation (int tweenType) {
		switch (tweenType) {
		case 0:
			return Interpolation.linear;
		case 1:
			return Interpolation.sineIn;
		case 2:
			return Interpolation.sineOut;
		case 3:
			return Interpolation.sine;
		case 4:
			return Interpolation.linear; //不支持Quad_EaseIn
		case 5:
			return Interpolation.linear; //不支持Quad_EaseOut
		case 6:
			return Interpolation.linear; //不支持Quad_EaseInOut
		case 7:
			return Interpolation.linear; //不支持Cubic_EaseIn
		case 8:
			return Interpolation.linear; //不支持Cubic_EaseOut
		case 9:
			return Interpolation.linear; //不支持Cubic_EaseInOut
		case 10:
			return Interpolation.linear; //不支持Quart_EaseIn
		case 11:
			return Interpolation.linear; //不支持Quart_EaseOut
		case 12:
			return Interpolation.linear; //不支持Quart_EaseInOut
		case 13:
			return Interpolation.linear; //不支持Quint_EaseIn
		case 14:
			return Interpolation.linear; //不支持Quint_EaseOut
		case 15:
			return Interpolation.linear; //不支持Quint_EaseInOut
		case 16:
			return Interpolation.exp10In;
		case 17:
			return Interpolation.exp10Out;
		case 18:
			return Interpolation.exp10;
		case 19:
			return Interpolation.circleIn;
		case 20:
			return Interpolation.circleOut;
		case 21:
			return Interpolation.circle;
		case 22:
			return Interpolation.elasticIn;
		case 23:
			return Interpolation.elasticOut;
		case 24:
			return Interpolation.elastic;
		case 25:
			return Interpolation.linear; //不支持Back_EaseIn
		case 26:
			return Interpolation.linear; //不支持Back_EaseOut
		case 27:
			return Interpolation.linear; //不支持Back_EaseInOut
		case 28:
			return Interpolation.bounceIn;
		case 29:
			return Interpolation.bounceOut;
		case 30:
			return Interpolation.bounce;
		default:
			return Interpolation.linear;
		}
	}
}
