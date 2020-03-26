package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingObject
import org.androidannotations.annotations.EBean

@EBean
open class AbstractPaintingDocument : GameDocument<AbstractPaintingGame, AbstractPaintingGameMove>() {
    override fun saveMove(move: AbstractPaintingGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        AbstractPaintingGameMove(Position(rec.row, rec.col), AbstractPaintingObject.values()[rec.intValue1])
}
