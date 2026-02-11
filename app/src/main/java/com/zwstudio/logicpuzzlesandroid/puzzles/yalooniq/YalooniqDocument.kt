package com.zwstudio.logicpuzzlesandroid.puzzles.yalooniq

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class YalooniqDocument(context: Context) : GameDocument<YalooniqGameMove>(context) {
    override fun saveMove(move: YalooniqGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        YalooniqGameMove(Position(rec.row, rec.col), rec.intValue1)
}