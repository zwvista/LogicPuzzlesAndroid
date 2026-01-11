package com.zwstudio.logicpuzzlesandroid.puzzles.underground

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class UndergroundGameState(game: UndergroundGame) : CellsGameState<UndergroundGame, UndergroundGameMove, UndergroundGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: UndergroundObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: UndergroundObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: UndergroundGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != UndergroundObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: UndergroundGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            UndergroundObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) UndergroundObject.Marker else UndergroundObject.Balloon
            UndergroundObject.Balloon -> UndergroundObject.Weight
            UndergroundObject.Weight -> if (markerOption == MarkerOptions.MarkerLast) UndergroundObject.Marker else UndergroundObject.Empty
            UndergroundObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) UndergroundObject.Balloon else UndergroundObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 2/Underground

        Summary
        Subway entrances

        Description
        1. Each neighbourhood contains one entrance to the Underground.
        2. For each entrance there is a corresponding entrance in a different neighbourhood.
        3. The arrows of two corresponding entrances must point to each other.
        4. Between two corresponding entrances there cannot be any other entrance.
        5. Two corresponding entrances cannot be in adjacent neighbourhood, i.e.
           there must be at least one neighbourhood between them.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 1. Place a Balloon and a Weight in each Area.
        for (area in game.areas) {
            if (area.size == 1) continue
            val symbol2range = mutableMapOf<UndergroundObject, MutableList<Position>>()
            symbol2range[UndergroundObject.Balloon] = mutableListOf()
            symbol2range[UndergroundObject.Weight] = mutableListOf()
            for (p in area) {
                val o = this[p]
                if (o == UndergroundObject.Balloon || o == UndergroundObject.Weight)
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
        for (c in 0 until cols)
            for (r in 0 until rows) {
                val p = Position(r, c)
                // 3. A Balloon can be placed on the top of the board, under another Balloon,
                //    or under a Block.
                when (this[p]) {
                    UndergroundObject.Balloon ->
                        // 3. A Balloon can be placed on the top of the board, under another Balloon,
                        //    or under a Block.
                        if (!(r == 0 || this[r - 1, c] == UndergroundObject.Balloon || this[r - 1, c] == UndergroundObject.Block)) {
                            isSolved = false
                            pos2state[p] = AllowedObjectState.Error
                        }
                    UndergroundObject.Weight ->
                        // 4. A Weight can be placed on the bottom of the board, over another Weight,
                        //    or over a Block.
                        if (!(r == rows - 1 || this[r + 1, c] == UndergroundObject.Weight || this[r + 1, c] == UndergroundObject.Block)) {
                            isSolved = false
                            pos2state[p] = AllowedObjectState.Error
                        }
                    else -> {}
                }
            }
    }
}
