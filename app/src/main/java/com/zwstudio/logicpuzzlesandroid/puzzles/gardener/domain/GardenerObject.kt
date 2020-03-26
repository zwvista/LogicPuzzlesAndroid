package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain

abstract class GardenerObject {
    abstract fun objAsString(): String

    companion object {
        fun objFromString(str: String?): GardenerObject {
            return when (str) {
                "marker" -> GardenerMarkerObject()
                "tree" -> GardenerTreeObject()
                else -> GardenerEmptyObject()
            }
        }
    }
}