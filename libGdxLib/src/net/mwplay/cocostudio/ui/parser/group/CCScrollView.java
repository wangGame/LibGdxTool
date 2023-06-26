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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ui.util.NUtils;

import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.group.ScrollViewObjectData;
import net.mwplay.cocostudio.ui.parser.GroupParser;

/**
 *         tip 滚动方向, 回弹滚动支持不是很好
 */
public class CCScrollView extends GroupParser<ScrollViewObjectData> {

    @Override
    public String getClassName() {
        return "ScrollViewObjectData";
    }

    @Override
    public Actor parse(BaseCocoStudioUIEditor editor, ScrollViewObjectData widget) {
        ScrollPaneStyle style = new ScrollPaneStyle();

        if (widget.FileData != null) {

            style.background = editor
                .findDrawable(widget, widget.FileData);
        }

        ScrollPane scrollPane = new ScrollPane(null, style);

        if ("Vertical_Horizontal".equals(widget.ScrollDirectionType)) {
            scrollPane.setForceScroll(true, true);
        } else if ("Horizontal".equals(widget.ScrollDirectionType)) {
            scrollPane.setForceScroll(true, false);
        } else if ("Vertical".equals(widget.ScrollDirectionType)) {
            scrollPane.setForceScroll(false, true);
        }

        scrollPane.setClamp(widget.ClipAble);
        scrollPane.setFlickScroll(widget.IsBounceEnabled);

        Table table = new Table();
        table.setSize(widget.InnerNodeSize.Width, widget.InnerNodeSize.height);

        if (widget.ComboBoxIndex == 0) {// 无颜guo l

        } else if (widget.ComboBoxIndex == 1) {// 单色

            Pixmap pixmap = new Pixmap((int) table.getWidth(),
                (int) table.getHeight(), Format.RGBA8888);
            Color color = NUtils.getColor(widget.SingleColor, widget.BackColorAlpha);

            pixmap.setColor(color);

            pixmap.fill();

            Drawable drawable = new TextureRegionDrawable(new TextureRegion(
                new Texture(pixmap)));

            table.setBackground(drawable);
            pixmap.dispose();

        }

//        scrollPane.setWidget(table);
        scrollPane.setActor(table);
        return scrollPane;
    }

    @Override
    public Group groupChildrenParse(BaseCocoStudioUIEditor editor,
            ScrollViewObjectData widget, Group parent, Actor actor) {
        ScrollPane scrollPane = (ScrollPane) actor;
        Table table = new Table();
        for (ObjectData childrenWidget : widget.Children) {
            Actor childrenActor = editor.parseWidget(table, childrenWidget);
            if (childrenActor == null) {
                continue;
            }

            table.setSize(Math.max(table.getWidth(), childrenActor.getRight()),
                Math.max(table.getHeight(), childrenActor.getTop()));
            table.addActor(childrenActor);
        }
        sort(widget, table);
        //

//        scrollPane.setWidget(table);
        scrollPane.setActor(table);

        return scrollPane;
    }

}
