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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ui.util.NUtils;

import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.model.timelines.CCTimelineActionData;
import net.mwplay.cocostudio.ui.model.timelines.CCTimelineData;
import net.mwplay.cocostudio.ui.model.timelines.CCTimelineFrame;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

public abstract class BaseWidgetParser<T extends ObjectData> {

	BaseCocoStudioUIEditor editor;

	/**
	 * 由于libgdx的zindex并不表示渲染层级,所以这里采用这种方式来获取子控件的当前层级
	 */
	public static int getZOrder (ObjectData widget, String name) {
		if (name == null) {
			return 0;
		}
		for (ObjectData child : widget.Children) {
			if (name.equals(child.Name)) {
				return child.ZOrder;
			}
		}
		return 0;
	}

	/**
	 * get widget type name
	 */
	public abstract String getClassName ();

	/**
	 * convert cocostudio widget to libgdx actor
	 */
	public abstract Actor parse (BaseCocoStudioUIEditor editor, T widget);

	/**
	 * common attribute parser
	 * according cocstudio ui setting properties of the configuration file
	 *
	 * @param editor
	 * @param widget
	 * @param parent
	 * @param actor
	 * @return
	 */
	public Actor commonParse (BaseCocoStudioUIEditor editor, T widget, Group parent, Actor actor) {
		this.editor = editor;
		actor.setName(widget.Name);
		actor.setSize(widget.Size.X, widget.Size.Y);
		// set origin
		if (widget.AnchorPoint != null) {
			actor.setOrigin(widget.AnchorPoint.ScaleX * actor.getWidth(), widget.AnchorPoint.ScaleY * actor.getHeight());
		}

		//判空，因为新版本的单独节点没有Postion属性
		if (widget.Position != null) {
			actor.setPosition(widget.Position.X - actor.getOriginX(), widget.Position.Y - actor.getOriginY());
		}

		// CocoStudio的编辑器ScaleX,ScaleY 会有负数情况
		//判空，因为新版本的单独节点没有Scale属性
		if (widget.Scale != null) {
			if (actor instanceof Label)
				((Label)actor).setFontScale(widget.Scale.ScaleX, widget.Scale.ScaleY);
			else
				actor.setScale(widget.Scale.ScaleX, widget.Scale.ScaleY);
		}

		if (widget.Rotation != 0) {// CocoStudio 是顺时针方向旋转,转换下.
			actor.setRotation(360 - widget.Rotation % 360);
		}
		//添加倾斜角
		if (widget.RotationSkewX != 0 && widget.RotationSkewX == widget.RotationSkewY) {
			actor.setRotation(360 - widget.RotationSkewX % 360);
		}



		// 设置可见
		actor.setVisible(widget.VisibleForFrame);
		Color color = NUtils.getColor(widget.CColor, widget.Alpha);
		actor.setColor(color);
		actor.setTouchable(deduceTouchable(actor, widget));
		// callback
		addCallback(actor, widget);
		// callback
		addActor(editor, actor, widget);
		if (widget.Children == null || widget.Children.size() == 0) {
			//添加Action
			parseAction(actor, widget);

			return actor;
		}

		return null;
	}

	private Touchable deduceTouchable (Actor actor, T widget) {
		if (widget.TouchEnable) {
			return Touchable.enabled;
		} else if (Touchable.childrenOnly.equals(actor.getTouchable())) {
			return Touchable.childrenOnly;
		} else {
			return Touchable.disabled;
		}
	}

