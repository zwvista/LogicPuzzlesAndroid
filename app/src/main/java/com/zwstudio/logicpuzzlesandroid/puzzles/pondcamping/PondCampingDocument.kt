package com.zwstudio.logicpuzzlesandroid.puzzles.pondcamping

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PondCampingDocument(context: Context) : GameDocument<PondCampingGameMove>(context) {
    override fun saveMove(move: PondCampingGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        PondCampingGameMove(Position(rec.row, rec.col), PondCampingObject.entries[rec.intValue1])
}