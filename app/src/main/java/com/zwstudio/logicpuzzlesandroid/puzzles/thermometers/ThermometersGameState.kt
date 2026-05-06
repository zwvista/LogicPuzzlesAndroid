package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ThermometersGameState(game: ThermometersGame) : CellsGameState<ThermometersGame, ThermometersGameMove, ThermometersGameState>(game) {
    var objArray = Array(rows * cols) { ThermometersObject.Empty }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ThermometersObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ThermometersObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ThermometersGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] === move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ThermometersGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            ThermometersObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) ThermometersObject.Marker else ThermometersObject.Filled
            ThermometersObject.Filled -> if (markerOption == MarkerOptions.MarkerLast) ThermometersObject.Marker else ThermometersObject.Empty
            ThermometersObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) ThermometersObject.Filled else ThermometersObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 14/Thermometers

        Summary
        Puzzle Fever

        Description
        1. On the board a few Thermometers are laid down. Your goal is  to fill
           them according to the hints.
        2. In a Thermometer, mercury always starts at the bulb and can progressively
           fill the Thermometer towards the end.
        3. A Thermometer can also be completely empty, including the bulb.
        4. The numbers on the border tell you how many filled cells are present
           on that Row or Column.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        // 2. In a Thermometer, mercury always starts at the bulb and can progressively
        // fill the Thermometer towards the end.
        for (thermometer in game.thermometers) {
            var canbeFilled = true
            for (p in thermometer)
                if (this[p] == ThermometersObject.Filled) {
                    val s = if(canbeFilled) AllowedObjectState.Normal else AllowedObjectState.Error
                    if (s == AllowedObjectState.Error) isSolved = false
                    pos2state[p] = s
                } else {
                    if (allowedObjectsOnly && !canbeFilled)
                        this[p] = ThermometersObject.Forbidden
                    else if (this[p] == ThermometersObject.Forbidden)
                        this[p] = ThermometersObject.Empty
                    canbeFilled = false
                }
        }
        for (r in 0..<rows) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0..<cols)
                if (this[r, c] == ThermometersObject.Filled)
                    n1++
            // 4. The numbers on the border tell you how many filled cells are present
            // on that Row.
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
            if (n1 == n2 && allowedObjectsOnly)
                for (c in 0..<cols)
                    if (this[r, c] != ThermometersObject.Filled)
                        this[r, c] = ThermometersObject.Forbidden
        }
        for (c in 0..<cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0..<rows)
                if (this[r, c] == ThermometersObject.Filled)
                    n1++
            // 4. The numbers on the border tell you how many filled cells are present
            // on that Column.
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
            if (n1 == n2 && allowedObjectsOnly)
                for (r in 0..<rows)
                    if (this[r, c] != ThermometersObject.Filled)
                        this[r, c] = ThermometersObject.Forbidden
        }
    }
}