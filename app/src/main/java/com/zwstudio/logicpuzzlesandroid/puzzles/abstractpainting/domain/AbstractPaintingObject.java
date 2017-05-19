package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class AbstractPaintingObject {
    public abstract String objAsString();
    public static AbstractPaintingObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new AbstractPaintingMarkerObject();
            case "tree":
                return new AbstractPaintingTreeObject();
            default:
                return new AbstractPaintingEmptyObject();
        }
    }
}
