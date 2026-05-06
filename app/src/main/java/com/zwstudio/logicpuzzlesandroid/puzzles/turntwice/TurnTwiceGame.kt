package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import kotlin.math.sign

class TurnTwiceGame(layout: List<String>, gi: GameInterface<TurnTwiceGame, TurnTwiceGameMove, TurnTwiceGameState>, gdi: GameDocumentInterface) : CellsGame<TurnTwiceGame, TurnTwiceGameMove, TurnTwiceGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val objArray: Array<TurnTwiceObject>
    val signposts = mutableListOf<Position>()
    // two signposts and the shortest path between them
    val paths = mutableListOf<Triple<Position, Position, List<Position>>>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TurnTwiceObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: TurnTwiceObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { TurnTwiceObject.Empty }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (str[c] == 'S') {
                    this[p] = TurnTwiceObject.SignPost
                    signposts.add(p)
                }
            }
        }
        val os0 = Position.Zero
        val sz = signposts.size
        for (i in 0 until sz - 1) {
            val p1 = signposts[i]
            for (j in i + 1 until sz) {
                val p2 = signposts[j]
                val sz2 = if (p1.row == p2.row || p1.col == p2.col) 1 else 2
                for (k in 0 until sz2) {
                    val path = mutableListOf<Position>()
                    if (run {
                        var p = p1
                        while (true) {
                            val os1 = Position((p2.row - p.row).sign, 0)
                            val os2 = Position(0, (p2.col - p.col).sign)
                            val os = if (k == 0 && os1 != os0 || k == 1 && os2 == os0) os1 else os2
                            p += os
                            if (p == p2) break
                            if (this[p] == TurnTwiceObject.Empty)
                                path.add(p)
                            else
                                return@run false
                        }
                        return@run true
                    }) {
                        paths.add(Triple(p1, p2, path))
                    }
                }
            }
        }
        val state = TurnTwiceGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
