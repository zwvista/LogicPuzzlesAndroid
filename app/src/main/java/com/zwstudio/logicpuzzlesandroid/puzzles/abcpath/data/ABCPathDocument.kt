package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameMove
import org.androidannotations.annotations.EBean

@EBean
class ABCPathDocument : GameDocument<ABCPathGame, ABCPathGameMove>() {
    override fun saveMove(move: ABCPathGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        ABCPathGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}
