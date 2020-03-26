package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain

abstract class LightenUpObject {
    var lightness = 0
    abstract fun objTypeAsString(): String

    companion object {
        fun objTypeFromString(str: String?): LightenUpObject {
            return when (str) {
                "lightbulb" -> LightenUpLightbulbObject()
                "marker" -> LightenUpMarkerObject()
                else -> LightenUpEmptyObject()
            }
        }
    }
}