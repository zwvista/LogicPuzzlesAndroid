package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2Game
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2GameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2Object

import org.androidannotations.annotations.EBean

@EBean
class WallSentinels2Document : GameDocument<WallSentinels2Game, WallSentinels2GameMove>() {
    override fun saveMove(move: WallSentinels2GameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        WallSentinels2GameMove(Position(rec.row, rec.col), WallSentinels2Object.objFromString(rec.strValue1!!))
}
