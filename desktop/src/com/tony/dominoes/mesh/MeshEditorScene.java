package com.tony.dominoes.mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MeshEditorScene {
    private final List<MeshAttachmentModel> attachments = new ArrayList<MeshAttachmentModel>();
    private int currentIndex = -1;

    public MeshAttachmentModel addAttachment(String name, EditableMesh mesh) {
        MeshAttachmentModel attachment = new MeshAttachmentModel(name, mesh);
        attachments.add(attachment);
        if (currentIndex < 0) {
            currentIndex = 0;
        }
        return attachment;
    }

    public MeshAttachmentModel replaceAttachment(int index, String name, EditableMesh mesh) {
        if (index < 0 || index >= attachments.size()) {
            throw new IndexOutOfBoundsException("attachment index: " + index);
        }
        MeshAttachmentModel attachment = new MeshAttachmentModel(name, mesh);
        attachments.set(index, attachment);
        return attachment;
    }

    public int currentIndex() {
        return currentIndex;
    }

    public List<MeshAttachmentModel> attachments() {
        return Collections.unmodifiableList(attachments);
    }

    public MeshAttachmentModel currentAttachment() {
        if (currentIndex < 0 || currentIndex >= attachments.size()) {
            return null;
        }
        return attachments.get(currentIndex);
    }

    public void setCurrentAttachment(int index) {
        if (index < 0 || index >= attachments.size()) {
            throw new IndexOutOfBoundsException("attachment index: " + index);
        }
        currentIndex = index;
    }

    public void isolateCurrentAttachment(boolean isolated) {
        MeshAttachmentModel current = currentAttachment();
        if (current == null) {
            return;
        }
        for (MeshAttachmentModel attachment : attachments) {
            attachment.display().setIsolated(false);
        }
        current.display().setIsolated(isolated);
    }

    public void clearIsolation() {
        for (MeshAttachmentModel attachment : attachments) {
            attachment.display().setIsolated(false);
        }
    }

    public List<MeshAttachmentModel> visibleAttachments() {
        MeshAttachmentModel isolated = isolatedAttachment();
        if (isolated != null) {
            return Collections.singletonList(isolated);
        }
        return Collections.unmodifiableList(attachments);
    }

    private MeshAttachmentModel isolatedAttachment() {
        for (MeshAttachmentModel attachment : attachments) {
            if (attachment.display().isolated()) {
                return attachment;
            }
        }
        return null;
    }
}
