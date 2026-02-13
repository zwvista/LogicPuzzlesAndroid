package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic.CrosstownTrafficGame
import com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic.CrosstownTrafficObject
import com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic.PipemaniaObject

class PipemaniaGameState(game: PipemaniaGame) : CellsGameState<PipemaniaGame, PipemaniaGameMove, PipemaniaGameState>(game) {
    var objArray = game.objArray.copyOf()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: PipemaniaObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: PipemaniaObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PipemaniaGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != PipemaniaObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PipemaniaGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != PipemaniaObject.Empty) return GameOperationType.Invalid
        move.obj = when (this[p]) {
            PipemaniaObject.Empty -> PipemaniaObject.UpRight
            PipemaniaObject.UpRight -> PipemaniaObject.DownRight
            PipemaniaObject.DownRight -> PipemaniaObject.LeftDown
            PipemaniaObject.LeftDown -> PipemaniaObject.LeftUp
            PipemaniaObject.LeftUp -> PipemaniaObject.Horizontal
            PipemaniaObject.Horizontal -> PipemaniaObject.Vertical
            PipemaniaObject.Vertical -> PipemaniaObject.Cross
            PipemaniaObject.Cross -> PipemaniaObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 5/Pipemania

        Summary
        Back to the 80s

        Description
        1. The former contractor for your present client left the work unfinished.
           In order not to waste what has bee done, you should complete the pipe
           loop, using the pieces available.
        2. Complete the board using all the tiles and form a single closed loop.
        3. The loop can cross itself.
        4. please note “a single closed loop" means that assuming the flow is straight
           even when the pipe crosses itself, i.e. following the pipe in straight lines
           (not turning at crossings).
    */
    private fun updateIsSolved() {
        val pos2dirs = mutableMapOf<Position, MutableList<Int>?>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2dirs[p] = when (this[p]) {
                    PipemaniaObject.UpRight -> mutableListOf(0, 1)
                    PipemaniaObject.DownRight -> mutableListOf(1, 2)
                    PipemaniaObject.LeftDown -> mutableListOf(2, 3)
                    PipemaniaObject.LeftUp -> mutableListOf(0, 3)
                    PipemaniaObject.Horizontal -> mutableListOf(1, 3)
                    PipemaniaObject.Vertical -> mutableListOf(0, 2)
                    PipemaniaObject.Cross -> mutableListOf(0, 1, 2, 3)
                    else -> null
                }
            }
        // 1. Draw a circuit (looping road)
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = pos2dirs[p]!!
                if (!dirs.all {
                        val p2 = p + CrosstownTrafficGame.offset[it]
                        val dirs2 = pos2dirs[p2]
                        dirs2 != null && dirs2.contains((it + 2) % 4)
                    }) { isSolved = false; return }
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
            var s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
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
            s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
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
            var s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
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
            s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
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