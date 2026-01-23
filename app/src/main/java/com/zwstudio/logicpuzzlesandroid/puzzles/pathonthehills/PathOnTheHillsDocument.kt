package com.zwstudio.logicpuzzlesandroid.puzzles.pathonthehills

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PathOnTheHillsDocument(context: Context) : GameDocument<PathOnTheHillsGameMove>(context) {
    override fun saveMove(move: PathOnTheHillsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        PathOnTheHillsGameMove(Position(rec.row, rec.col), rec.intValue1)
}