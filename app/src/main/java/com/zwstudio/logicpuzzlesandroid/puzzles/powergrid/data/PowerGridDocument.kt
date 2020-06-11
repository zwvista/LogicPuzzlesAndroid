package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGame
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain.PowerGridObject
import org.androidannotations.annotations.EBean

@EBean
class PowerGridDocument : GameDocument<PowerGridGame, PowerGridGameMove>() {
    override fun saveMove(move: PowerGridGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        PowerGridGameMove(Position(rec.row, rec.col), PowerGridObject.objFromString(rec.strValue1))
}