package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class PipemaniaObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> PipemaniaMarkerObject
            "flower" -> PipemaniaFlowerObject()
            else -> PipemaniaEmptyObject
        }
    }
}

object PipemaniaBlockObject : PipemaniaObject() {
    override fun objAsString() = "block"
}

object PipemaniaEmptyObject : PipemaniaObject()


object PipemaniaForbiddenObject : PipemaniaObject() {
    override fun objAsString() = "forbidden"
}

object PipemaniaMarkerObject : PipemaniaObject() {
    override fun objAsString() = "marker"
}

class PipemaniaFlowerObject(var state: AllowedObjectState = AllowedObjectState.Normal) : PipemaniaObject() {
    override fun objAsString() = "flower"
}

class PipemaniaGameMove(val p: Position, var obj: PipemaniaObject = PipemaniaEmptyObject)
