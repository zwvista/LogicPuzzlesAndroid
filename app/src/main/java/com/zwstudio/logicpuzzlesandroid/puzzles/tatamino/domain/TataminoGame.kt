package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import java.util.*

class TataminoGame(layout: List<String>, gi: GameInterface<TataminoGame, TataminoGameMove, TataminoGameState>, gdi: GameDocumentInterface) : CellsGame<TataminoGame, TataminoGameMove, TataminoGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 2, 1, 2)
    }

    var areas: List<List<Position>> = ArrayList()
    var pos2area: Map<Position, Int> = HashMap()
    var dots: GridDots
    var objArray: CharArray

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        dots = GridDots(rows() + 1, cols() + 1)
        objArray = CharArray(rows() * cols())
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val ch = str[c]
                this[r, c] = ch
            }
        }
        for (r in 0 until rows()) {
            dots[r, 0, 2] = GridLineObject.Line
            dots[r, cols(), 2] = GridLineObject.Line
        }
        for (c in 0 until cols()) {
            dots[0, c, 1] = GridLineObject.Line
            dots[rows(), c, 1] = GridLineObject.Line
        }
        val state = TataminoGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: TataminoGameMove, f: (TataminoGameState, TataminoGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state: TataminoGameState = cloner.deepClone(state())
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

    fun switchObject(move: TataminoGameMove) = changeObject(move, TataminoGameState::switchObject)
    fun setObject(move: TataminoGameMove) = changeObject(move, TataminoGameState::setObject)

    fun getObject(p: Position) = state().get(p)
    fun getObject(row: Int, col: Int) = state().get(row, col)
    fun getPosState(p: Position) = state().pos2state.get(p)
    fun getDots() = state().dots
}