	private void parseAction (final Actor actor, final T widget) {
		CCTimelineActionData ccTimelineActionData = editor.export.Content.Content.Animation;
		float duration = ccTimelineActionData.Duration;
		float speed = ccTimelineActionData.Speed;

		List<CCTimelineData> ccTimelineDatas = ccTimelineActionData.Timelines;

		ParallelAction parallelAction = new ParallelAction();

		for (CCTimelineData ccTimelineData : ccTimelineDatas) {
			if (ccTimelineData.ActionTag == widget.ActionTag) {

				List<CCTimelineFrame> ccTimelineFrames = ccTimelineData.Frames;

				//位移动画 MoveTo
				if (ccTimelineData.Property.equals("Position")) {
					SequenceAction sequenceAction = Actions.sequence();

					for (CCTimelineFrame ccTimelineFrame : ccTimelineFrames) {
						Action moveTo = null;
						//假如没有插值
						if (null == ccTimelineFrame.EasingData) {
							moveTo = Actions
								.moveTo(ccTimelineFrame.X - actor.getWidth() / 2, ccTimelineFrame.Y - actor.getHeight() / 2,
									speed / duration * ccTimelineFrame.FrameIndex);
						} else {//有插值
							moveTo = Actions
								.moveTo(ccTimelineFrame.X - actor.getWidth() / 2, ccTimelineFrame.Y - actor.getHeight() / 2,
									speed / duration * ccTimelineFrame.FrameIndex,
									NUtils.getInterpolation(ccTimelineFrame.EasingData.Type));
						}

						sequenceAction.addAction(moveTo);
					}

					parallelAction.addAction(sequenceAction);
				} else if (ccTimelineData.Property.equals("FileData")) {
					SequenceAction sequenceAction = Actions.sequence();
					for (CCTimelineFrame ccTimelineFrame : ccTimelineFrames) {
						final CCTimelineFrame temp = ccTimelineFrame;

						Action action = Actions.delay(speed / duration * ccTimelineFrame.FrameIndex, Actions.run(new Runnable() {
							@Override
							public void run () {
								((Image)actor).setDrawable(editor.findDrawable(widget, temp.TextureFile));
							}
						}));

						sequenceAction.addAction(action);
					}

					parallelAction.addAction(sequenceAction);
				} else if (ccTimelineData.Property.equals("Scale")) {
					SequenceAction sequenceAction = Actions.sequence();

					for (CCTimelineFrame ccTimelineFrame : ccTimelineFrames) {
						Action scaleTo = null;
						if (ccTimelineFrame.EasingData != null) {
							scaleTo = Actions
								.scaleTo(ccTimelineFrame.X, ccTimelineFrame.Y, speed / duration * ccTimelineFrame.FrameIndex,
									NUtils.getInterpolation(ccTimelineFrame.EasingData.Type));
						} else {
							scaleTo = Actions
								.scaleTo(ccTimelineFrame.X, ccTimelineFrame.Y, speed / duration * ccTimelineFrame.FrameIndex);
						}

						sequenceAction.addAction(scaleTo);
					}

					parallelAction.addAction(sequenceAction);
				} else if (ccTimelineData.Property.equals("RotationSkew")) {
					SequenceAction sequenceAction = Actions.sequence();
					for (CCTimelineFrame ccTimelineFrame : ccTimelineFrames) {
						Action rotation = null;
						float angle = new Vector2(ccTimelineFrame.X, ccTimelineFrame.Y).angle();
						if (ccTimelineFrame.EasingData != null) {
							rotation = Actions.rotateTo(angle, speed / duration * ccTimelineFrame.FrameIndex,
								NUtils.getInterpolation(ccTimelineFrame.EasingData.Type));
						} else {
							rotation = Actions.rotateTo(angle, speed / duration * ccTimelineFrame.FrameIndex);
						}

						sequenceAction.addAction(rotation);
					}

					parallelAction.addAction(sequenceAction);
				} else if (ccTimelineData.Property.equals("VisibleForFrame")) {
					SequenceAction sequenceAction = Actions.sequence();
					for (CCTimelineFrame ccTimelineFrame : ccTimelineFrames) {
						Action alpha = null;
						float alphaValue = 0;
						//显示
						if (ccTimelineFrame.Value) {
							alphaValue = 1;
						}

						if (ccTimelineFrame.EasingData != null) {
							alpha = Actions.alpha(alphaValue, speed / duration * ccTimelineFrame.FrameIndex,
								NUtils.getInterpolation(ccTimelineFrame.EasingData.Type));
						} else {
							alpha = Actions.alpha(alphaValue, speed / duration * ccTimelineFrame.FrameIndex);
						}

						sequenceAction.addAction(alpha);
					}

					parallelAction.addAction(sequenceAction);
				}
			}
		}

		editor.actorActionMap.put(actor, parallelAction);
		actor.addAction(Actions.forever(parallelAction));
	}

	public void addCallback (final Actor actor, final T widget) {
		if (widget.CallBackType == null || widget.CallBackType.length() == 0) {
			return;
		}
		if ("Click".equals(widget.CallBackType)) {
			actor.addListener(new ClickListener() {
				@Override
				public void clicked (InputEvent event, float x, float y) {
					invoke(actor, widget.CallBackName);
					super.clicked(event, x, y);
				}
			});
		} else if ("Touch".equals(widget.CallBackType)) {
			actor.addListener(new ClickListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					invoke(actor, widget.CallBackName);
					return super.touchDown(event, x, y, pointer, button);
				}
			});
		}
	}

	public void invoke (Actor actor, String methodName) {
		Stage stage = actor.getStage();
		if (stage == null) {
			return;
		}

		if (methodName == null || methodName.length() == 0) {
			// default callback method
			methodName = actor.getName();
		}

		if (methodName == null || methodName.length() == 0) {
			Gdx.app.error("","CallBackName isEmpty");
			return;
		}

		Class clazz = stage.getClass();

		Method method = null;
		try {
			method = clazz.getMethod(methodName);
		} catch (Exception e) {
			Gdx.app.debug("",clazz.getName() + "没有这个回调方法:" + methodName);
		}

		if (method == null) {
			return;
		}
		try {
			method.invoke(stage);
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.error("",clazz.getName() + "回调出错:" + methodName);
		}

	}

	protected void addActor (BaseCocoStudioUIEditor editor, Actor actor, T option) {
		Array<Actor> arrayActors = editor.actors.get(actor.getName());
		if (arrayActors == null) {
			arrayActors = new Array<Actor>();
		}
		arrayActors.add(actor);
		editor.actors.put(actor.getName(), arrayActors);

		editor.actionActors.put(option.ActionTag, actor);
	}

	/**
	 * 子控件根据zOrder属性排序
	 */
	protected void sort (final T widget, Group group) {
		group.getChildren().sort(new Comparator<Actor>() {
			@Override
			public int compare (Actor arg0, Actor arg1) {
				return getZOrder(widget, arg0.getName()) - getZOrder(widget, arg1.getName());
			}
		});

	}
}
