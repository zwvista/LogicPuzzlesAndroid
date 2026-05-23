package com.zwstudio.logicpuzzlesandroid.puzzles.heliumandiron

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HeliumAndIronGameState(game: HeliumAndIronGame) : CellsGameState<HeliumAndIronGame, HeliumAndIronGameMove, HeliumAndIronGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

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
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            HeliumAndIronObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) HeliumAndIronObject.Marker else HeliumAndIronObject.Balloon
            HeliumAndIronObject.Balloon -> HeliumAndIronObject.Weight
            HeliumAndIronObject.Weight -> if (markerOption == MarkerOptions.MarkerLast) HeliumAndIronObject.Marker else HeliumAndIronObject.Empty
            HeliumAndIronObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) HeliumAndIronObject.Balloon else HeliumAndIronObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Helium And Iron

        Summary
        One rises, the other falls

        Description
        1. Place a Balloon and a Weight in each Area.
        2. Helium Balloons ten to float to the top, while Iron Weight tend to fall
           to the ground.
        3. A Balloon can be placed on the top of the board, under another Balloon,
           or under a Block.
        4. A Weight can be placed on the bottom of the board, over another Weight,
           or over a Block.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 1. Place a Balloon and a Weight in each Area.
        for (area in game.areas) {
            if (area.size == 1) continue
            val symbol2range = mutableMapOf<HeliumAndIronObject, MutableList<Position>>()
            symbol2range[HeliumAndIronObject.Balloon] = mutableListOf()
            symbol2range[HeliumAndIronObject.Weight] = mutableListOf()
            for (p in area) {
                val o = this[p]
                if (o == HeliumAndIronObject.Balloon || o == HeliumAndIronObject.Weight)
                    symbol2range[o]!!.add(p)
            }
            for ((_, range) in symbol2range)
                if (range.size != 1) {
                    isSolved = false
                    for (p in range)
                        pos2state[p] = AllowedObjectState.Error
                }
        }
        if (!isSolved) return
        // 2. Helium Balloons ten to float to the top, while Iron Weight tend to fall
        //    to the ground.
        for (c in 0..<cols)
            for (r in 0..<rows) {
                val p = Position(r, c)
                // 3. A Balloon can be placed on the top of the board, under another Balloon,
                //    or under a Block.
                when (this[p]) {
                    HeliumAndIronObject.Balloon ->
                        // 3. A Balloon can be placed on the top of the board, under another Balloon,
                        //    or under a Block.
                        if (!(r == 0 || this[r - 1, c] == HeliumAndIronObject.Balloon || this[r - 1, c] == HeliumAndIronObject.Block)) {
                            isSolved = false
                            pos2state[p] = AllowedObjectState.Error
                        }
                    HeliumAndIronObject.Weight ->
                        // 4. A Weight can be placed on the bottom of the board, over another Weight,
                        //    or over a Block.
                        if (!(r == rows - 1 || this[r + 1, c] == HeliumAndIronObject.Weight || this[r + 1, c] == HeliumAndIronObject.Block)) {
                            isSolved = false
                            pos2state[p] = AllowedObjectState.Error
                        }
                    else -> {}
                }
            }
    }
}
