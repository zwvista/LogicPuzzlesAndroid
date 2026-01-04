package com.zwstudio.logicpuzzlesandroid.puzzles.wishsandwich

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class WishSandwichObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "bread" -> WishSandwichBreadObject()
            "ham" -> WishSandwichHamObject()
            "marker" -> WishSandwichMarkerObject
            else -> WishSandwichMarkerObject
        }
    }
}

object WishSandwichEmptyObject : WishSandwichObject()

class WishSandwichBreadObject(var state: AllowedObjectState = AllowedObjectState.Normal) : WishSandwichObject() {
    override fun objAsString() = "bread"
}

object WishSandwichForbiddenObject : WishSandwichObject() {
    override fun objAsString() = "forbidden"
}

class WishSandwichHamObject(var state: AllowedObjectState = AllowedObjectState.Normal) : WishSandwichObject() {
    override fun objAsString() = "ham"
}

object WishSandwichMarkerObject : WishSandwichObject() {
    override fun objAsString() = "marker"
}

class WishSandwichGameMove(val p: Position, var obj: WishSandwichObject = WishSandwichEmptyObject)
