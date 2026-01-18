package com.zwstudio.logicpuzzlesandroid.puzzles.pathonthemountains

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PathOnTheMountainsDocument(context: Context) : GameDocument<PathOnTheMountainsGameMove>(context) {
    override fun saveMove(move: PathOnTheMountainsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        PathOnTheMountainsGameMove(Position(rec.row, rec.col), rec.intValue1)
}