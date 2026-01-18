package com.zwstudio.logicpuzzlesandroid.puzzles.funnynumbers

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FunnyNumbersDocument(context: Context) : GameDocument<FunnyNumbersGameMove>(context) {
    override fun saveMove(move: FunnyNumbersGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        FunnyNumbersGameMove(Position(rec.row, rec.col), FunnyNumbersObject.objFromString(rec.strValue1!!))
}