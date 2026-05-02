package com.zwstudio.logicpuzzlesandroid.puzzles.farmer

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FarmerDocument(context: Context) : GameDocument<FarmerGameMove>(context) {
    override fun saveMove(move: FarmerGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        FarmerGameMove(Position(rec.row, rec.col), FarmerObject.entries[rec.intValue1])
}