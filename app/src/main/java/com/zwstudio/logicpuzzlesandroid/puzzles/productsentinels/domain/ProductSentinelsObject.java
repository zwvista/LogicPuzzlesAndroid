package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain;


public abstract class ProductSentinelsObject {
    public abstract String objAsString();
    public static ProductSentinelsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new ProductSentinelsMarkerObject();
            case "tower":
                return new ProductSentinelsTowerObject();
            default:
                return new ProductSentinelsMarkerObject();
        }
    }
}
