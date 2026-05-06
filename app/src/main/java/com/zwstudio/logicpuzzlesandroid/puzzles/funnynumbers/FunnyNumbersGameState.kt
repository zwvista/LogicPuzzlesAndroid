package com.zwstudio.logicpuzzlesandroid.puzzles.funnynumbers

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FunnyNumbersGameState(game: FunnyNumbersGame) : CellsGameState<FunnyNumbersGame, FunnyNumbersGameMove, FunnyNumbersGameState>(game) {
    var objArray = game.objArray.copyOf()
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FunnyNumbersGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != 0 || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FunnyNumbersGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != 0) return GameOperationType.Invalid
        move.obj = (this[p] + 1) % (game.areas[game.pos2area[p]!!].size + 1)
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 2/Funny Numbers

        Summary
        Hahaha ... haha ... ehm ...

        Description
        1. Fill each region with numbers 1 to X where the X is the region area.
        2. Same numbers can't touch each other horizontally or vertically across regions.
        3. The numbers outside tell you the sum of the row or column.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (area in game.areas) {
            // 2. Same numbers can't touch each other horizontally or vertically across regions.
            for (p in area) {
                val n = this[p]
                pos2state[p] = if (n > 0 && FunnyNumbersGame.offset.any {
                    val p2 = p + it
                    isValid(p2) && this[p2] == n
                }) AllowedObjectState.Error else AllowedObjectState.Normal
            }
            val num2rng = area.groupBy { this[it] }
                .filter { (num, rng) -> num != 0 && rng.size > 1 }
            if (num2rng.isNotEmpty()) {
                isSolved = false
                for ((_, rng) in num2rng)
                    for (p in rng)
                        pos2state[p] = AllowedObjectState.Error
            }
        }
        // 3. The numbers outside tell you the sum of the row or column.
        for (r in 0..<rows) {
            val n2 = game.row2hint[r]
            if (n2 == 0) continue
            val n1 = (0..<cols).fold(0) { acc, c -> acc + this[r, c] }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        // 3. The numbers outside tell you the sum of the row or column.
        for (c in 0..<cols) {
            val n2 = game.col2hint[c]
            if (n2 == 0) continue
            val n1 = (0..<rows).fold(0) { acc, r -> acc + this[r, c] }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}