package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class KakurasuDocument(context: Context) : GameDocument<KakurasuGameMove>(context) {
    override fun saveMove(move: KakurasuGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        KakurasuGameMove(Position(rec.row, rec.col), KakurasuObject.values()[rec.intValue1])
}