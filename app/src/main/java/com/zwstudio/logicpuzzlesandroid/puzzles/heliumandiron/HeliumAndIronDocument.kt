package com.zwstudio.logicpuzzlesandroid.puzzles.heliumandiron

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HeliumAndIronDocument(context: Context) : GameDocument<HeliumAndIronGameMove>(context) {
    override fun saveMove(move: HeliumAndIronGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        HeliumAndIronGameMove(Position(rec.row, rec.col), HeliumAndIronObject.entries[rec.intValue1])
}