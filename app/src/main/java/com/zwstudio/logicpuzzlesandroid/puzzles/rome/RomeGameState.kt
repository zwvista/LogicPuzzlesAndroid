package com.zwstudio.logicpuzzlesandroid.puzzles.rome

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RomeGameState(game: RomeGame) : CellsGameState<RomeGame, RomeGameMove, RomeGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: RomeObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: RomeObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: RomeGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != RomeObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: RomeGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != RomeObject.Empty) return GameOperationType.Invalid
        val o = this[move.p]
        move.obj = when (o) {
            RomeObject.Empty -> RomeObject.Up
            RomeObject.Up -> RomeObject.Right
            RomeObject.Right -> RomeObject.Down
            RomeObject.Down -> RomeObject.Left
            RomeObject.Left -> RomeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Rome

        Summary
        All roads lead to ...

        Description
        1. All the roads lead to Rome.
        2. Hence you should fill the remaining spaces with arrows and in the
           end, starting at any tile and following the arrows, you should get
           at the Rome icon.
        3. Arrows in an area should all be different, i.e. there can't be two
           similar arrows in an area.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 3. Arrows in an area should all be different, i.e. there can't be two
        //    similar arrows in an area.
        for (area in game.areas) {
            val symbol2range = mutableMapOf<RomeObject, MutableList<Position>>()
            for (p in area)
                symbol2range.getOrPut(this[p]) { mutableListOf() }.add(p)
            for ((_, range) in symbol2range)
                if (range.size > 1) {
                    isSolved = false
                    for (p in range)
                        pos2state[p] = AllowedObjectState.Error
                }
            if (symbol2range.contains(RomeObject.Empty))
                isSolved = false
        }
        if (!isSolved) return
        // 1. All the roads lead to Rome.
        // 2. Hence you should fill the remaining spaces with arrows and in the
        //    end, starting at any tile and following the arrows, you should get
        //    at the Rome icon.
        val validRange = mutableSetOf<Position>()
        val invalidRange = mutableSetOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                var p = Position(r, c)
                val range = mutableSetOf<Position>()
                while (true) {
                    val o = this[p]
                    if (o == RomeObject.Rome || validRange.contains(p)) {
                        for (p2 in range) { validRange.add(p2) }
                        break
                    }
                    if (!isValid(p) || invalidRange.contains(p) || range.contains(p)) {
                        isSolved = false
                        for (p2 in range) { invalidRange.add(p2) }
                        break
                    }
                    range.add(p)
                    val os = RomeGame.offset[o.ordinal - 2]
                    p += os
                }
            }
        for (p in invalidRange)
            pos2state[p] = AllowedObjectState.Error
    }
}
