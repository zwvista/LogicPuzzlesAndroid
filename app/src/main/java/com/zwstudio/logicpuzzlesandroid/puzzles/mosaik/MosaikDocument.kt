package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class MosaikDocument : GameDocument<MosaikGameMove>() {
    override fun saveMove(move: MosaikGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        MosaikGameMove(Position(rec.row, rec.col), MosaikObject.values()[rec.intValue1])
}