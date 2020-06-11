package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGame
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameMove
import org.androidannotations.annotations.EBean

@EBean
class SkyscrapersDocument : GameDocument<SkyscrapersGame, SkyscrapersGameMove>() {
    override fun saveMove(move: SkyscrapersGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        SkyscrapersGameMove(Position(rec.row, rec.col), rec.intValue1)
}