package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PipemaniaDocument(context: Context) : GameDocument<PipemaniaGameMove>(context) {
    override fun saveMove(move: PipemaniaGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        PipemaniaGameMove(Position(rec.row, rec.col), PipemaniaObject.objFromString(rec.strValue1!!))
}