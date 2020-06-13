package com.zwstudio.logicpuzzlesandroid.puzzles.abc

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class AbcDocument : GameDocument<AbcGame, AbcGameMove>() {
    override fun saveMove(move: AbcGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        AbcGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}
