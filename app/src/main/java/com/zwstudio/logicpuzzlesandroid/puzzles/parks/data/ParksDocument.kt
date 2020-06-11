package com.zwstudio.logicpuzzlesandroid.puzzles.parks.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGame
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksObject
import org.androidannotations.annotations.EBean

@EBean
class ParksDocument : GameDocument<ParksGame, ParksGameMove>() {
    override fun saveMove(move: ParksGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ParksGameMove(Position(rec.row, rec.col), ParksObject.objFromString(rec.strValue1))
}