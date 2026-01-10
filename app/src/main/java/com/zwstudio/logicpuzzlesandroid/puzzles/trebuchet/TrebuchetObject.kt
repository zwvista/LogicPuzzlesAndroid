package com.zwstudio.logicpuzzlesandroid.puzzles.trebuchet

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TrebuchetObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> TrebuchetMarkerObject
            "dune" -> TrebuchetDuneObject()
            else -> TrebuchetEmptyObject
        }
    }
}

object TrebuchetEmptyObject : TrebuchetObject()

class TrebuchetHintObject(var state: HintState = HintState.Normal) : TrebuchetObject() {
    override fun objAsString() = "hint"
}

object TrebuchetMarkerObject : TrebuchetObject() {
    override fun objAsString() = "marker"
}

object TrebuchetForbiddenObject : TrebuchetObject() {
    override fun objAsString() = "forbidden"
}

class TrebuchetDuneObject(var state: AllowedObjectState = AllowedObjectState.Normal) : TrebuchetObject() {
    override fun objAsString() = "dune"
}

class TrebuchetGameMove(val p: Position, var obj: TrebuchetObject = TrebuchetEmptyObject)
