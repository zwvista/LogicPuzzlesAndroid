package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ProductSentinelsGame(layout: List<String>, gi: GameInterface<ProductSentinelsGame, ProductSentinelsGameMove, ProductSentinelsGameState>, gdi: GameDocumentInterface) : CellsGame<ProductSentinelsGame, ProductSentinelsGameMove, ProductSentinelsGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2)
                if (s == "  ") continue
                val n = s.trim(' ').toInt()
                pos2hint[p] = n
            }
        }
        val state = ProductSentinelsGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: ProductSentinelsGameMove, f: (ProductSentinelsGameState, ProductSentinelsGameMove) -> Boolean): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(currentState)
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: ProductSentinelsGameMove) = changeObject(move, ProductSentinelsGameState::switchObject)
    fun setObject(move: ProductSentinelsGameMove) = changeObject(move, ProductSentinelsGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}