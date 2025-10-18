package com.zwstudio.logicpuzzlesandroid.puzzles.zenlandscaper

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class ZenLandscaperObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "filled" -> ZenLandscaperFilledObject()
            "marker" -> ZenLandscaperMarkerObject
            else -> ZenLandscaperEmptyObject
        }
    }
}

object ZenLandscaperEmptyObject : ZenLandscaperObject()

class ZenLandscaperFilledObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ZenLandscaperObject() {
    override fun objAsString() = "filled"
}

object ZenLandscaperForbiddenObject : ZenLandscaperObject() {
    override fun objAsString() = "forbidden"
}

object ZenLandscaperMarkerObject : ZenLandscaperObject() {
    override fun objAsString() = "marker"
}

class ZenLandscaperGameMove(val p: Position, var obj: ZenLandscaperObject = ZenLandscaperEmptyObject)
