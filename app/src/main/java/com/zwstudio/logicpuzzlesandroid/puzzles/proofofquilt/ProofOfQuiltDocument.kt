package com.zwstudio.logicpuzzlesandroid.puzzles.proofofquilt

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ProofOfQuiltDocument(context: Context) : GameDocument<ProofOfQuiltGameMove>(context) {
    override fun saveMove(move: ProofOfQuiltGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        ProofOfQuiltGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}
