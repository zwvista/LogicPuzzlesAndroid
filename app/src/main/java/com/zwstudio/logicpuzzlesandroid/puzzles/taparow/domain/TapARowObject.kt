package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain

abstract class TapARowObject {
    abstract fun objTypeAsString(): String

    companion object {
        fun objTypeFromString(str: String?): TapARowObject {
            return when (str) {
                "marker" -> TapARowMarkerObject()
                "wall" -> TapARowWallObject()
                else -> TapARowEmptyObject()
            }
        }
    }
}