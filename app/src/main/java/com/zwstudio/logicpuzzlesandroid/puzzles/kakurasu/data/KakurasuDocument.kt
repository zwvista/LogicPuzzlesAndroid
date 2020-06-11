package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuObject
import org.androidannotations.annotations.EBean

@EBean
class KakurasuDocument : GameDocument<KakurasuGame, KakurasuGameMove>() {
    protected override fun saveMove(move: KakurasuGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress): KakurasuGameMove {
        return object : KakurasuGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = KakurasuObject.values()[rec.intValue1]
            }
        }
    }
}