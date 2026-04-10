package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrosstownTrafficGameState(game: CrosstownTrafficGame) : CellsGameState<CrosstownTrafficGame, CrosstownTrafficGameMove, CrosstownTrafficGameState>(game) {
    val objArray = Array(rows * cols) { CrosstownTrafficObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CrosstownTrafficObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: CrosstownTrafficObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = CrosstownTrafficObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: CrosstownTrafficGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p].toString() == move.obj.toString()) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: CrosstownTrafficGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            CrosstownTrafficObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) CrosstownTrafficObject.Marker else CrosstownTrafficObject.UpRight
            CrosstownTrafficObject.UpRight -> CrosstownTrafficObject.DownRight
            CrosstownTrafficObject.DownRight -> CrosstownTrafficObject.LeftDown
            CrosstownTrafficObject.LeftDown -> CrosstownTrafficObject.LeftUp
            CrosstownTrafficObject.LeftUp -> CrosstownTrafficObject.Horizontal
            CrosstownTrafficObject.Horizontal -> CrosstownTrafficObject.Vertical
            CrosstownTrafficObject.Vertical -> CrosstownTrafficObject.Cross
            CrosstownTrafficObject.Cross -> if (markerOption == MarkerOptions.MarkerLast) CrosstownTrafficObject.Marker else CrosstownTrafficObject.Empty
            CrosstownTrafficObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) CrosstownTrafficObject.UpRight else CrosstownTrafficObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Crosstown Traffic

        Summary
        looks like pipes made of asphalt

        Description
        1. Draw a circuit (looping road)
        2. The road may cross itself, but otherwise does not touch or retrace itself.
        3. The numbers along the edge indicate the stretch of the nearest section
           of road from that point, in corresponding row or column.
        4. For example if the first stretch of road is curve, straight and curve
           the number of that hint is 3.
        5. Another example: if the first stretch of road is curve and curve,
           the number of that hint is 2.
        6. Not all tiles should be used. In some levels some part of the board
           can remain unused.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, MutableList<Int>>()
        for (r in 1 until rows - 1)
            for (c in 1 until cols - 1) {
                val p = Position(r, c)
                pos2dirs[p] = when (this[p]) {
                    CrosstownTrafficObject.UpRight -> mutableListOf(0, 1)
                    CrosstownTrafficObject.DownRight -> mutableListOf(1, 2)
                    CrosstownTrafficObject.LeftDown -> mutableListOf(2, 3)
                    CrosstownTrafficObject.LeftUp -> mutableListOf(0, 3)
                    CrosstownTrafficObject.Horizontal -> mutableListOf(1, 3)
                    CrosstownTrafficObject.Vertical -> mutableListOf(0, 2)
                    CrosstownTrafficObject.Cross -> mutableListOf(0, 1, 2, 3)
                    else -> mutableListOf()
                }
            }
        // 1. Draw a circuit (looping road)
        for (r in 1 until rows - 1)
            for (c in 1 until cols - 1) {
                val p = Position(r, c)
                val dirs = pos2dirs[p]!!
                if (!dirs.all {
                    val p2 = p + CrosstownTrafficGame.offset[it]
                    val dirs2 = pos2dirs[p2]
                    dirs2 != null && dirs2.contains((it + 2) % 4)
                }) isSolved = false
            }
        // 3. The numbers along the edge indicate the stretch of the nearest section
        //    of road from that point, in corresponding row or column.
        for (r in 1 until rows - 1) {
            var n1 = 0
            var pHint = Position(r, 0)
            var n2 = game.pos2hint[pHint]!!
            for (c in 1 until cols - 1) {
                val dirs = pos2dirs[Position(r, c)] ?: listOf()
                val (b1, b2) = dirs.contains(1) to dirs.contains(3)
                if (b1 && !b2 && n1 == 0 || b1 && b2 && n1 > 0)
                    n1 += 1
                else if (!b1 && b2 && n1 > 0) {
                    n1 += 1
                    break
                } else if (n1 > 0)
                    break
            }
            var s = if (n2 == CrosstownTrafficGame.PUZ_UNKNOWN || n1 == n2) HintState.Complete else if (n1 < n2) HintState.Normal else HintState.Error
            pos2state[pHint] = s
            if (s != HintState.Complete) isSolved = false
            n1 = 0
            pHint = Position(r, cols - 1)
            n2 = game.pos2hint[pHint]!!
            for (c in cols - 2 downTo 1) {
                val dirs = pos2dirs[Position(r, c)] ?: listOf()
                val (b1, b2) = dirs.contains(3) to dirs.contains(1)
                if (b1 && !b2 && n1 == 0 || b1 && b2 && n1 > 0)
                    n1 += 1
                else if (!b1 && b2 && n1 > 0) {
                    n1 += 1
                    break
                } else if (n1 > 0)
                    break
            }
            s = if (n2 == CrosstownTrafficGame.PUZ_UNKNOWN || n1 == n2) HintState.Complete else if (n1 < n2) HintState.Normal else HintState.Error
            pos2state[pHint] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (c in 1 until cols - 1) {
            var n1 = 0
            var pHint = Position(0, c)
            var n2 = game.pos2hint[pHint]!!
            for (r in 1 until rows - 1) {
                val dirs = pos2dirs[Position(r, c)] ?: listOf()
                val (b1, b2) = dirs.contains(2) to dirs.contains(0)
                if (b1 && !b2 && n1 == 0 || b1 && b2 && n1 > 0)
                    n1 += 1
                else if (!b1 && b2 && n1 > 0) {
                    n1 += 1
                    break
                } else if (n1 > 0)
                    break
            }
            var s = if (n2 == CrosstownTrafficGame.PUZ_UNKNOWN || n1 == n2) HintState.Complete else if (n1 < n2) HintState.Normal else HintState.Error
            pos2state[pHint] = s
            if (s != HintState.Complete) isSolved = false
            n1 = 0
            pHint = Position(rows - 1, c)
            n2 = game.pos2hint[pHint]!!
            for (r in rows - 2 downTo 1) {
                val dirs = pos2dirs[Position(r, c)] ?: listOf()
                val (b1, b2) = dirs.contains(0) to dirs.contains(2)
                if (b1 && !b2 && n1 == 0 || b1 && b2 && n1 > 0)
                    n1 += 1
                else if (!b1 && b2 && n1 > 0) {
                    n1 += 1
                    break
                } else if (n1 > 0)
                    break
            }
            s = if (n2 == CrosstownTrafficGame.PUZ_UNKNOWN || n1 == n2) HintState.Complete else if (n1 < n2) HintState.Normal else HintState.Error
            pos2state[pHint] = s
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // Check the loop
        val p = pos2dirs.keys.first()
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            if (dirs.size == 2) {
                pos2dirs.remove(p2)
                n = dirs.first { (it + 2) % 4 != n }
            } else {
                dirs.remove(n)
                dirs.remove((n + 2) % 4)
            }
            p2 += CrosstownTrafficGame.offset[n]
            if (p2 == p) break
        }
    }
}