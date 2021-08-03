package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DigitalBattleShipsDocument(context: Context) : GameDocument<DigitalBattleShipsGameMove>(context) {
    override fun saveMove(move: DigitalBattleShipsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        DigitalBattleShipsGameMove(Position(rec.row, rec.col), DigitalBattleShipsObject.values()[rec.intValue1])
}