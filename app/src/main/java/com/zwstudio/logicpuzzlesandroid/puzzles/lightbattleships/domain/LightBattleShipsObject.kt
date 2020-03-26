package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain

abstract class LightBattleShipsObject {
    abstract fun objAsString(): String

    companion object {
        fun objFromString(str: String?): LightBattleShipsObject {
            return when (str) {
                "marker" -> LightBattleShipsMarkerObject()
                "battleShipTop" -> LightBattleShipsBattleShipTopObject()
                "battleShipBottom" -> LightBattleShipsBattleShipBottomObject()
                "battleShipLeft" -> LightBattleShipsBattleShipLeftObject()
                "battleShipRight" -> LightBattleShipsBattleShipRightObject()
                "battleShipMiddle" -> LightBattleShipsBattleShipMiddleObject()
                "battleShipUnit" -> LightBattleShipsBattleShipUnitObject()
                else -> LightBattleShipsEmptyObject()
            }
        }
    }
}