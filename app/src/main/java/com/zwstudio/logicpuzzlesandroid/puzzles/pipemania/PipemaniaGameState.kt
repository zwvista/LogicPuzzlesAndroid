package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic.CrosstownTrafficGame

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
        isSolved = true
        val pos2dirs = mutableMapOf<Position, MutableList<Int>>()
        // 1. The former contractor for your present client left the work unfinished.
        //    In order not to waste what has bee done, you should complete the pipe
        //    loop, using the pieces available.
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
                    else -> mutableListOf()
                }
            }
        if (!isSolved) return
        // 2. Complete the board using all the tiles and form a single closed loop.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = pos2dirs[p]!!
                if (!dirs.all {
                    val p2 = p + PipemaniaGame.offset[it]
                    val dirs2 = pos2dirs[p2]
                    dirs2 != null && dirs2.contains((it + 2) % 4)
                }) { isSolved = false; return }
            }
        // 3. The loop can cross itself.
        // 4. please note “a single closed loop" means that assuming the flow is straight
        //    even when the pipe crosses itself, i.e. following the pipe in straight lines
        //    (not turning at crossings).
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