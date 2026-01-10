package com.zwstudio.logicpuzzlesandroid.puzzles.plugitin

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PlugItInDocument(context: Context) : GameDocument<PlugItInGameMove>(context) {
    override fun saveMove(move: PlugItInGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        PlugItInGameMove(Position(rec.row, rec.col), rec.intValue1)
}