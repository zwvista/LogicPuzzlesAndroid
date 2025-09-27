package com.zwstudio.logicpuzzlesandroid.puzzles.branches

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class BranchesObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "up" -> BranchesUpObject
            "right" -> BranchesRightObject
            "down" -> BranchesDownObject
            "left" -> BranchesLeftObject
            "horizontal" -> BranchesHorizontalObject
            "vertical" -> BranchesVerticalObject
            else -> BranchesEmptyObject
        }
    }
}

object BranchesEmptyObject : BranchesObject()

class BranchesHintObject(var state: HintState = HintState.Normal) : BranchesObject() {
    override fun objAsString() = "hint"
}

object BranchesUpObject : BranchesObject() {
    override fun objAsString() = "up"
}

object BranchesRightObject : BranchesObject() {
    override fun objAsString() = "right"
}

object BranchesDownObject : BranchesObject() {
    override fun objAsString() = "down"
}

object BranchesLeftObject : BranchesObject() {
    override fun objAsString() = "left"
}

object BranchesHorizontalObject : BranchesObject() {
    override fun objAsString() = "horizontal"
}

object BranchesVerticalObject : BranchesObject() {
    override fun objAsString() = "vertical"
}

class BranchesGameMove(val p: Position, var obj: BranchesObject = BranchesEmptyObject)
