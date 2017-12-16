package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain;


public abstract class LightBattleShipsObject {
    public abstract String objAsString();
    public static LightBattleShipsObject objFromString(String str) {
        switch (str) {
        case "marker":
            return new LightBattleShipsMarkerObject();
        case "battleShipTop":
            return new LightBattleShipsBattleShipTopObject();
        case "battleShipBottom":
            return new LightBattleShipsBattleShipBottomObject();
        case "battleShipLeft":
            return new LightBattleShipsBattleShipLeftObject();
        case "battleShipRight":
            return new LightBattleShipsBattleShipRightObject();
        case "battleShipMiddle":
            return new LightBattleShipsBattleShipMiddleObject();
        case "battleShipUnit":
            return new LightBattleShipsBattleShipUnitObject();
        default:
            return new LightBattleShipsEmptyObject();
        }
    }
}
