package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGame
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain.MinesweeperObject
import org.androidannotations.annotations.EBean

@EBean
class MinesweeperDocument : GameDocument<MinesweeperGame, MinesweeperGameMove>() {
    override fun saveMove(move: MinesweeperGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        MinesweeperGameMove(Position(rec.row, rec.col), MinesweeperObject.objFromString(rec.strValue1))
}