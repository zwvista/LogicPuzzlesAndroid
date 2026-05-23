package com.zwstudio.logicpuzzlesandroid.puzzles.guessthelabyrinth

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GuessTheLabyrinthGameState(game: GuessTheLabyrinthGame) : CellsGameState<GuessTheLabyrinthGame, GuessTheLabyrinthGameMove, GuessTheLabyrinthGameState>(game) {
    val objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: GuessTheLabyrinthGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + GuessTheLabyrinthGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: GuessTheLabyrinthGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 3/Puzzle Set 4/Guess the Labyrinth

        Summary
        Before solving it

        Description
        1. There is a hidden Labyrinth in the board.
        2. The Labyrinth is a one-square wide path which doesn't branch out and
           that forms a closed loop
        3. The intersections where three lines meet are marked with a dot
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] == GridLineObject.Line }
                // 3. The intersections where three lines meet are marked with a dot
                if ((dirs.size == 3) != game.posts.contains(p)) {
                    isSolved = false; return
                }
            }
        // 2. The Labyrinth is a one-square wide path which doesn't branch out and
        //    that forms a closed loop
        val pos2dirs = mutableMapOf<Position, MutableList<Int>>()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val dirs = (0..<4).filter {
                    this[p + GuessTheLabyrinthGame.offset2[it]][GuessTheLabyrinthGame.dirs[it]] != GridLineObject.Line
                }.toMutableList()
                if (dirs.size != 2) { isSolved = false; return }
                pos2dirs[p] = dirs
            }
        // Check the loop
        val p = pos2dirs.keys.first()
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += GuessTheLabyrinthGame.offset[n]
            if (p2 == p) break
        }
    }
}