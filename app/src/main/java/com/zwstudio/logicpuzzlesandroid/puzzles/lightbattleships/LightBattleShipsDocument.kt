package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LightBattleShipsDocument(context: Context) : GameDocument<LightBattleShipsGameMove>(context) {
    override fun saveMove(move: LightBattleShipsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        LightBattleShipsGameMove(Position(rec.row, rec.col), LightBattleShipsObject.objFromString(rec.strValue1!!))
}