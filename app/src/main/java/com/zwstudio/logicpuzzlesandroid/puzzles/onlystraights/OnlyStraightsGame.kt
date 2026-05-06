package com.zwstudio.logicpuzzlesandroid.puzzles.onlystraights

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class OnlyStraightsGame(layout: List<String>, gi: GameInterface<OnlyStraightsGame, OnlyStraightsGameMove, OnlyStraightsGameState>, gdi: GameDocumentInterface) : CellsGame<OnlyStraightsGame, OnlyStraightsGameMove, OnlyStraightsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_TOWN = 'O'
    }

    val objArray: MutableList<OnlyStraightsTown>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: OnlyStraightsTown) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: OnlyStraightsTown) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = MutableList(rows * cols) { OnlyStraightsTown.Empty }
        for (r in 0 until rows) {
            var str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                this[p] = when (str[c]) {
                    '1' ->  OnlyStraightsTown.Center
                    '2' ->  OnlyStraightsTown.Right
                    '4' ->  OnlyStraightsTown.Bottom
                    '3' ->  OnlyStraightsTown.CenterRight
                    '5' ->  OnlyStraightsTown.CenterBottom
                    '6' ->  OnlyStraightsTown.RightBottom
                    '7' ->  OnlyStraightsTown.CenterRightBottom
                    else ->  OnlyStraightsTown.Empty
                }
            }
        }
        val state = OnlyStraightsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
