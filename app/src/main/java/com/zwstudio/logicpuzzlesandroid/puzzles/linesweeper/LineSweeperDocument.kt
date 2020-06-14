package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class LineSweeperDocument : GameDocument<LineSweeperGameMove>() {
    override fun saveMove(move: LineSweeperGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        LineSweeperGameMove(Position(rec.row, rec.col), rec.intValue1)
}