package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ProductSentinelsObject {
    Empty, Forbidden, Hint, Marker, Tower
}

class ProductSentinelsGameMove(val p: Position, var obj: ProductSentinelsObject = ProductSentinelsObject.Empty)
