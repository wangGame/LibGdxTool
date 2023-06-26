package net.mwplay.cocostudio.ui.model.objectdata.group;

import net.mwplay.cocostudio.ui.model.CColor;
import net.mwplay.cocostudio.ui.model.FileData;
import net.mwplay.cocostudio.ui.model.objectdata.GroupData;

/**
 * Created by Administrator on 2017/7/21.
 */
public class ButtonObjectData extends GroupData {
    public String ButtonText;
    public CColor TextColor;
    public FileData PressedFileData;
    public FileData NormalFileData;
    public FileData DisabledFileData;
    public boolean DisplayState;
}
