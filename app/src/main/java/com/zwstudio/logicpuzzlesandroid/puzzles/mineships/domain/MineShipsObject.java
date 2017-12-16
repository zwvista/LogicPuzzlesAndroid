package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain;

public abstract class MineShipsObject {
    public abstract String objAsString();
    public static MineShipsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new MineShipsMarkerObject();
            case "battleShipTop":
                return new MineShipsBattleShipTopObject();
            case "battleShipBottom":
                return new MineShipsBattleShipBottomObject();
            case "battleShipLeft":
                return new MineShipsBattleShipLeftObject();
            case "battleShipRight":
                return new MineShipsBattleShipRightObject();
            case "battleShipMiddle":
                return new MineShipsBattleShipMiddleObject();
            case "battleShipUnit":
                return new MineShipsBattleShipUnitObject();
            default:
                return new MineShipsEmptyObject();
        }
    }
}
