package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NurikabeDocument(context: Context) : GameDocument<NurikabeGameMove>(context) {
    override fun saveMove(move: NurikabeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        NurikabeGameMove(Position(rec.row, rec.col), NurikabeObject.entries[rec.intValue1])
}