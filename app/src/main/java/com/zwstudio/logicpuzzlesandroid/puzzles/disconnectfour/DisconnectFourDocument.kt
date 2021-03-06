package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class DisconnectFourDocument : GameDocument<DisconnectFourGameMove>() {
    override fun saveMove(move: DisconnectFourGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        DisconnectFourGameMove(Position(rec.row, rec.col), DisconnectFourObject.values()[rec.intValue1])
}