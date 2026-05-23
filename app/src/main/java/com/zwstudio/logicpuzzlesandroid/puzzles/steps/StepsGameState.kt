package com.zwstudio.logicpuzzlesandroid.puzzles.steps

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import kotlin.math.abs

class StepsGameState(game: StepsGame) : CellsGameState<StepsGame, StepsGameMove, StepsGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: StepsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != StepsGame.PUZ_EMPTY || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: StepsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != StepsGame.PUZ_EMPTY) return GameOperationType.Invalid
        val n = game.areas[game.pos2area[p]!!].size
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (this[p]) {
            StepsGame.PUZ_EMPTY -> if (markerOption == MarkerOptions.MarkerFirst) StepsGame.PUZ_MARKER else n
            StepsGame.PUZ_MARKER -> if (markerOption == MarkerOptions.MarkerFirst) n else StepsGame.PUZ_EMPTY
            else -> if (markerOption == MarkerOptions.MarkerLast) StepsGame.PUZ_MARKER else StepsGame.PUZ_EMPTY
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Steps

        Summary
        Go up or down

        Description
        1. Each area has a single number in it, which is equal to the area size.
        2. Its position should be such that, by moving horizontally and vertically,
           the distance to another number should be the difference between the two
           numbers.
        3. Or in other words: The number of empty squares between any pair of numbers
           in the same row or column, must equal the difference between those numbers.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                pos2state[p] = HintState.Normal
                if (this[p] == StepsGame.PUZ_FORBIDDEN)
                    this[p] = StepsGame.PUZ_EMPTY
            }
        for (area in game.areas) {
            val rng = mutableListOf<Position>()
            for (p in area)
                if (this[p] != StepsGame.PUZ_EMPTY)
                    rng.add(p)
            // 1. Each area has a single number in it, which is equal to the area size.
            if (!(rng.size == 1 && this[rng[0]] == area.size)) {
                isSolved = false
                for (p in rng)
                    pos2state[p] = HintState.Error
            }
            if (allowedObjectsOnly && !rng.isEmpty())
                for (p in area)
                    if (!rng.contains(p))
                        this[p] = StepsGame.PUZ_FORBIDDEN
        }
        // 2. Its position should be such that, by moving horizontally and vertically,
        //    the distance to another number should be the difference between the two
        //    numbers.
        // 3. Or in other words: The number of empty squares between any pair of numbers
        //    in the same row or column, must equal the difference between those numbers.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val n = this[p]
                if (n <= 0) continue
                if (pos2state[p] != HintState.Error)
                    pos2state[p] = HintState.Complete
                next@ for (i in listOf(1, 2)) {
                    val os = StepsGame.offset[i]
                    var p2 = p + os
                    var steps = 0
                    while (true) {
                        if (!isValid(p2)) continue@next
                        if (this[p2] > 0) break
                        p2 += os; steps += 1
                    }
                    val s = if (abs(this[p2] - n) == steps) HintState.Complete else HintState.Error
                    if (s != HintState.Complete) isSolved = false
                    if (pos2state[p] == HintState.Complete) pos2state[p] = s
                    if (pos2state[p2] == HintState.Complete) pos2state[p2] = s
                }
            }
    }
}