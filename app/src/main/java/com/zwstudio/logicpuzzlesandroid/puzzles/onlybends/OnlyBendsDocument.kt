package com.zwstudio.logicpuzzlesandroid.puzzles.onlybends

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class OnlyBendsDocument(context: Context) : GameDocument<OnlyBendsGameMove>(context) {
    override fun saveMove(move: OnlyBendsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        OnlyBendsGameMove(Position(rec.row, rec.col), rec.intValue1)
}