package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGame
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain.BusySeasObject
import org.androidannotations.annotations.EBean

@EBean
class BusySeasDocument : GameDocument<BusySeasGame, BusySeasGameMove>() {
    override fun saveMove(move: BusySeasGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BusySeasGameMove(Position(rec.row, rec.col), BusySeasObject.objFromString(rec.strValue1))
}
