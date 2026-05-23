package com.zwstudio.logicpuzzlesandroid.puzzles.zensolitaire

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.zengardens.ZenGardensGame
import kotlin.math.sign

class ZenSolitaireGameState(game: ZenSolitaireGame) : CellsGameState<ZenSolitaireGame, ZenSolitaireGameMove, ZenSolitaireGameState>(game) {
    val objArray = IntArray(rows * cols)
    var lastMove: ZenSolitaireGameMove? = null

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        for (p in game.stones)
            this[p] = ZenSolitaireGame.PUZ_STONE
        updateIsSolved()
    }

    override fun setObject(move: ZenSolitaireGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] != ZenSolitaireGame.PUZ_STONE || this[move.p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        lastMove = move
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ZenSolitaireGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] != ZenSolitaireGame.PUZ_STONE) return GameOperationType.Invalid
        // 3. From a stone, you can move horizontally or vertically to the next stone. You can't
        //    jump over stones, if you encounter it, you have to pick it up.
        // 5. when a stone has been picked up, you can pass away it if you encounter it again
        //    (it's not there anymore).
        fun f(p1: Position, p2: Position) : Pair<Boolean, Int> {
            val (r1, c1) = p1
            val (r2, c2) = p2
            if (!(r1 == r2 || c1 == c2))
                return Pair(false, -1)
            val os = Position((r2 - r1).sign, (c2 - c1).sign)
            var p3 = p1 + os
            while (p3 != p2) {
                if (this[p3] == ZenSolitaireGame.PUZ_STONE) return Pair(false, -1)
                p3 += os
            }
            val dir = ZenGardensGame.offset.indexOf(os)
            return Pair(true, dir)
        }
        // 2. You can start at any stone and pick it up (just to click on it and it will be numbered
        //    in the order you pick it up).
        if (lastMove == null) {
            move.dir = -1
            move.obj = 1
        } else {
            val (ok, dir) = f(lastMove!!.p, p)
            // 4. When moving from a stone to another, you can change direction, but you cannot reverse it.
            if (!ok || dir == (lastMove!!.dir + 2) % 4)
                return GameOperationType.Invalid
            move.dir = dir
            move.obj = lastMove!!.obj + 1
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 15/ZenSolitaire

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
        // 6. The goal is to pick up every stone.
        for (r in 0..<rows)
            for (c in 0..<cols)
                if (this[r, c] == ZenSolitaireGame.PUZ_STONE) {
                    isSolved = false
                    return
                }
    }
}