package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGame
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameMove
import org.androidannotations.annotations.EBean

@EBean
class MathraxDocument : GameDocument<MathraxGame, MathraxGameMove>() {
    override fun saveMove(move: MathraxGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        MathraxGameMove(Position(rec.row, rec.col), rec.intValue1)
}