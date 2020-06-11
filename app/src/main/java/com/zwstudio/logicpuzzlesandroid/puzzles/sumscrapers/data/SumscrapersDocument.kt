package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGame
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGameMove
import org.androidannotations.annotations.EBean

@EBean
class SumscrapersDocument : GameDocument<SumscrapersGame, SumscrapersGameMove>() {
    override fun saveMove(move: SumscrapersGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        SumscrapersGameMove(Position(rec.row, rec.col), rec.intValue1)
}