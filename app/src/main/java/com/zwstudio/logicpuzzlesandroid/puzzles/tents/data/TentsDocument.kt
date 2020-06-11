package com.zwstudio.logicpuzzlesandroid.puzzles.tents.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsObject
import org.androidannotations.annotations.EBean

@EBean
class TentsDocument : GameDocument<TentsGame, TentsGameMove>() {
    override fun saveMove(move: TentsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TentsGameMove(Position(rec.row, rec.col), TentsObject.objFromString(rec.strValue1))
}