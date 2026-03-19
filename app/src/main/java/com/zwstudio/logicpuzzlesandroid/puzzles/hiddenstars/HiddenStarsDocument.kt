package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstars

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HiddenStarsDocument(context: Context) : GameDocument<HiddenStarsGameMove>(context) {
    override fun saveMove(move: HiddenStarsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        HiddenStarsGameMove(Position(rec.row, rec.col), HiddenStarsObject.entries[rec.intValue1])
}