package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FillominoDocument(context: Context) : GameDocument<FillominoGameMove>(context) {
    override fun saveMove(move: FillominoGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        FillominoGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}