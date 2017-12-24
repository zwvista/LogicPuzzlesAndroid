package com.zwstudio.logicpuzzlesandroid.common.data;

public interface GameDocumentInterface {
    int getMarkerOption();
    void setMarkerOption(GameProgress rec, int o);
    boolean isAllowedObjectsOnly();
    void setAllowedObjectsOnly(GameProgress rec, boolean o);
}
