package com.zwstudio.logicpuzzlesandroid.puzzles.mirrors

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MirrorsGameState(game: MirrorsGame) : CellsGameState<MirrorsGame, MirrorsGameMove, MirrorsGameState>(game) {
    var cloner = Cloner()
    var objArray = game.objArray.copyOf()
    val pos2dirs = mutableMapOf<Position, List<Int>>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: MirrorsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: MirrorsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: MirrorsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != MirrorsObject.Empty || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: MirrorsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != MirrorsObject.Empty) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            MirrorsObject.Empty -> MirrorsObject.UpRight
            MirrorsObject.UpRight -> MirrorsObject.DownRight
            MirrorsObject.DownRight -> MirrorsObject.LeftDown
            MirrorsObject.LeftDown -> MirrorsObject.LeftUp
            MirrorsObject.LeftUp -> MirrorsObject.Horizontal
            MirrorsObject.Horizontal -> MirrorsObject.Vertical
            MirrorsObject.Vertical -> MirrorsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 10/Mirrors

        Summary
        Zip, swish, zoom! Lasers on mirrors!

        Description
        1. The goal is to draw a single, continuous, non-crossing path that fills
           the entire board.
        2. Some tiles are already given and can contain Mirrors, which force the
           path to make a turn. Other tiles already contain a fixed piece of straight
           path.
        3. Your task is to fill the remaining board tiles with straight or 90 degree
           path lines, in the end connecting a single, continuous line.
        4. Please note you can make 90 degree turn even there are no mirrors.

        Variant
        5. In the Maze variant, the path isn't closed. You have two spots on the
           board which represent the start and end of the path.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == MirrorsObject.Empty)
                    isSolved = false
                pos2dirs[p] = when (this[p]) {
                    MirrorsObject.UpRight -> listOf(0, 1)
                    MirrorsObject.DownRight -> listOf(1, 2)
                    MirrorsObject.LeftDown -> listOf(2, 3)
                    MirrorsObject.LeftUp -> listOf(0, 3)
                    MirrorsObject.Horizontal -> listOf(1, 3)
                    MirrorsObject.Vertical -> listOf(0, 2)
                    else -> listOf()
                }
            }
        if (!isSolved) return
        val pos2dirs = cloner.deepClone(pos2dirs)
        // 1. The goal is to draw a single, continuous, non-crossing path that fills
        //    the entire board.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = pos2dirs[p]!!
                if (!dirs.all {
                    val p2 = p + MirrorsGame.offset[it]
                    val dirs2 = pos2dirs[p2]
                    this[p2] == MirrorsObject.Spot || dirs2 != null && dirs2.contains((it + 2) % 4)
                }) { isSolved = false; return }
            }
        if (game.spots.isEmpty()) {
            // Check the loop
            val p = pos2dirs.keys.first()
            var p2 = p
            var n = -1
            while (true) {
                val dirs = pos2dirs[p2]
                if (dirs == null) { isSolved = false; return }
                pos2dirs.remove(p2)
                n = dirs.first { (it + 2) % 4 != n }
                p2 += MirrorsGame.offset[n]
                if (p2 == p) break
            }
        } else {
            val (ps0, ps1) = game.spots[0] to game.spots[1]
            val rng = pos2dirs.filter { (p, dirs) ->
                dirs.any { p + MirrorsGame.offset[it] == ps0 }
            }
            if (rng.size != 1) { isSolved = false; return }
            var p2 = rng.firstNotNullOf { (p, _) -> p }
            var n = rng.firstNotNullOf { (_, dirs) -> dirs.first { p2 + MirrorsGame.offset[it] == ps0 } }
            while (true) {
                val dirs = pos2dirs[p2]
                if (dirs == null) { isSolved = false; return }
                pos2dirs.remove(p2)
                n = dirs.first { (it + 2) % 4 != n }
                p2 += MirrorsGame.offset[n]
                if (p2 == ps1) break
            }
        }
    }
}