package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MinesweeperDocument(context: Context) : GameDocument<MinesweeperGameMove>(context) {
    override fun saveMove(move: MinesweeperGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        MinesweeperGameMove(Position(rec.row, rec.col), MinesweeperObject.objFromString(rec.strValue1!!))
}