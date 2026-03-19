package com.zwstudio.logicpuzzlesandroid.puzzles.joinme

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class JoinMeDocument(context: Context) : GameDocument<JoinMeGameMove>(context) {
    override fun saveMove(move: JoinMeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        JoinMeGameMove(Position(rec.row, rec.col), rec.intValue1)
}