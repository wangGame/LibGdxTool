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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.objectdata.widget.ImageViewObjectData;
import net.mwplay.cocostudio.ui.parser.WidgetParser;

public class CCImageView extends WidgetParser<ImageViewObjectData> {

    @Override
    public String getClassName() {
        return "ImageViewObjectData";
    }

    @Override
    public Actor parse(BaseCocoStudioUIEditor editor, ImageViewObjectData widget) {

		Drawable tr = editor.findDrawable(widget, widget.FileData);
        if (tr == null) {
            return new Image();
        }
        if (tr instanceof TextureRegionDrawable){
            if (widget.FlipX){
                TextureRegionDrawable tr1 = (TextureRegionDrawable) (tr);
                tr1.getRegion().flip(widget.FlipX, widget.FlipY);
            }
        }
        return new Image(tr);
    }

}
