package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class MosaikGameState(game: MosaikGame) : CellsGameState<MosaikGame, MosaikGameMove, MosaikGameState>(game) {
    var objArray = Array(rows() * cols()) { MosaikObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MosaikObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: MosaikObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            pos2state[p] = if (n == 0) HintState.Complete else HintState.Normal
        updateIsSolved()
    }

    fun setObject(move: MosaikGameMove): Boolean {
        if (this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: MosaikGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            MosaikObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) MosaikObject.Marker else MosaikObject.Filled
            MosaikObject.Filled -> if (markerOption == MarkerOptions.MarkerLast) MosaikObject.Marker else MosaikObject.Empty
            MosaikObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) MosaikObject.Filled else MosaikObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 4/Mosaik

        Summary
        Paint the mosaic, filling squares with the numbered hints

        Description
        1. In Mosaik, there is a hidden image which can be discovered using the
           numbered hints.
        2. A number tells you how many tiles must be filled in the 3*3 area formed
           by the tile itself and the ones surrounding it.
        3. Thus the numbers can go from 0, where no tiles is filled, to 9, where
           every tile is filled in a 3*3 area around the tile with the number.
        4. Every number in between denotes that some of the tiles in that 3*3
           area are filled and some are not.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols())
                if (this[r, c] == MosaikObject.Forbidden)
                    this[r, c] = MosaikObject.Empty
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            for (os in MosaikGame.offset) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                if (this[p2] == MosaikObject.Filled)
                    n1++
            }
            // 2. A number tells you how many tiles must be filled in the 3*3 area formed
            // by the tile itself and the ones surrounding it.
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
    }
}