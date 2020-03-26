package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandObject

import org.androidannotations.annotations.EBean

@EBean
open class BootyIslandDocument : GameDocument<BootyIslandGame, BootyIslandGameMove>() {
    override fun saveMove(move: BootyIslandGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BootyIslandGameMove(Position(rec.row, rec.col), BootyIslandObject.objFromString(rec.strValue1))
}
