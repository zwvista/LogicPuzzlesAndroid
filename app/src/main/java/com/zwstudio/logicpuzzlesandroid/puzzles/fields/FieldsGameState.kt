package com.zwstudio.logicpuzzlesandroid.puzzles.fields

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FieldsGameState(game: FieldsGame) : CellsGameState<FieldsGame, FieldsGameMove, FieldsGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FieldsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FieldsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FieldsGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != FieldsObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FieldsGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != FieldsObject.Empty) return GameOperationType.Invalid
        val o = this[move.p]
        move.obj = if (o == FieldsObject.Empty) FieldsObject.Yellow else if (o == FieldsObject.Yellow) FieldsObject.Red else FieldsObject.Empty
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Disconnect Four

        Summary
        Win by not winning!

        Description
        1. The opposite of the famous game 'Connect Four', where you must line
           up four tokens of the same colour.
        2. In this puzzle you have to ensure that there are no more than three
           tokens of the same colour lined up horizontally, vertically or
           diagonally.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2state[p] = AllowedObjectState.Normal
            }
        var oLast: FieldsObject
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
            oLast = FieldsObject.Empty
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o != oLast) {
                    checkTrees()
                    oLast = o
                }
                if (o == FieldsObject.Empty)
                    isSolved = false
                else
                    trees.add(p)
            }
            checkTrees()
        }
        for (c in 0 until cols) {
            oLast = FieldsObject.Empty
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o != oLast) {
                    checkTrees()
                    oLast = o
                }
                if (o == FieldsObject.Empty)
                    isSolved = false
                else
                    trees.add(p)
            }
            checkTrees()
        }
    }
}
