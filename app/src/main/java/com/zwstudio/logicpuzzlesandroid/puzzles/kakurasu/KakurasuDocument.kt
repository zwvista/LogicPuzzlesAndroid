package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class KakurasuDocument : GameDocument<KakurasuGame, KakurasuGameMove>() {
    override fun saveMove(move: KakurasuGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        KakurasuGameMove(Position(rec.row, rec.col), KakurasuObject.values()[rec.intValue1])
}