package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain

abstract class HolidayIslandObject {
    abstract fun objAsString(): String

    companion object {
        fun objFromString(str: String?): HolidayIslandObject {
            return when (str) {
                "marker" -> HolidayIslandMarkerObject()
                "tree" -> HolidayIslandTreeObject()
                else -> HolidayIslandEmptyObject()
            }
        }
    }
}