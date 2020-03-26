package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain

abstract class LighthousesObject {
    abstract fun objAsString(): String

    companion object {
        fun objFromString(str: String?): LighthousesObject {
            return when (str) {
                "marker" -> LighthousesMarkerObject()
                "lighthouse" -> LighthousesLighthouseObject()
                else -> LighthousesMarkerObject()
            }
        }
    }
}