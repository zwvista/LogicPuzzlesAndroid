package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class ProductSentinelsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> ProductSentinelsMarkerObject()
            "tower" -> ProductSentinelsTowerObject()
            else -> ProductSentinelsMarkerObject()
        }
    }
}

class ProductSentinelsEmptyObject : ProductSentinelsObject()

class ProductSentinelsForbiddenObject : ProductSentinelsObject() {
    override fun objAsString() = "forbidden"
}

class ProductSentinelsHintObject(var state: HintState = HintState.Normal) : ProductSentinelsObject() {
    override fun objAsString() = "hint"
}

class ProductSentinelsMarkerObject : ProductSentinelsObject() {
    override fun objAsString() = "marker"
}

class ProductSentinelsTowerObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ProductSentinelsObject() {
    override fun objAsString() = "tower"
}

class ProductSentinelsGameMove(val p: Position, var obj: ProductSentinelsObject = ProductSentinelsEmptyObject())
