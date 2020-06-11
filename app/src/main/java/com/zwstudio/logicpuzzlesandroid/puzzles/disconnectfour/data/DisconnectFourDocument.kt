package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGame
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourObject
import org.androidannotations.annotations.EBean

@EBean
class DisconnectFourDocument : GameDocument<DisconnectFourGame, DisconnectFourGameMove>() {
    protected override fun saveMove(move: DisconnectFourGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress): DisconnectFourGameMove {
        return object : DisconnectFourGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = DisconnectFourObject.values()[rec.intValue1]
            }
        }
    }
}