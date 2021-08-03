package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class KakuroDocument(context: Context) : GameDocument<KakuroGameMove>(context) {
    override fun saveMove(move: KakuroGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        KakuroGameMove(Position(rec.row, rec.col), rec.intValue1)
}