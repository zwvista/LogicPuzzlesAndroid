package com.zwstudio.logicpuzzlesandroid.puzzles.wishsandwich

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class WishSandwichObject {
    Empty, Forbidden, Hint, Marker,
    Bread, Ham, 
}

class WishSandwichGameMove(val p: Position, var obj: WishSandwichObject = WishSandwichObject.Empty)
