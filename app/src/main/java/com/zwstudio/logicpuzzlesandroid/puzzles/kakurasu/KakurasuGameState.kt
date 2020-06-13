package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class KakurasuGameState(game: KakurasuGame) : CellsGameState<KakurasuGame, KakurasuGameMove, KakurasuGameState>(game) {
    var objArray = Array(rows * cols) { KakurasuObject.Empty }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: KakurasuObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: KakurasuObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: KakurasuGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: KakurasuGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        if (!isValid(move.p)) return false
        val o = this[move.p]
        move.obj = when (o) {
            KakurasuObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) KakurasuObject.Marker else KakurasuObject.Cloud
            KakurasuObject.Cloud -> if (markerOption == MarkerOptions.MarkerLast) KakurasuObject.Marker else KakurasuObject.Empty
            KakurasuObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) KakurasuObject.Cloud else KakurasuObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 8/Kakurasu

        Summary
        Cloud Kakuro on a Skyscraper

        Description
        1. On the bottom and right border, you see the value of (respectively)
           the columns and rows.
        2. On the other borders, on the top and the left, you see the hints about
           which tile have to be filled on the board. These numbers represent the
           sum of the values mentioned above.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] == KakurasuObject.Forbidden)
                    this[r, c] = KakurasuObject.Empty
        for (r in 1 until rows - 1) {
            // 1. On the bottom and right border, you see the value of (respectively)
            // the rows.
            var n1 = 0
            val n2 = game.row2hint[r * 2]
            for (c in 1 until cols - 1)
                if (this[r, c] == KakurasuObject.Cloud)
                    // 2. On the other borders, on the top and the left, you see the hints about
                    // which tile have to be filled on the board.
                    n1 += game.col2hint[c * 2 + 1]
            // 2. These numbers represent the sum of the values mentioned above.
            val s = if (n1 == 0) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            row2state[r * 2] = s
            if (s != HintState.Complete) isSolved = false
            if (n1 >= n2 && allowedObjectsOnly)
                for (c in 1 until cols - 1)
                    when (this[r, c]) {
                        KakurasuObject.Empty, KakurasuObject.Marker -> this[r, c] = KakurasuObject.Forbidden
                        else -> {}
                    }
        }
        for (c in 1 until cols - 1) {
            // 1. On the bottom and right border, you see the value of (respectively)
            // the columns.
            var n1 = 0
            val n2 = game.col2hint[c * 2]
            for (r in 1 until rows - 1)
                if (this[r, c] == KakurasuObject.Cloud)
                    // 2. On the other borders, on the top and the left, you see the hints about
                    // which tile have to be filled on the board.
                    n1 += game.row2hint[r * 2 + 1]
            // 2. These numbers represent the sum of the values mentioned above.
            val s = if (n1 == 0) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            col2state[c * 2] = s
            if (s != HintState.Complete) isSolved = false
            if (n1 >= n2 && allowedObjectsOnly)
                for (r in 1 until rows - 1)
                    when (this[r, c]) {
                        KakurasuObject.Empty, KakurasuObject.Marker -> this[r, c] = KakurasuObject.Forbidden
                        else -> {}
                    }
        }
    }
}