package com.zwstudio.logicpuzzlesandroid.puzzles.rome

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RomeGameState(game: RomeGame) : CellsGameState<RomeGame, RomeGameMove, RomeGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: RomeObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: RomeObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: RomeGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != RomeObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: RomeGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != RomeObject.Empty) return GameOperationType.Invalid
        val o = this[move.p]
        move.obj = when (o) {
            RomeObject.Empty -> RomeObject.Up
            RomeObject.Up -> RomeObject.Right
            RomeObject.Right -> RomeObject.Down
            RomeObject.Down -> RomeObject.Left
            RomeObject.Left -> RomeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Rome

        Summary
        All roads lead to ...

        Description
        1. All the roads lead to Rome.
        2. Hence you should fill the remaining spaces with arrows and in the
           end, starting at any tile and following the arrows, you should get
           at the Rome icon.
        3. Arrows in an area should all be different, i.e. there can't be two
           similar arrows in an area.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2state[p] = AllowedObjectState.Normal
            }
        var oLast: RomeObject
        val trees = mutableListOf<Position>()
        fun checkTrees() {
            if (trees.size > 3) {
                isSolved = false
                for (p in trees)
                    pos2state[p] = AllowedObjectState.Error
            }
            trees.clear()
        }
        for (r in 0 until rows) {
            oLast = RomeObject.Empty
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o != oLast) {
                    checkTrees()
                    oLast = o
                }
                if (o == RomeObject.Empty)
                    isSolved = false
                else
                    trees.add(p)
            }
            checkTrees()
        }
        for (c in 0 until cols) {
            oLast = RomeObject.Empty
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o != oLast) {
                    checkTrees()
                    oLast = o
                }
                if (o == RomeObject.Empty)
                    isSolved = false
                else
                    trees.add(p)
            }
            checkTrees()
        }
    }
}
