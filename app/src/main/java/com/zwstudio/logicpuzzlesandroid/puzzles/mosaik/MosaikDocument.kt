package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MosaikDocument(context: Context) : GameDocument<MosaikGameMove>(context) {
    override fun saveMove(move: MosaikGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        MosaikGameMove(Position(rec.row, rec.col), MosaikObject.values()[rec.intValue1])
}