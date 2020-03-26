package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain

abstract class FourMeNotObject {
    abstract fun objAsString(): String

    companion object {
        fun objFromString(str: String?): FourMeNotObject {
            return when (str) {
                "marker" -> FourMeNotMarkerObject()
                "tree" -> FourMeNotTreeObject()
                else -> FourMeNotEmptyObject()
            }
        }
    }
}