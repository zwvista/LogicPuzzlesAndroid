package com.zwstudio.logicpuzzlesandroid.puzzles.rabbits

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class RabbitsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> RabbitsMarkerObject
            "rabbit" -> RabbitsRabbitObject()
            "tree" -> RabbitsTreeObject()
            else -> RabbitsMarkerObject
        }
    }
}

object RabbitsEmptyObject : RabbitsObject()

object RabbitsForbiddenObject : RabbitsObject() {
    override fun objAsString() = "forbidden"
}

class RabbitsHintObject(var state: HintState = HintState.Normal) : RabbitsObject() {
    override fun objAsString() = "hint"
}

object RabbitsMarkerObject : RabbitsObject() {
    override fun objAsString() = "marker"
}

class RabbitsRabbitObject(var state: AllowedObjectState = AllowedObjectState.Normal) : RabbitsObject() {
    override fun objAsString() = "rabbit"
}

class RabbitsTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : RabbitsObject() {
    override fun objAsString() = "tree"
}

class RabbitsGameMove(val p: Position, var obj: RabbitsObject = RabbitsEmptyObject)
