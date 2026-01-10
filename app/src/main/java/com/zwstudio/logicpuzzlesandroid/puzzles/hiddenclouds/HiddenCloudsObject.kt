package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenclouds

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class HiddenCloudsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> HiddenCloudsMarkerObject
            "dune" -> HiddenCloudsDuneObject()
            else -> HiddenCloudsEmptyObject
        }
    }
}

object HiddenCloudsEmptyObject : HiddenCloudsObject()

class HiddenCloudsHintObject(var state: HintState = HintState.Normal) : HiddenCloudsObject() {
    override fun objAsString() = "hint"
}

object HiddenCloudsMarkerObject : HiddenCloudsObject() {
    override fun objAsString() = "marker"
}

object HiddenCloudsForbiddenObject : HiddenCloudsObject() {
    override fun objAsString() = "forbidden"
}

class HiddenCloudsDuneObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HiddenCloudsObject() {
    override fun objAsString() = "dune"
}

class HiddenCloudsGameMove(val p: Position, var obj: HiddenCloudsObject = HiddenCloudsEmptyObject)
