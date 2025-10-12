package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HiddenPathGameState(game: HiddenPathGame) : CellsGameState<HiddenPathGame, HiddenPathGameMove, HiddenPathGameState>(game) {
    var objArray: Array<HiddenPathObject>
    var pos2state = mutableMapOf<Position, HintState>()
    var currentPos = Position()
    var nextNum = 0

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: HiddenPathObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: HiddenPathObject) {this[p.row, p.col] = obj}

    init {
        objArray = Array (game.maxNum) { HiddenPathObject() }
        for (i in 0 until game.maxNum)
            objArray[i].obj = game.objArray[i]
        updateIsSolved()
    }

    override fun setObject(move: HiddenPathGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p].obj != 0) return false
        this[p].obj = nextNum
        updateIsSolved()
        return true
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 3/Hidden Path

        Summary
        Jump once on every tile, following the arrows

        Description
        Starting at the tile number 1, reach the last tile by jumping from tile to tile.
        1. When jumping from a tile, you have to follow the direction of the arrow and
           land on a tile in that direction
        2. Although you have to follow the direction of the arrow, you can land on any
           tile in that direction, not just the one next to the current tile.
        3. The goal is to jump on every tile, only once and reach the last tile.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val num2pos = mutableMapOf<Int, Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = this[p].obj
                if (n == -1) // forbidden
                    this[p].obj = 0
                if (n != 0)
                    num2pos[n] = p
            }
        nextNum = 0
        for ((n, p) in num2pos) {
            if (n == game.maxNum) continue
            if (!num2pos.contains(n + 1)) {
                isSolved = false
                if (nextNum == 0) {
                    currentPos = p
                    nextNum = n + 1
                }
                this[p].state = HintState.Normal
            } else {
                val p2 = num2pos[n + 1]!!
                val b = game.pos2range[p]!!.contains(p2)
                this[p].state = if (b) HintState.Complete else HintState.Error
                if (!b)
                    isSolved = false
                if (b && n + 1 == game.maxNum)
                    this[p2].state = HintState.Complete
            }
        }
        if (!allowedObjectsOnly) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p].obj != 0) continue
                val b = game.pos2range[currentPos]!!.contains(p)
                if (!b) // forbidden
                    this[p].obj = -1
            }
    }
}
