package com.zwstudio.logicpuzzlesandroid.puzzles.pointing

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PointingGameState(game: PointingGame) : CellsGameState<PointingGame, PointingGameMove, PointingGameState>(game) {
    var markedArrows = mutableSetOf<Position>()
    var nonPointingArrows = setOf<Position>()

    init {
        updateIsSolved()
    }

    override fun setObject(move: PointingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        if (!markedArrows.add(p))
            markedArrows.remove(p)
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PointingGameMove): GameOperationType {
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 2/Pointing

        Summary
        Are you pointing to me?

        Description
        1. Mark some arrows so that each arrow points to exactly one marked arrow.
    */
    private fun updateIsSolved() {
        isSolved = true
        nonPointingArrows = game.arrow2rng
            .filter { (_, rng) -> rng.all { !markedArrows.contains(it) } }.keys.toSet()
        if (nonPointingArrows.isNotEmpty()) isSolved = false
    }
}
