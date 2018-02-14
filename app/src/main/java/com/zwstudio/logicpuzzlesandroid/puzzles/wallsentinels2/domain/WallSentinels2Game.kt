package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WallSentinels2Game(layout: List<String>, gi: GameInterface<WallSentinels2Game, WallSentinels2GameMove, WallSentinels2GameState>, gdi: GameDocumentInterface) : CellsGame<WallSentinels2Game, WallSentinels2GameMove, WallSentinels2GameState>(gi, gdi) {

    var objArray: MutableList<WallSentinels2Object>

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: WallSentinels2Object) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: WallSentinels2Object) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length / 2)
        // https://stackoverflow.com/questions/43172947/kotlin-creating-a-mutable-list-with-repeating-elements
        objArray = MutableList(rows() * cols()) { WallSentinels2EmptyObject() }
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2)
                if (s != "  ") {
                    val n = s[1] - '0'
                    val o =
                        if (s[0] == '.') WallSentinels2HintLandObject(n)
                        else WallSentinels2HintWallObject(n)
                    this[p] = o
                }
            }
        }
        val state = WallSentinels2GameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: WallSentinels2GameMove, f: ((WallSentinels2GameState, WallSentinels2GameMove) -> Boolean)): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
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

    fun switchObject(move: WallSentinels2GameMove) = changeObject(move, WallSentinels2GameState::switchObject)
    fun setObject(move: WallSentinels2GameMove) = changeObject(move, WallSentinels2GameState::setObject)

    fun getObject(p: Position): WallSentinels2Object = state()[p]
    fun getObject(row: Int, col: Int): WallSentinels2Object = state()[row, col]

    companion object {
        var offset = listOf(Position(-1, 0), Position(0, 1), Position(1, 0), Position(0, -1))
        var offset2 = listOf(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1))
    }
}
