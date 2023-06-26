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

package net.mwplay.cocostudio.ui.parser.group;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ui.util.NUtils;

import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.objectdata.group.ButtonObjectData;
import net.mwplay.cocostudio.ui.parser.GroupParser;

public class CCButton extends GroupParser<ButtonObjectData> {

	protected BaseCocoStudioUIEditor editor;

	@Override
	public String getClassName () {
		return "ButtonObjectData";
	}

	@Override
	public Actor parse (BaseCocoStudioUIEditor editor, final ButtonObjectData widget) {
		this.editor = editor;

		final Button button;
		//分开解决TextButton和ImageButton
		if (widget.ButtonText != null && !widget.ButtonText.equals("")) {
			BitmapFont bitmapFont = editor
				.createLabelStyleBitmapFint(widget, widget.ButtonText, NUtils.getColor(widget.TextColor, widget.Alpha));

			TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
				editor.findDrawable(widget, widget.NormalFileData), editor.findDrawable(widget, widget.PressedFileData), null,
				bitmapFont);

			button = new TextButton(widget.ButtonText, textButtonStyle);

		} else {

			Button.ButtonStyle buttonStyle = new Button.ButtonStyle(editor.findDrawable(widget, widget.NormalFileData),
				editor.findDrawable(widget, widget.PressedFileData), null);

			buttonStyle.disabled=editor.findDrawable(widget, widget.DisabledFileData);

			button=new Button(buttonStyle);


//			ImageButtonStyle style = new ImageButtonStyle(editor.findDrawable(widget, widget.NormalFileData),
//				editor.findDrawable(widget, widget.PressedFileData), null, null, null, null);
//
//			style.disabled = editor.findDrawable(widget, widget.DisabledFileData);
//
//			button = new ImageButton(style);

			//已把textbutton和ImageButton分开，所以注释掉
			/*if (widget.getButtonText() != null
                    && !widget.getButtonText().equals("")) {
                TTFLabelStyle labelStyle = editor.createLabelStyle(widget,
                        widget.getButtonText(),
                        editor.getColor(widget.getTextColor(), 0));
                TTFLabel com.kw.gdx.label = new TTFLabel(widget.getButtonText(), labelStyle);
                com.kw.gdx.label.setPosition((button.getWidth() - com.kw.gdx.label.getWidth()) / 2,
                        (button.getHeight() - com.kw.gdx.label.getHeight()) / 2);
                button.addActor(com.kw.gdx.label);
            }*/
		}

		button.setDisabled(widget.DisplayState);

		if ("Click".equals(widget.CallBackType)) {
			button.addListener(new ClickListener() {
				@Override
				public void clicked (InputEvent event, float x, float y) {
					invoke(button, widget.CallBackName);
					super.clicked(event, x, y);
				}
			});
		} else if ("Touch".equals(widget.CallBackType)) {

			button.addListener(new ClickListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button2) {
					invoke(button, widget.CallBackName);
					return super.touchDown(event, x, y, pointer, button2);
				}
			});
		}

		return button;
	}

}
