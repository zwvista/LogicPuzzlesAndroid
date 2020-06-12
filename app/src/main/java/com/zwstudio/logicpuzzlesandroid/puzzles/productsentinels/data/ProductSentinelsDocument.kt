package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsObject
import org.androidannotations.annotations.EBean

@EBean
class ProductSentinelsDocument : GameDocument<ProductSentinelsGame, ProductSentinelsGameMove>() {
    override fun saveMove(move: ProductSentinelsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ProductSentinelsGameMove(Position(rec.row, rec.col), ProductSentinelsObject.objFromString(rec.strValue1!!))
}