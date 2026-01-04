package com.zwstudio.logicpuzzlesandroid.puzzles.wishsandwich

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class WishSandwichObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> WishSandwichMarkerObject
            "tower" -> WishSandwichPostObject()
            else -> WishSandwichMarkerObject
        }
    }
}

object WishSandwichEmptyObject : WishSandwichObject()

object WishSandwichForbiddenObject : WishSandwichObject() {
    override fun objAsString() = "forbidden"
}

object WishSandwichMarkerObject : WishSandwichObject() {
    override fun objAsString() = "marker"
}

class WishSandwichPostObject(var state: AllowedObjectState = AllowedObjectState.Normal) : WishSandwichObject() {
    override fun objAsString() = "tower"
}

class WishSandwichGameMove(val p: Position, var obj: WishSandwichObject = WishSandwichEmptyObject)
