package com.zwstudio.logicpuzzlesandroid.puzzles.shopandgas

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ShopAndGasDocument(context: Context) : GameDocument<ShopAndGasGameMove>(context) {
    override fun saveMove(move: ShopAndGasGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        ShopAndGasGameMove(Position(rec.row, rec.col), rec.intValue1)
}