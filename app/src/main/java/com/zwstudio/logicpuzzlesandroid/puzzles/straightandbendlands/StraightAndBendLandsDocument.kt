package com.zwstudio.logicpuzzlesandroid.puzzles.straightandbendlands

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class StraightAndBendLandsDocument(context: Context) : GameDocument<StraightAndBendLandsGameMove>(context) {
    override fun saveMove(move: StraightAndBendLandsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        StraightAndBendLandsGameMove(Position(rec.row, rec.col), rec.intValue1)
}