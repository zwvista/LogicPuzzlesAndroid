package com.zwstudio.logicpuzzlesandroid.puzzles.adifferentfarmer

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ADifferentFarmerDocument(context: Context) : GameDocument<ADifferentFarmerGameMove>(context) {
    override fun saveMove(move: ADifferentFarmerGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        ADifferentFarmerGameMove(Position(rec.row, rec.col), ADifferentFarmerObject.entries[rec.intValue1])
}