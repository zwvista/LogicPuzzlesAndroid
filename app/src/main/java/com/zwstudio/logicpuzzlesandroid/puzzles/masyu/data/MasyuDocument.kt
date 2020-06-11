package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove
import org.androidannotations.annotations.EBean

@EBean
class MasyuDocument : GameDocument<MasyuGame, MasyuGameMove>() {
    override fun saveMove(move: MasyuGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        MasyuGameMove(Position(rec.row, rec.col), rec.intValue1)
}