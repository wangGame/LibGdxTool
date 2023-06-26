/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.mwplay.cocostudio.ui.parser.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.CColor;
import net.mwplay.cocostudio.ui.model.objectdata.widget.TextBMFontObjectData;
import net.mwplay.cocostudio.ui.parser.WidgetParser;

public class CCLabelBMFont extends WidgetParser<TextBMFontObjectData> {

	@Override
	public String getClassName () {
		return "TextBMFontObjectData";
	}

	@Override
	public Actor parse (BaseCocoStudioUIEditor editor, TextBMFontObjectData widget) {
		BitmapFont font = null;

		font = editor.findBitmapFont(widget.LabelBMFontFile_CNB);

		if (font == null) {
			Gdx.app.debug(widget.ctype, "BitmapFont字体:" + widget.LabelBMFontFile_CNB.Path + " 不存在");
			font = new BitmapFont();
		}else {
		}

		CColor color = widget.CColor;

		//不使用Int坐标
		font.setUseIntegerPositions(false);
		//使高度与Cocos坐标相一致
		font.getData().ascent=0;
		font.getData().capHeight=font.getData().lineHeight;

//		Color textColor = new Color(color.R / 255.0f, color.G / 255.0f, color.B / 255.0f, widget.Alpha / 255.0f);
		Color textColor = Color.WHITE;
		Label.LabelStyle style = new Label.LabelStyle(font, textColor);
		Label label = new Label(widget.LabelText, style);
//		MyBMFontLabel com.kw.gdx.label=new MyBMFontLabel(widget.LabelText, font);
//		com.kw.gdx.label.setColor(textColor);

		if (widget.AnchorPoint.ScaleX == 0 && widget.AnchorPoint.ScaleY == 0) {
			label.setAlignment(Align.bottomLeft);
		} else if (widget.AnchorPoint.ScaleX == 0 && widget.AnchorPoint.ScaleY == 1) {
			label.setAlignment(Align.topLeft);
		} else if (widget.AnchorPoint.ScaleX == 1 && widget.AnchorPoint.ScaleY == 0) {
			label.setAlignment(Align.bottomRight);
		} else if (widget.AnchorPoint.ScaleX == 1 && widget.AnchorPoint.ScaleY == 1) {
			label.setAlignment(Align.topRight);
		} else if (widget.AnchorPoint.ScaleX == 0) {
			label.setAlignment(Align.left);
		} else if (widget.AnchorPoint.ScaleX == 1) {
			label.setAlignment(Align.right);
		} else if (widget.AnchorPoint.ScaleY == 0) {
			label.setAlignment(Align.bottom);
		} else if (widget.AnchorPoint.ScaleY == 1) {
			label.setAlignment(Align.top);
		} else {
			label.setAlignment(Align.center);
		}
		return label;
	}

}
