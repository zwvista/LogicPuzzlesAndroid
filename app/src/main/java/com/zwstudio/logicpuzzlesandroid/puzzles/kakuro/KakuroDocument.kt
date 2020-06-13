package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class KakuroDocument : GameDocument<KakuroGame, KakuroGameMove>() {
    override fun saveMove(move: KakuroGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        KakuroGameMove(Position(rec.row, rec.col), rec.intValue1)
}