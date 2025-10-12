package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import org.bson.BSON.toInt
import kotlin.math.sign

class HiddenPathGameState(game: HiddenPathGame) : CellsGameState<HiddenPathGame, HiddenPathGameMove, HiddenPathGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()
    var nextNum = 0

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: HiddenPathGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] != 0) return false
        this[p] = nextNum
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
        isSolved = true
        val num2pos = mutableMapOf<Int, Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = this[p]
                if (n != 0)
                    num2pos[n] = p
            }
        nextNum = 0
        for ((n, p) in num2pos) {
            if (n == game.maxNum) continue
            if (!num2pos.contains(n + 1)) {
                isSolved = false
                if (nextNum == 0)
                    nextNum = n + 1
                pos2state[p] = HintState.Normal
            } else {
                val d = num2pos[n + 1]!! - p
                val os = HiddenPathGame.offset[game.pos2hint[p]!!]
                val b = d.row.sign == os.row && d.col.sign == os.col
                pos2state[p] = if (b) HintState.Complete else HintState.Error
                if (!b)
                    isSolved = false
            }
        }
    }
}
