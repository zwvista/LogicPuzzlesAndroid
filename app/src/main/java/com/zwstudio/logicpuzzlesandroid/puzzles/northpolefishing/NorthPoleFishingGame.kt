package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*

class NorthPoleFishingGame(layout: List<String>, gi: GameInterface<NorthPoleFishingGame, NorthPoleFishingGameMove, NorthPoleFishingGameState>, gdi: GameDocumentInterface) : CellsGame<NorthPoleFishingGame, NorthPoleFishingGameMove, NorthPoleFishingGameState>(gi, gdi) {
    companion object {
        val offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = arrayOf(1, 0, 3, 2)
    }

    var objArray: MutableList<NorthPoleFishingObject>
    var blocks = mutableListOf<Position>()
    var holes = mutableListOf<Position>()
    var dots: GridDots

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: NorthPoleFishingObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: NorthPoleFishingObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = MutableList(rows * cols) { NorthPoleFishingObject.Empty }
        dots = GridDots(rows, cols)
        for (r in 0 until rows - 1) {
            val str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                when (str[c]) {
                    'B' ->  {
                        this[p] = NorthPoleFishingObject.Block
                        blocks.add(p)
                        dots[r, c, 2] = GridLineObject.Line
                        dots[r + 1, c, 0] = GridLineObject.Line
                        dots[r, c + 1, 2] = GridLineObject.Line
                        dots[r + 1, c + 1, 0] = GridLineObject.Line
                        dots[r, c, 1] = GridLineObject.Line
                        dots[r, c + 1, 3] = GridLineObject.Line
                        dots[r + 1, c, 1] = GridLineObject.Line
                        dots[r + 1, c + 1, 3] = GridLineObject.Line
                    }
                    'H' -> {
                        this[p] = NorthPoleFishingObject.Hole
                        holes.add(p)
                    }
                }
            }
        }
        for (r in 0 until rows - 1) {
            dots[r, 0, 2] = GridLineObject.Line
            dots[r + 1, 0, 0] = GridLineObject.Line
            dots[r, cols - 1, 2] = GridLineObject.Line
            dots[r + 1, cols - 1, 0] = GridLineObject.Line
        }
        for (c in 0 until cols - 1) {
            dots[0, c, 1] = GridLineObject.Line
            dots[0, c + 1, 3] = GridLineObject.Line
            dots[rows - 1, c, 1] = GridLineObject.Line
            dots[rows - 1, c + 1, 3] = GridLineObject.Line
        }
        val state = NorthPoleFishingGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    fun switchObject(move: NorthPoleFishingGameMove) = changeObject(move, NorthPoleFishingGameState::switchObject)
    fun setObject(move: NorthPoleFishingGameMove) = changeObject(move, NorthPoleFishingGameState::setObject)

    fun getObject(p: Position, dir: Int): GridLineObject = currentState[p, dir]
    fun getObject(row: Int, col: Int, dir: Int): GridLineObject = currentState[row, col, dir]
    fun getPosState(p: Position) = currentState.pos2state[p]
}
