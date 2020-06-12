package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain.NoughtsAndCrossesGameMove
import org.androidannotations.annotations.EBean

@EBean
class NoughtsAndCrossesDocument : GameDocument<NoughtsAndCrossesGame, NoughtsAndCrossesGameMove>() {
    override fun saveMove(move: NoughtsAndCrossesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        NoughtsAndCrossesGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}