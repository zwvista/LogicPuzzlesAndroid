package com.zwstudio.logicpuzzlesandroid.puzzles.zengardens

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ZenGardensDocument(context: Context) : GameDocument<ZenGardensGameMove>(context) {
    override fun saveMove(move: ZenGardensGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        ZenGardensGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}