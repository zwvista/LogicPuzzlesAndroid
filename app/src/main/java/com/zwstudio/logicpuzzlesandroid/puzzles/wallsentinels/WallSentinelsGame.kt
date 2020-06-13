package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WallSentinelsGame(layout: List<String>, gi: GameInterface<WallSentinelsGame, WallSentinelsGameMove, WallSentinelsGameState>, gdi: GameDocumentInterface) : CellsGame<WallSentinelsGame, WallSentinelsGameMove, WallSentinelsGameState>(gi, gdi) {

    companion object {
        val offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        val offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1)
        )
    }

    var objArray: MutableList<WallSentinelsObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: WallSentinelsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: WallSentinelsObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length / 2)
        // https://stackoverflow.com/questions/43172947/kotlin-creating-a-mutable-list-with-repeating-elements
        objArray = MutableList(rows * cols) { WallSentinelsEmptyObject() }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2)
                if (s != "  ") {
                    val n = s[1] - '0'
                    val o =
                        if (s[0] == '.') WallSentinelsHintLandObject(n)
                        else WallSentinelsHintWallObject(n)
                    this[p] = o
                }
            }
        }
        val state = WallSentinelsGameState(this)
        levelInitilized(state)
    }

    fun switchObject(move: WallSentinelsGameMove) = changeObject(move, WallSentinelsGameState::switchObject)
    fun setObject(move: WallSentinelsGameMove) = changeObject(move, WallSentinelsGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
