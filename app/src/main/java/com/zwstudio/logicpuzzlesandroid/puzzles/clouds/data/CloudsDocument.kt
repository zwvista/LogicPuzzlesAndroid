package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsObject
import org.androidannotations.annotations.EBean

@EBean
open class CloudsDocument : GameDocument<CloudsGame?, CloudsGameMove?>() {
    protected override fun saveMove(move: CloudsGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.obj!!.ordinal
    }

    override fun loadMove(rec: MoveProgress): CloudsGameMove {
        return object : CloudsGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = CloudsObject.values()[rec.intValue1]
            }
        }
    }
}