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
package net.mwplay.cocostudio.ui.parser;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.mwplay.cocostudio.ui.BaseWidgetParser;
import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.model.Scale;
import net.mwplay.cocostudio.ui.model.objectdata.WidgetData;

/**
 * 单控件转换器
 */
public abstract class WidgetParser<T extends WidgetData> extends BaseWidgetParser<T> {

    @Override
    public Actor commonParse(BaseCocoStudioUIEditor editor, T widget,
                             Group parent, Actor actor) {
        Actor ac = super.commonParse(editor, widget, parent, actor);
        if (ac != null) {
            return ac;
        }
        return widgetChildrenParse(editor, widget, parent, actor);
    }

    /**
     * 解析子控件
     */
    public Group widgetChildrenParse(BaseCocoStudioUIEditor editor,
            T widget, Group parent, Actor actor) {
        Table table = new Table();
        table.setClip(widget.ClipAble);
        table.setName(actor.getName());

        Scale scale = widget.Scale;

        if (scale != null) {
            table.setScale(scale.ScaleX, scale.ScaleY);
        }

        table.setRotation(actor.getRotation());
        table.setVisible(actor.isVisible());

        table.setTouchable(widget.TouchEnable ? Touchable.enabled
            : Touchable.childrenOnly);

        // editor.getActors().get(actor.getName()).removeValue(actor, true);
        //
        // addActor(editor, table, option);

        actor.setVisible(true);
        actor.setTouchable(Touchable.disabled);

        if (scale != null || widget.Rotation != 0) {
            table.setTransform(true);
        }

        table.setSize(actor.getWidth(), actor.getHeight());
        table.setPosition(actor.getX(), actor.getY());

        // 锚点就是子控件的锚点

        Scale anchorPoint = widget.AnchorPoint;
        if (anchorPoint != null) {

            table.setOrigin(anchorPoint.ScaleX * table.getWidth(),
                anchorPoint.ScaleY * table.getHeight());
        }
        for (ObjectData childrenWidget : widget.Children) {
            Actor childrenActor = editor.parseWidget(table, childrenWidget);
            if (childrenActor == null) {
                continue;
            }
            table.addActor(childrenActor);
        }
        sort(widget, table);

        // Widget的位置应该与Table重合.相当于Widget的属性被移植到了Table
        actor.setPosition(0, 0);
        actor.setScale(1, 1);
        table.addActorAt(0, actor);
        return table;
    }

}
