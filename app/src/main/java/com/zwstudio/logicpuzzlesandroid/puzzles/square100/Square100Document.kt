package com.zwstudio.logicpuzzlesandroid.puzzles.square100

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class Square100Document : GameDocument<Square100GameMove>() {
    override fun saveMove(move: Square100GameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        Square100GameMove(Position(rec.row, rec.col), obj = rec.strValue1!!)
}