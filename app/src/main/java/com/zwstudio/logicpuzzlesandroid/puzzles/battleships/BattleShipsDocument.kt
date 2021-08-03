package com.zwstudio.logicpuzzlesandroid.puzzles.battleships

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BattleShipsDocument(context: Context) : GameDocument<BattleShipsGameMove>(context) {
    override fun saveMove(move: BattleShipsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        BattleShipsGameMove(Position(rec.row, rec.col), BattleShipsObject.values()[rec.intValue1])
}
