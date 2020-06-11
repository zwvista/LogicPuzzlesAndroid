package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGame
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameMove
import org.androidannotations.annotations.EBean

@EBean
class NumberLinkDocument : GameDocument<NumberLinkGame, NumberLinkGameMove>() {
    override fun saveMove(move: NumberLinkGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        NumberLinkGameMove(Position(rec.row, rec.col), rec.intValue1)
}