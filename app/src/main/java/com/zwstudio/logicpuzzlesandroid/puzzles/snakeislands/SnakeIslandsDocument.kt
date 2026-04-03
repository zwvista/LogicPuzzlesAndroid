package com.zwstudio.logicpuzzlesandroid.puzzles.snakeislands

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeIslandsDocument(context: Context) : GameDocument<SnakeIslandsGameMove>(context) {
    override fun saveMove(move: SnakeIslandsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        SnakeIslandsGameMove(Position(rec.row, rec.col), SnakeIslandsObject.entries[rec.intValue1])
}