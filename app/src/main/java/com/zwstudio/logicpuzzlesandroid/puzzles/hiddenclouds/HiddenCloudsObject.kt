package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenclouds

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class HiddenCloudsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> HiddenCloudsMarkerObject
            "cloud" -> HiddenCloudsCloudObject()
            else -> HiddenCloudsEmptyObject
        }
    }
}

object HiddenCloudsEmptyObject : HiddenCloudsObject()

object HiddenCloudsMarkerObject : HiddenCloudsObject() {
    override fun objAsString() = "marker"
}

object HiddenCloudsForbiddenObject : HiddenCloudsObject() {
    override fun objAsString() = "forbidden"
}

class HiddenCloudsCloudObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HiddenCloudsObject() {
    override fun objAsString() = "cloud"
}

class HiddenCloudsGameMove(val p: Position, var obj: HiddenCloudsObject = HiddenCloudsEmptyObject)
