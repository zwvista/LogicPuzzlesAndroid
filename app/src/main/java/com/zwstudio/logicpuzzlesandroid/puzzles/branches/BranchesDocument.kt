package com.zwstudio.logicpuzzlesandroid.puzzles.branches

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BranchesDocument(context: Context) : GameDocument<BranchesGameMove>(context) {
    override fun saveMove(move: BranchesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BranchesGameMove(Position(rec.row, rec.col), BranchesObject.objFromString(rec.strValue1!!))
}