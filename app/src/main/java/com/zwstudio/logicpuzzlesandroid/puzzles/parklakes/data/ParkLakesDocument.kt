package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesObject
import org.androidannotations.annotations.EBean

@EBean
class ParkLakesDocument : GameDocument<ParkLakesGame, ParkLakesGameMove>() {
    override fun saveMove(move: ParkLakesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ParkLakesGameMove(Position(rec.row, rec.col), ParkLakesObject.objFromString(rec.strValue1))
}