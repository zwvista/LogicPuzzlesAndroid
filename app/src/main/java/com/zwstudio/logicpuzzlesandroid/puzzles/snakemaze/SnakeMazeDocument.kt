package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeMazeDocument(context: Context) : GameDocument<SnakeMazeGameMove>(context) {
    override fun saveMove(move: SnakeMazeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        SnakeMazeGameMove(Position(rec.row, rec.col), rec.intValue1)
}