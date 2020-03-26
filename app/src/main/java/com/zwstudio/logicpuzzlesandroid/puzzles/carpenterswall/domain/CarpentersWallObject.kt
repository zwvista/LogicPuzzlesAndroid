package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain

abstract class CarpentersWallObject {
    abstract fun objAsString(): String
    open val isHint: Boolean
        get() = false

    companion object {
        fun objTypeFromString(str: String?): CarpentersWallObject {
            return when (str) {
                "wall" -> CarpentersWallWallObject()
                "marker" -> CarpentersWallMarkerObject()
                else -> CarpentersWallEmptyObject()
            }
        }
    }
}