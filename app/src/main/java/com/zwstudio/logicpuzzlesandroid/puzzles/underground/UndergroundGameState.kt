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
            UndergroundObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) UndergroundObject.Marker else UndergroundObject.Up
            UndergroundObject.Up -> UndergroundObject.Right
            UndergroundObject.Right -> UndergroundObject.Down
            UndergroundObject.Down -> UndergroundObject.Left
            UndergroundObject.Left -> if (markerOption == MarkerOptions.MarkerLast) UndergroundObject.Marker else UndergroundObject.Empty
            UndergroundObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) UndergroundObject.Up else UndergroundObject.Empty
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        val nUp = UndergroundObject.Up.ordinal
        isSolved = true
        for (r in 0..<rows) {
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] == UndergroundObject.Forbidden)
                    this[p] = UndergroundObject.Empty
                pos2state[p] = AllowedObjectState.Normal
            }
        }
        // 1. Each neighbourhood contains one entrance to the Underground.
        for (area in game.areas) {
            val rng = area.filter { this[it].ordinal >= nUp }
            if (rng.size != 1) {
                isSolved = false
                for (p in rng) pos2state[p] = AllowedObjectState.Error
            }
            if (allowedObjectsOnly && rng.isNotEmpty())
                area.filter { this[it] == UndergroundObject.Empty }.forEach {
                    this[it] = UndergroundObject.Forbidden
                }
        }
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val d = this[p].ordinal - nUp
                if (d < 0) continue
                val n1 = game.pos2area[p]!!
                val os = UndergroundGame.offset[d]
                var p2 = p + os
                val rng = mutableListOf<Position>()
                var s = AllowedObjectState.Error
                while (isValid(p2)) {
                    val d2 = this[p2].ordinal - nUp
                    if (d2 < 0) {
                        rng.add(p2)
                        p2 += os
                    } else {
                        // 2. For each entrance there is a corresponding entrance in a different neighbourhood.
                        // 3. The arrows of two corresponding entrances must point to each other.
                        // 4. Between two corresponding entrances there cannot be any other entrance.
                        // 5. Two corresponding entrances cannot be in adjacent neighbourhood, i.e.
                        //    there must be at least one neighbourhood between them.
                        val n2 = game.pos2area[p2]!!
                        if (n2 != n1 && rng.any {
                            val n3 = game.pos2area[it]!!
                            n3 != n1 && n3 != n2
                        } && (d + 2) % 4 == d2) s = AllowedObjectState.Normal
                        break
                    }
                }
                if (s == AllowedObjectState.Error) isSolved = false
                pos2state[p] = s
                if (allowedObjectsOnly && s == AllowedObjectState.Normal)
                    rng.filter { this[it] == UndergroundObject.Empty }.forEach {
                        this[it] = UndergroundObject.Forbidden
                    }
            }
    }
}
