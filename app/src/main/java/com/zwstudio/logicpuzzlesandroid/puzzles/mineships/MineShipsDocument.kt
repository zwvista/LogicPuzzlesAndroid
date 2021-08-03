package com.zwstudio.logicpuzzlesandroid.puzzles.mineships

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MineShipsDocument(context: Context) : GameDocument<MineShipsGameMove>(context) {
    override fun saveMove(move: MineShipsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        MineShipsGameMove(Position(rec.row, rec.col), MineShipsObject.objFromString(rec.strValue1!!))
}