package com.zwstudio.logicpuzzlesandroid.puzzles.thegreylabyrinth

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheGreyLabyrinthDocument(context: Context) : GameDocument<TheGreyLabyrinthGameMove>(context) {
    override fun saveMove(move: TheGreyLabyrinthGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        TheGreyLabyrinthGameMove(Position(rec.row, rec.col), TheGreyLabyrinthObject.entries[rec.intValue1])
}