package com.zwstudio.logicpuzzlesandroid.puzzles.landscapes

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LandscapesDocument(context: Context) : GameDocument<LandscapesGameMove>(context) {
    override fun saveMove(move: LandscapesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        LandscapesGameMove(Position(rec.row, rec.col), LandscapesObject.entries[rec.intValue1])
}