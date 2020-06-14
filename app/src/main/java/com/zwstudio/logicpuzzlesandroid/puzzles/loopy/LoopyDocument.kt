package com.zwstudio.logicpuzzlesandroid.puzzles.loopy

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class LoopyDocument : GameDocument<LoopyGameMove>() {
    override fun saveMove(move: LoopyGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        LoopyGameMove(Position(rec.row, rec.col), rec.intValue1)
}