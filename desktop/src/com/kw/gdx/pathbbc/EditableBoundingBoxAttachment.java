package com.kw.gdx.pathbbc;

/**
 * Bounding Box attachment 编辑数据。
 */
public class EditableBoundingBoxAttachment extends EditableAttachment {
    public EditableBoundingBoxAttachment(String name) {
        super(name, AttachmentKind.boundingBox);
        closed = true;
    }
}
