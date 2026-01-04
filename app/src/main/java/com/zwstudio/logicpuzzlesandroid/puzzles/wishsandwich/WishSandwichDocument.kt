package com.zwstudio.logicpuzzlesandroid.puzzles.wishsandwich

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WishSandwichDocument(context: Context) : GameDocument<WishSandwichGameMove>(context) {
    override fun saveMove(move: WishSandwichGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        WishSandwichGameMove(Position(rec.row, rec.col), WishSandwichObject.objFromString(rec.strValue1!!))
}