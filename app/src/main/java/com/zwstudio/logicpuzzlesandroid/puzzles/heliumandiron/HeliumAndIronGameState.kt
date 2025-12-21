package com.zwstudio.logicpuzzlesandroid.puzzles.heliumandiron

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HeliumAndIronGameState(game: HeliumAndIronGame) : CellsGameState<HeliumAndIronGame, HeliumAndIronGameMove, HeliumAndIronGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: HeliumAndIronObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: HeliumAndIronObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: HeliumAndIronGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != HeliumAndIronObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: HeliumAndIronGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != HeliumAndIronObject.Empty) return GameOperationType.Invalid
        val o = this[move.p]
        move.obj = when (o) {
            HeliumAndIronObject.Empty -> HeliumAndIronObject.Up
            HeliumAndIronObject.Up -> HeliumAndIronObject.Right
            HeliumAndIronObject.Right -> HeliumAndIronObject.Down
            HeliumAndIronObject.Down -> HeliumAndIronObject.Left
            HeliumAndIronObject.Left -> HeliumAndIronObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/HeliumAndIron

        Summary
        All roads lead to ...

        Description
        1. All the roads lead to HeliumAndIron.
        2. Hence you should fill the remaining spaces with arrows and in the
           end, starting at any tile and following the arrows, you should get
           at the HeliumAndIron icon.
        3. Arrows in an area should all be different, i.e. there can't be two
           similar arrows in an area.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2state[p] = AllowedObjectState.Normal
            }
        // 3. Arrows in an area should all be different, i.e. there can't be two
        //    similar arrows in an area.
        for (area in game.areas) {
            val symbol2range = mutableMapOf<HeliumAndIronObject, MutableList<Position>>()
            for (p in area)
                symbol2range.getOrPut(this[p]) { mutableListOf() }.add(p)
            for ((_, range) in symbol2range)
                if (range.size > 1)
                    for (p in range) {
                        isSolved = false
                        pos2state[p] = AllowedObjectState.Error
                    }
            if (symbol2range.contains(HeliumAndIronObject.Empty))
                isSolved = false
        }
        if (!isSolved) return
        // 1. All the roads lead to HeliumAndIron.
        // 2. Hence you should fill the remaining spaces with arrows and in the
        //    end, starting at any tile and following the arrows, you should get
        //    at the HeliumAndIron icon.
        val validRange = mutableSetOf<Position>()
        val invalidRange = mutableSetOf<Position>()
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                var p = Position(r, c)
                val range = mutableSetOf<Position>()
                while (true) {
                    val o = this[p]
                    if (o == HeliumAndIronObject.HeliumAndIron || validRange.contains(p)) {
                        for (p2 in range) { validRange.add(p2) }
                        break
                    }
                    if (!isValid(p) || invalidRange.contains(p) || range.contains(p)) {
                        isSolved = false
                        for (p2 in range) { invalidRange.add(p2) }
                        break
                    }
                    range.add(p)
                    val os = HeliumAndIronGame.offset[o.ordinal - 2]
                    p += os
                }
            }
        }
        for (p in invalidRange)
            pos2state[p] = AllowedObjectState.Error
    }
}
