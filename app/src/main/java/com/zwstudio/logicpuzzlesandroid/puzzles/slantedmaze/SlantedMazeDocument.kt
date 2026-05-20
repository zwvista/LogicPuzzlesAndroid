package com.zwstudio.logicpuzzlesandroid.puzzles.slantedmaze

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SlantedMazeDocument(context: Context) : GameDocument<SlantedMazeGameMove>(context) {
    override fun saveMove(move: SlantedMazeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        SlantedMazeGameMove(Position(rec.row, rec.col), SlantedMazeObject.entries[rec.intValue1])
}
