package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesObject
import org.androidannotations.annotations.EBean

@EBean
class LighthousesDocument : GameDocument<LighthousesGame, LighthousesGameMove>() {
    override fun saveMove(move: LighthousesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        LighthousesGameMove(Position(rec.row, rec.col), LighthousesObject.objFromString(rec.strValue1!!))
}