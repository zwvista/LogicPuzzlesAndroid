package com.zwstudio.logicpuzzlesandroid.puzzles.adifferentfarmer

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ADifferentFarmerGameState(game: ADifferentFarmerGame) : CellsGameState<ADifferentFarmerGame, ADifferentFarmerGameMove, ADifferentFarmerGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: ADifferentFarmerObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: ADifferentFarmerObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ADifferentFarmerGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != ADifferentFarmerObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ADifferentFarmerGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ADifferentFarmerObject.Empty) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            ADifferentFarmerObject.Empty -> ADifferentFarmerObject.Up
            ADifferentFarmerObject.Up -> ADifferentFarmerObject.Right
            ADifferentFarmerObject.Right -> ADifferentFarmerObject.Down
            ADifferentFarmerObject.Down -> ADifferentFarmerObject.Left
            ADifferentFarmerObject.Left -> ADifferentFarmerObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 4/A different Farmer

        Summary
        Not all farmers are created equal

        Description
        1. A Different Farmer plants fruits and vegetables in a different way.
        2. He places exactly one of each of the three fruits or vegetables in each field
           (marked area).
        3. The same plant cannot be placed in adjacent tiles, not even diagonally.
        4. All the plants must be connected horizontally or vertically.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 3. Arrows in an area should all be different, i.e. there can't be two
        //    similar arrows in an area.
        for (area in game.areas) {
            val symbol2range = mutableMapOf<ADifferentFarmerObject, MutableList<Position>>()
            for (p in area)
                symbol2range.getOrPut(this[p]) { mutableListOf() }.add(p)
            for ((_, range) in symbol2range)
                if (range.size > 1) {
                    isSolved = false
                    for (p in range)
                        pos2state[p] = AllowedObjectState.Error
                }
            if (symbol2range.contains(ADifferentFarmerObject.Empty))
                isSolved = false
        }
        if (!isSolved) return
        // 1. All the roads lead to ADifferentFarmer.
        // 2. Hence you should fill the remaining spaces with arrows and in the
        //    end, starting at any tile and following the arrows, you should get
        //    at the ADifferentFarmer icon.
        val validRange = mutableSetOf<Position>()
        val invalidRange = mutableSetOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                var p = Position(r, c)
                val range = mutableSetOf<Position>()
                while (true) {
                    val o = this[p]
                    if (o == ADifferentFarmerObject.ADifferentFarmer || validRange.contains(p)) {
                        for (p2 in range) { validRange.add(p2) }
                        break
                    }
                    if (!isValid(p) || invalidRange.contains(p) || range.contains(p)) {
                        isSolved = false
                        for (p2 in range) { invalidRange.add(p2) }
                        break
                    }
                    range.add(p)
                    val os = ADifferentFarmerGame.offset[o.ordinal - 2]
                    p += os
                }
            }
        for (p in invalidRange)
            pos2state[p] = AllowedObjectState.Error
    }
}
