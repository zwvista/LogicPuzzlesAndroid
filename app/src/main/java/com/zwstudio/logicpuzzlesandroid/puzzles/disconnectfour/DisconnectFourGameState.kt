package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DisconnectFourGameState(game: DisconnectFourGame) : CellsGameState<DisconnectFourGame, DisconnectFourGameMove, DisconnectFourGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: DisconnectFourObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: DisconnectFourObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: DisconnectFourGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != DisconnectFourObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: DisconnectFourGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != DisconnectFourObject.Empty) return GameOperationType.Invalid
        val o = this[move.p]
        move.obj = if (o == DisconnectFourObject.Empty) DisconnectFourObject.Yellow else if (o == DisconnectFourObject.Yellow) DisconnectFourObject.Red else DisconnectFourObject.Empty
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
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                pos2state[p] = AllowedObjectState.Normal
            }
        var oLast: DisconnectFourObject
        val trees = mutableListOf<Position>()
        fun checkTrees() {
            if (trees.size > 3) {
                isSolved = false
                for (p in trees)
                    pos2state[p] = AllowedObjectState.Error
            }
            trees.clear()
        }
        for (r in 0..<rows) {
            oLast = DisconnectFourObject.Empty
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o != oLast) {
                    checkTrees()
                    oLast = o
                }
                if (o == DisconnectFourObject.Empty)
                    isSolved = false
                else
                    trees.add(p)
            }
            checkTrees()
        }
        for (c in 0..<cols) {
            oLast = DisconnectFourObject.Empty
            for (r in 0..<rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o != oLast) {
                    checkTrees()
                    oLast = o
                }
                if (o == DisconnectFourObject.Empty)
                    isSolved = false
                else
                    trees.add(p)
            }
            checkTrees()
        }
    }
}
