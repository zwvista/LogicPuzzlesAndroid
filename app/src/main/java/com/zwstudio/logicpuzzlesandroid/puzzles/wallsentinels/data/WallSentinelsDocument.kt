package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsObject

import org.androidannotations.annotations.EBean

@EBean
class WallSentinelsDocument : GameDocument<WallSentinelsGame, WallSentinelsGameMove>() {
    override fun saveMove(move: WallSentinelsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        WallSentinelsGameMove(Position(rec.row, rec.col), WallSentinelsObject.objFromString(rec.strValue1!!))
}
