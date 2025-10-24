package com.zwstudio.logicpuzzlesandroid.puzzles.landscaper

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LandscaperDocument(context: Context) : GameDocument<LandscaperGameMove>(context) {
    override fun saveMove(move: LandscaperGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        LandscaperGameMove(Position(rec.row, rec.col), LandscaperObject.entries[rec.intValue1])
}