package com.zwstudio.logicpuzzlesandroid.puzzles.culturedbranches

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class CulturedBranchesObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "up" -> CulturedBranchesUpObject
            "right" -> CulturedBranchesRightObject
            "down" -> CulturedBranchesDownObject
            "left" -> CulturedBranchesLeftObject
            "horizontal" -> CulturedBranchesHorizontalObject
            "vertical" -> CulturedBranchesVerticalObject
            else -> CulturedBranchesEmptyObject
        }
    }
}

object CulturedBranchesEmptyObject : CulturedBranchesObject()

class CulturedBranchesHintObject(var state: HintState = HintState.Normal) : CulturedBranchesObject() {
    override fun objAsString() = "hint"
}

object CulturedBranchesUpObject : CulturedBranchesObject() {
    override fun objAsString() = "up"
}

object CulturedBranchesRightObject : CulturedBranchesObject() {
    override fun objAsString() = "right"
}

object CulturedBranchesDownObject : CulturedBranchesObject() {
    override fun objAsString() = "down"
}

object CulturedBranchesLeftObject : CulturedBranchesObject() {
    override fun objAsString() = "left"
}

object CulturedBranchesHorizontalObject : CulturedBranchesObject() {
    override fun objAsString() = "horizontal"
}

object CulturedBranchesVerticalObject : CulturedBranchesObject() {
    override fun objAsString() = "vertical"
}

class CulturedBranchesGameMove(val p: Position, var obj: CulturedBranchesObject = CulturedBranchesEmptyObject)
