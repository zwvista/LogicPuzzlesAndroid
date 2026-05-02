package com.zwstudio.logicpuzzlesandroid.puzzles.farmer

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FarmerGameState(game: FarmerGame) : CellsGameState<FarmerGame, FarmerGameMove, FarmerGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FarmerObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FarmerObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FarmerGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != FarmerObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FarmerGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != FarmerObject.Empty) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            FarmerObject.Empty -> FarmerObject.Up
            FarmerObject.Up -> FarmerObject.Right
            FarmerObject.Right -> FarmerObject.Down
            FarmerObject.Down -> FarmerObject.Left
            FarmerObject.Left -> FarmerObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Farmer

        Summary
        Vegetable Gardener

        Description
        1. A Farmer has a scientific way to work his field:
        2. He plants three types of fruits or vegetables.
        3. Each area must contain either three identical plants or three different plants.
        4. When two plants are orthogonally adjacent across an area, they must be different.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 3. Arrows in an area should all be different, i.e. there can't be two
        //    similar arrows in an area.
        for (area in game.areas) {
            val symbol2range = mutableMapOf<FarmerObject, MutableList<Position>>()
            for (p in area)
                symbol2range.getOrPut(this[p]) { mutableListOf() }.add(p)
            for ((_, range) in symbol2range)
                if (range.size > 1) {
                    isSolved = false
                    for (p in range)
                        pos2state[p] = AllowedObjectState.Error
                }
            if (symbol2range.contains(FarmerObject.Empty))
                isSolved = false
        }
        if (!isSolved) return
        // 1. All the roads lead to Farmer.
        // 2. Hence you should fill the remaining spaces with arrows and in the
        //    end, starting at any tile and following the arrows, you should get
        //    at the Farmer icon.
        val validRange = mutableSetOf<Position>()
        val invalidRange = mutableSetOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                var p = Position(r, c)
                val range = mutableSetOf<Position>()
                while (true) {
                    val o = this[p]
                    if (o == FarmerObject.Farmer || validRange.contains(p)) {
                        for (p2 in range) { validRange.add(p2) }
                        break
                    }
                    if (!isValid(p) || invalidRange.contains(p) || range.contains(p)) {
                        isSolved = false
                        for (p2 in range) { invalidRange.add(p2) }
                        break
                    }
                    range.add(p)
                    val os = FarmerGame.offset[o.ordinal - 2]
                    p += os
                }
            }
        for (p in invalidRange)
            pos2state[p] = AllowedObjectState.Error
    }
}
