package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DisconnectFourGameState(game: DisconnectFourGame) : CellsGameState<DisconnectFourGame, DisconnectFourGameMove, DisconnectFourGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: DisconnectFourObject) {objArray[row * cols() + col] = dotObj}
    operator fun set(p: Position, obj: DisconnectFourObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: DisconnectFourGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != DisconnectFourObject.Empty || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: DisconnectFourGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != DisconnectFourObject.Empty) return false
        val o = this[p]
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
        for (r in 0 until rows())
            for (c in 0 until cols()) {
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
        for (r in 0 until rows()) {
            oLast = DisconnectFourObject.Empty
            for (c in 0 until cols()) {
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
        for (c in 0 until cols()) {
            oLast = DisconnectFourObject.Empty
            for (r in 0 until rows()) {
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