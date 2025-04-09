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

package net.mwplay.cocostudio.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import net.mwplay.cocostudio.ui.model.CCExport;
import net.mwplay.cocostudio.ui.model.FileData;
import net.mwplay.cocostudio.ui.model.GameProjectData;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.group.*;
import net.mwplay.cocostudio.ui.model.objectdata.widget.ImageViewObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.widget.SpriteObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.widget.TextBMFontObjectData;
import net.mwplay.cocostudio.ui.model.objectdata.widget.TextObjectData;
import net.mwplay.cocostudio.ui.model.timelines.CCIntFrameData;
import net.mwplay.cocostudio.ui.model.timelines.CCPointFrameData;
import net.mwplay.cocostudio.ui.model.timelines.CCScaleValueFrameData;
import net.mwplay.cocostudio.ui.model.timelines.CCTimelineActionData;
import net.mwplay.cocostudio.ui.model.timelines.CCTimelineData;
import net.mwplay.cocostudio.ui.model.timelines.CCTimelineFrame;
import net.mwplay.cocostudio.ui.parser.group.*;
import net.mwplay.cocostudio.ui.parser.widget.*;
import net.mwplay.cocostudio.ui.widget.TTFLabelStyle;

import java.util.HashMap;
import java.util.Map;

//import net.mwplay.nativefont.NativeFont;
//import net.mwplay.nativefont.NativeFontPaint;

public abstract class BaseCocoStudioUIEditor {
	protected CCExport export;
	protected Map<Actor, Action> actorActionMap;
	protected Map<String, Array<Actor>> actors;
	protected Map<Integer, Actor> actionActors;
	protected Map<String, BaseWidgetParser> parsers;

	public BaseCocoStudioUIEditor (FileHandle jsonFile) {
		parsers = new HashMap<String, BaseWidgetParser>();
		addParser(new CCNode());//节点
		addParser(new CCParticle());//粒子
		addParser(new CCSpriteView());//精灵
		addParser(new CCButton());//按钮
		addParser(new CCCheckBox());//复选框
		addParser(new CCImageView());//图片
		addParser(new CCLabel());//文本
		addParser(new CCLabelBMFont());//艺术字体
		addParser(new CCLoadingBar());//进度条
		addParser(new CCSlider());//滑动条
		addParser(new CCTextField());//输入框
		addParser(new CCPanel());//基础容器
		addParser(new CCPageView());//翻页容器
		addParser(new CCScrollView());//滚动容器
		addParser(new CCTextAtlas());//?
		addParser(new CCLabelAtlas());//?
		addParser(new CCTImageView());//?
		addParser(new CCProjectNode());//项目节点
		addParser(new CCLayer());//图层
		actors = new HashMap<String, Array<Actor>>();
		actionActors = new HashMap<Integer, Actor>();
		//animations = new HashMap<String, Map<Actor, Action>>();
		actorActionMap = new HashMap<Actor, Action>();
		String json = jsonFile.readString("utf-8");
		Json jj = new Json();
		jj.setTypeName("ctype");
		jj.addClassTag("GameFileData", GameProjectData.class);
		jj.addClassTag("TimelineActionData", CCTimelineActionData.class);
		jj.addClassTag("SingleNodeObjectData", SingleNodeObjectData.class);
		jj.addClassTag("SpriteObjectData", SpriteObjectData.class);
		jj.addClassTag("ProjectNodeObjectData", ProjectNodeObjectData.class);
		jj.addClassTag("LayerObjectData", LayerObjectData.class);
		jj.addClassTag("PanelObjectData", PanelObjectData.class);
		jj.addClassTag("ButtonObjectData", ButtonObjectData.class);
		jj.addClassTag("TextObjectData", TextObjectData.class);
		jj.addClassTag("ImageViewObjectData", ImageViewObjectData.class);
		jj.addClassTag("CheckBoxObjectData", CheckBoxObjectData.class);
		jj.addClassTag("ScrollViewObjectData", ScrollViewObjectData.class);
		jj.addClassTag("TextBMFontObjectData", TextBMFontObjectData.class);
		jj.addClassTag("TimelineData", CCTimelineData.class);
		jj.addClassTag("PointFrameData", CCTimelineFrame.class);
		jj.addClassTag("ScaleValueFrameData", CCTimelineFrame.class);
		jj.addClassTag("IntFrameData", CCIntFrameData.class);
		jj.setIgnoreUnknownFields(true);
		export = jj.fromJson(CCExport.class, json);
	}

	public abstract <T extends ObjectData> Drawable findDrawable (T widget, FileData textureFile);

	public abstract BitmapFont createLabelStyleBitmapFint (ObjectData widget, String buttonText, Color color);

	public abstract BaseCocoStudioUIEditor findCoco (FileData fileData);

	public Actor parseWidget (Group parent, ObjectData widget) {
		String className = widget.ctype;
		BaseWidgetParser parser = parsers.get(className);
		if (parser == null) {
			Gdx.app.debug(widget.ctype, "not support Widget:" + className);
			return null;
		}
		Actor actor = parser.parse(this, widget);
		actor = parser.commonParse(this, widget, parent, actor);
		return actor;
	}

	public Group createGroup () {
		Actor actor = parseWidget(null, export.Content.Content.ObjectData);
		actorActionMap.clear();
		actors.clear();
		actionActors.clear();
		return (Group)actor;
	}

	public abstract TTFLabelStyle createLabelStyle (ObjectData widget, String labelText, Color color);

	public abstract BitmapFont findBitmapFont (FileData labelBMFontFile_cnb);

	public abstract TextureRegion findTextureRegion (ObjectData widget, FileData path);

	public abstract String findParticePath (FileData fileData);

	public void addParser (BaseWidgetParser parser) {
		parsers.put(parser.getClassName(), parser);
	}
}
