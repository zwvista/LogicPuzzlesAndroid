package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ProductSentinelsDocument(context: Context) : GameDocument<ProductSentinelsGameMove>(context) {
    override fun saveMove(move: ProductSentinelsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ProductSentinelsGameMove(Position(rec.row, rec.col), ProductSentinelsObject.objFromString(rec.strValue1!!))
}