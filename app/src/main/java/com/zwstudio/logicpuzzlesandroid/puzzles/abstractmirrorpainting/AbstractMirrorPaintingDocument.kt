package com.zwstudio.logicpuzzlesandroid.puzzles.abstractmirrorpainting

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class AbstractMirrorPaintingDocument(context: Context) : GameDocument<AbstractMirrorPaintingGameMove>(context) {
    override fun saveMove(move: AbstractMirrorPaintingGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        AbstractMirrorPaintingGameMove(Position(rec.row, rec.col), AbstractMirrorPaintingObject.entries[rec.intValue1])
}