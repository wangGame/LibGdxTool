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
package net.mwplay.cocostudio.ui.model;

import java.util.List;

/**
 * 控件
 */
public class ObjectData {
    public int FontSize;
    public boolean VisibleForFrame = true;
    public int Alpha=255;
    public int ZOrder;
    public boolean FlipX;
    public boolean FlipY;
    public boolean Scale9Enable;
    public int Scale9Width;
    public int Scale9Height;
    public int Scale9OriginX;
    public int Scale9OriginY;
    public boolean TouchEnable;
    public FileData FontResource;
    public float Rotation;
    public List<ObjectData> Children;
    public int ActionTag;
    public Size Position;
    public Scale Scale;
    public Scale AnchorPoint;
    public CColor CColor;
    public Size Size;
    public String Name;
    public String ctype;
    public String CallBackType;
    public String CallBackName;
    public float RotationSkewX;
    public float RotationSkewY;
}
