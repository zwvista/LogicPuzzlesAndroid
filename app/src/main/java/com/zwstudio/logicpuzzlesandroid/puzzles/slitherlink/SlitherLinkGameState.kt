package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SlitherLinkGameState(game: SlitherLinkGame) : CellsGameState<SlitherLinkGame, SlitherLinkGameMove, SlitherLinkGameState>(game) {
    val objArray = Array(rows * cols) { Array(4) { GridLineObject.Empty } }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<GridLineObject>) {this[p.row, p.col] = obj}
    private fun isValidMove(move: SlitherLinkGameMove) = !(move.p.row == rows - 1 && move.dir == 2 || move.p.col == cols - 1 && move.dir == 1)

    init {
        updateIsSolved()
    }

    override fun setObject(move: SlitherLinkGameMove): GameOperationType {
        if (!isValidMove(move)) return GameOperationType.Invalid
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + SlitherLinkGame.offset[dir]
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SlitherLinkGameMove): GameOperationType {
        if (!isValidMove(move)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[move.p][move.dir]) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/SlitherLink

        Summary
        Draw a loop a-la-minesweeper!

        Description
        1. Draw a single looping path with the aid of the numbered hints. The path
           cannot have branches or cross itself.
        2. Each number in a tile tells you on how many of its four sides are touched
           by the path.
        3. For example:
        4. A 0 tells you that the path doesn't touch that square at all.
        5. A 1 tells you that the path touches that square ONLY one-side.
        6. A 3 tells you that the path does a U-turn around that square.
        7. There can't be tiles marked with 4 because that would form a single
           closed loop in it.
        8. Empty tiles can have any number of sides touched by that path.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. Each number in a tile tells you on how many of its four sides are touched
        // by the path.
        for ((p, n2) in game.pos2hint) {
            val n1 = (0..<4).count { i -> this[p + SlitherLinkGame.offset2[i]][SlitherLinkGame.dirs[i]] == GridLineObject.Line }
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        if (!isSolved) return
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] == GridLineObject.Line }
                if (dirs.size == 2)
                    // 1. Draw a single looping path
                    pos2dirs[p] = dirs
                else if (dirs.isNotEmpty()) {
                    // 1. The path cannot have branches or cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += SlitherLinkGame.offset[n]
            if (p2 == p) break
        }
    }
}