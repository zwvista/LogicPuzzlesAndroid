package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsObject
import org.androidannotations.annotations.EBean

@EBean
class MiniLitsDocument : GameDocument<MiniLitsGame, MiniLitsGameMove>() {
    override fun saveMove(move: MiniLitsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        MiniLitsGameMove(Position(rec.row, rec.col), MiniLitsObject.objFromString(rec.strValue1!!))
}