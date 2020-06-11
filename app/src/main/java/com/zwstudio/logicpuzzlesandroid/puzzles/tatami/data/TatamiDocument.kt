package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameMove
import org.androidannotations.annotations.EBean

@EBean
class TatamiDocument : GameDocument<TatamiGame, TatamiGameMove>() {
    override fun saveMove(move: TatamiGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        TatamiGameMove(Position(rec.row, rec.col), rec.strValue1[0])
}