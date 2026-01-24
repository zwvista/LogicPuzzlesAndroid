package com.zwstudio.logicpuzzlesandroid.puzzles.youturnmeon

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class YouTurnMeOnDocument(context: Context) : GameDocument<YouTurnMeOnGameMove>(context) {
    override fun saveMove(move: YouTurnMeOnGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        YouTurnMeOnGameMove(Position(rec.row, rec.col), rec.intValue1)
}