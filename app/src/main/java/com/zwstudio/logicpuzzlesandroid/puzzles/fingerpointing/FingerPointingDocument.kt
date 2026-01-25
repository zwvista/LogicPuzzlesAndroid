package com.zwstudio.logicpuzzlesandroid.puzzles.fingerpointing

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FingerPointingDocument(context: Context) : GameDocument<FingerPointingGameMove>(context) {
    override fun saveMove(move: FingerPointingGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        FingerPointingGameMove(Position(rec.row, rec.col), FingerPointingObject.entries[rec.intValue1])
}