package com.kw.gdx.pathbbc;

/**
 * Clipping attachment 编辑数据。
 *
 * endSlotName 对应 Spine clipping 的 end slot：
 * 从当前 clipping slot 开始裁剪，到 end slot 结束。
 */
public class EditableClippingAttachment extends EditableAttachment {
    public String endSlotName;

    public EditableClippingAttachment(String name) {
        super(name, AttachmentKind.clipping);
        closed = true;
    }

    public void setEndSlotName(String endSlotName) {
        this.endSlotName = endSlotName;
    }
}
