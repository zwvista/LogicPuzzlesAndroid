package com.zwstudio.logicpuzzlesandroid.puzzles.zenlandscaper

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ZenLandscaperGameState(game: ZenLandscaperGame) : CellsGameState<ZenLandscaperGame, ZenLandscaperGameMove, ZenLandscaperGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ZenLandscaperGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != ' ' || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ZenLandscaperGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[move.p] != ' ') return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == ' ') '1' else if (o == '3') ' ' else (o.code + 1).toChar()
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 15/ZenLandscaper

        Summary
        Variety and Balance

        Description
        1. The Zen master has been very stressed as of late, to the point that
           yesterday he bolted for the Bahamas.
        2. The sun proved so irresistible, that he didn't even complete the
           Japanese Gardens he was working on.
        3. Being the Zen Apprentice, you are given the task to complete all of
           them following the master teaching of variety and continuity.
        4. The teaching says that any three contiguous tiles vertically,
           horizontally or diagonally must NOT be:
           -> all different
           -> all equal
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 4. The teaching says that any three contiguous tiles vertically,
        //    horizontally or diagonally must NOT be:
        //    -> all different
        //    -> all equal
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                for (i in 2 .. 4) {
                    val os = ZenLandscaperGame.offset[i]
                    val tiles = mutableListOf(p)
                    var p2 = p + os
                    for (j in 1..<3) {
                        if (!isValid(p2)) break
                        tiles.add(p2)
                        p2 += os
                    }
                    if (tiles.size < 3) continue
                    val chSet = tiles.map { this[it] }.toSet()
                    if (chSet.contains(' ')) {
                        isSolved = false
                        continue
                    }
                    if (chSet.size != 2) {
                        isSolved = false
                        for (p2 in tiles)
                            pos2state[p2] = AllowedObjectState.Error
                    }
                }
            }
    }
}