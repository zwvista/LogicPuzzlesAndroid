package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove
import org.androidannotations.annotations.EBean

@EBean
class BridgesDocument : GameDocument<BridgesGame, BridgesGameMove>() {
    override fun saveMove(move: BridgesGameMove, rec: MoveProgress) {
        rec.row = move.pFrom.row
        rec.col = move.pFrom.col
        rec.row2 = move.pTo.row
        rec.col2 = move.pTo.col
    }

    override fun loadMove(rec: MoveProgress) =
        BridgesGameMove(Position(rec.row, rec.col), Position(rec.row2, rec.col2))
}
