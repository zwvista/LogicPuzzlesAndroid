package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesObject
import org.androidannotations.annotations.EBean

@EBean
open class LighthousesDocument : GameDocument<LighthousesGame?, LighthousesGameMove?>() {
    protected override fun saveMove(move: LighthousesGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.strValue1 = move.obj!!.objAsString()
    }

    override fun loadMove(rec: MoveProgress): LighthousesGameMove {
        return object : LighthousesGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = LighthousesObject.Companion.objFromString(rec.strValue1)
            }
        }
    }
}