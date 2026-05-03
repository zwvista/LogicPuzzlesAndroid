package com.zwstudio.logicpuzzlesandroid.puzzles.themagicnumber

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheMagicNumberGameState(game: TheMagicNumberGame) : CellsGameState<TheMagicNumberGame, TheMagicNumberGameMove, TheMagicNumberGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TheMagicNumberObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TheMagicNumberObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TheMagicNumberGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != TheMagicNumberObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TheMagicNumberGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != TheMagicNumberObject.Empty) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (this[p]) {
            TheMagicNumberObject.Empty -> TheMagicNumberObject.Fv1
            TheMagicNumberObject.Fv1 -> TheMagicNumberObject.Fv2
            TheMagicNumberObject.Fv2 -> TheMagicNumberObject.Fv3
            TheMagicNumberObject.Fv3 -> TheMagicNumberObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/The Magic Number

        Summary
        No more, no less, you don't have to guess

        Description
        1. Fill the board with 3 different symbols.
        2. On side-6 boards there will be 2 of each on any row or column.
        3. On side-9 boards there will be 3 of each on any row or column.
        4. On side-12 boards there will be 4 of each on any row or column.
        5. When a tile has a shaded background, the symbols around it must
           be different.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 2. On side-6 boards there will be 2 of each on any row or column.
        // 3. On side-9 boards there will be 3 of each on any row or column.
        // 4. On side-12 boards there will be 4 of each on any row or column.
        fun checkSymbols(symbol2range: Map<TheMagicNumberObject, List<Position>>) {
            for ((_, range) in symbol2range) {
                val cnt = range.size
                if (cnt != game.symbolCountPerRowCol) {
                    isSolved = false
                    if (cnt > game.symbolCountPerRowCol)
                        for (p in range)
                            pos2state[p] = AllowedObjectState.Error
                }
            }
        }
        for (r in 0 until rows) {
            val symbol2range = mutableMapOf<TheMagicNumberObject, MutableList<Position>>()
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == TheMagicNumberObject.Empty)
                    isSolved = false
                else
                    symbol2range.getOrPut(o) { mutableListOf() }.add(p)
            }
            checkSymbols(symbol2range)
        }
        for (c in 0 until cols) {
            val symbol2range = mutableMapOf<TheMagicNumberObject, MutableList<Position>>()
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o == TheMagicNumberObject.Empty)
                    isSolved = false
                else
                    symbol2range.getOrPut(o) { mutableListOf() }.add(p)
            }
            checkSymbols(symbol2range)
        }
        // 5. When a tile has a shaded background, the symbols around it must
        //    be different.
        for (p in game.shaded) {
            val o = this[p]
            if (o == TheMagicNumberObject.Empty) continue
            for (os in TheMagicNumberGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) {continue}
                if (this[p2] == o) {
                    isSolved = false
                    pos2state[p] = AllowedObjectState.Error
                    pos2state[p2] = AllowedObjectState.Error
                }
            }
        }
    }
}