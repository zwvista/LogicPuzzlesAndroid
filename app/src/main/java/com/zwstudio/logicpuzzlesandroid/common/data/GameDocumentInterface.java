package com.zwstudio.logicpuzzlesandroid.common.data;

/**
 * Created by TCC-2-9002 on 2016/10/27.
 */

public interface GameDocumentInterface {
    int getMarkerOption();
    void setMarkerOption(GameProgress rec, int o);
    boolean isAllowedObjectsOnly();
    void setAllowedObjectsOnly(GameProgress rec, boolean o);
}
