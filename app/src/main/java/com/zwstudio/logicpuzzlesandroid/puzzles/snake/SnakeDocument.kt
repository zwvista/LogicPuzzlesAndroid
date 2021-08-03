package com.zwstudio.logicpuzzlesandroid.puzzles.snake

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeDocument(context: Context) : GameDocument<SnakeGameMove>(context) {
    override fun saveMove(move: SnakeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        SnakeGameMove(Position(rec.row, rec.col), SnakeObject.values()[rec.intValue1])
}