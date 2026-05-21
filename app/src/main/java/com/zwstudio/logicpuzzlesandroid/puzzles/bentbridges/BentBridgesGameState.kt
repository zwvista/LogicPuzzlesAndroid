package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.MasyuGame

class BentBridgesGameState(game: BentBridgesGame) : CellsGameState<BentBridgesGame, BentBridgesGameMove, BentBridgesGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Array<Boolean>) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: BentBridgesGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + MasyuGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Bent Bridges

        Summary
        One turn at most

        Description
        1. Connect all the islands together with bridges.
        2. You should be able to go from any island to any other island in the end.
        3. The number on the island tells you how many bridges connect to that island.
        4. A bridge can turn once by 90 degrees between islands.
        5. Bridges cannot cross each other.

        Variants
        6. Crossing: bridges can cross each other, but cannot turn at the intersection.
        7. Magnetic: islands with the same number cannot have a bridge between themselves.
    */
    private fun updateIsSolved() {
        isSolved = true
    }
}
