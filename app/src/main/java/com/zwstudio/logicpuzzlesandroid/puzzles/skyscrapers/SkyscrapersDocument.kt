package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SkyscrapersDocument(context: Context) : GameDocument<SkyscrapersGameMove>(context) {
    override fun saveMove(move: SkyscrapersGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        SkyscrapersGameMove(Position(rec.row, rec.col), rec.intValue1)
}