package com.zwstudio.logicpuzzlesandroid.puzzles.landscaper

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LandscaperGameState(game: LandscaperGame) : CellsGameState<LandscaperGame, LandscaperGameMove, LandscaperGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: LandscaperObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: LandscaperObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: LandscaperGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != LandscaperObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: LandscaperGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != LandscaperObject.Empty) return GameOperationType.Invalid
        val o = this[move.p]
        move.obj = if (o == LandscaperObject.Empty) LandscaperObject.Tree else if (o == LandscaperObject.Tree) LandscaperObject.Flower else LandscaperObject.Empty
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 4/Landscaper

        Summary
        Plant Trees and Flowers with enough variety

        Description
        1. Your goal as a landscaper is to plant some Trees and Flowers on the
           field, in every available tile.
        2. In doing this, you must assure the scenery is varied enough:
        3. No more than two consecutive Trees or Flowers should appear horizontally
           or vertically.
        4. Every row and column should have an equal number of Trees and Flowers.
        5. Each row disposition must be unique, i.e. the same arrangement of Trees
           and Flowers can't appear on two rows.
        6. Each column disposition must be unique as well.

        Odd-size levels
        7. Please note that in odd-size levels, the number of Trees and Flowers
           obviously won't be equal on a row or column. However each row and
           column will have the same number of Flowers and Trees.
        8. Also, the number of Trees will always be greater than that of Flowers
           (i.e. 3 Flowers and 4 Trees, 4 Flowers and 5 Trees, etc).
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        var oLast: LandscaperObject
        val tokens = mutableListOf<Position>()
        fun checkTokens() {
            if (tokens.size > 2) {
                isSolved = false
                for (p in tokens)
                    pos2state[p] = AllowedObjectState.Error
            }
            tokens.clear()
        }
        val rowDispos = mutableSetOf<String>()
        val rowCounts = mutableSetOf<List<Int>>()
        for (r in 0..<rows) {
            oLast = LandscaperObject.Empty
            var dispo = ""
            var (nTree, nFlower) = 0 to 0
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                dispo += o.ordinal.toString()
                if (o != oLast) {
                    checkTokens()
                    oLast = o
                }
                when (o) {
                    LandscaperObject.Empty -> isSolved = false
                    LandscaperObject.Tree -> { nTree++; tokens.add(p) }
                    LandscaperObject.Flower -> { nFlower++; tokens.add(p) }
                }
            }
            checkTokens()
            rowDispos.add(dispo)
            rowCounts.add(listOf(nTree, nFlower))
        }
        val colDispos = mutableSetOf<String>()
        val colCounts = mutableSetOf<List<Int>>()
        for (c in 0..<cols) {
            oLast = LandscaperObject.Empty
            var dispo = ""
            var (nTree, nFlower) = 0 to 0
            for (r in 0..<rows) {
                val p = Position(r, c)
                val o = this[p]
                dispo += o.ordinal.toString()
                if (o != oLast) {
                    checkTokens()
                    oLast = o
                }
                when (o) {
                    LandscaperObject.Empty -> isSolved = false
                    LandscaperObject.Tree -> { nTree++; tokens.add(p) }
                    LandscaperObject.Flower -> { nFlower++; tokens.add(p) }
                }
            }
            checkTokens()
            colDispos.add(dispo)
            colCounts.add(listOf(nTree, nFlower))
        }
        if (!isSolved) return
        fun checkCount(counts: List<Int>): Boolean {
            val (nTree, nFlower) = counts[0] to counts[1]
            return rows % 2 == 0 && nTree == nFlower || nTree == nFlower + 1
        }
        if (!(rowDispos.size == rows && colDispos.size == cols && rowCounts.size == 1 && colCounts.size == 1 &&
            checkCount(rowCounts.first()) && checkCount(colCounts.first())))
            isSolved = false
    }
}
