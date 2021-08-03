package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PairakabeDocument(context: Context) : GameDocument<PairakabeGameMove>(context) {
    override fun saveMove(move: PairakabeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        PairakabeGameMove(Position(rec.row, rec.col), PairakabeObject.objTypeFromString(rec.strValue1!!))
}