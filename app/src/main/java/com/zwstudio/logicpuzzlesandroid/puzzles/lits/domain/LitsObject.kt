package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain

abstract class LitsObject {
    abstract fun objAsString(): String

    companion object {
        fun objFromString(str: String?): LitsObject {
            return when (str) {
                "marker" -> LitsMarkerObject()
                "tree" -> LitsTreeObject()
                else -> LitsEmptyObject()
            }
        }
    }
}