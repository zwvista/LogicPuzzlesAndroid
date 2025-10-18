package com.zwstudio.logicpuzzlesandroid.puzzles.zenlandscaper

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ZenLandscaperGameState(game: ZenLandscaperGame) : CellsGameState<ZenLandscaperGame, ZenLandscaperGameMove, ZenLandscaperGameState>(game) {
    var objArray = Array<ZenLandscaperObject>(rows * cols) { ZenLandscaperEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ZenLandscaperObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ZenLandscaperObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

/**
 * Sets the object at a specified position in the game board.
 * This function validates the move, updates the board state, and checks if the puzzle is solved.
 *
 * @param move The move containing the position and object to be placed
 * @return GameOperationType indicating the result of the operation:
 *         - Invalid if the move is not valid or the object is the same as current
 *         - MoveComplete if the operation was successful
 */
    override fun setObject(move: ZenLandscaperGameMove): GameOperationType {
    // Check if the position is valid or if the object is already at the position
        if (!isValid(move.p) || this[move.p] === move.obj) return GameOperationType.Invalid
    // Update the board with the new object at the specified position
        this[move.p] = move.obj
    // Check if the puzzle has been solved after the move
        updateIsSolved()
    // Return success status
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ZenLandscaperGameMove): GameOperationType {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val o = this[p]
        move.obj = when (o) {
            is ZenLandscaperEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) ZenLandscaperMarkerObject else ZenLandscaperFilledObject()
            is ZenLandscaperFilledObject -> if (markerOption == MarkerOptions.MarkerLast) ZenLandscaperMarkerObject else ZenLandscaperEmptyObject
            is ZenLandscaperMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) ZenLandscaperFilledObject() else ZenLandscaperEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 14/ZenLandscaper

        Summary
        Puzzle Fever

        Description
        1. On the board a few ZenLandscaper are laid down. Your goal is  to fill
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
                if (this[p] is ZenLandscaperFilledObject) {
                    val s = if(canbeFilled) AllowedObjectState.Normal else AllowedObjectState.Error
                    if (s == AllowedObjectState.Error) isSolved = false
                    this[p] = ZenLandscaperFilledObject(s)
                } else {
                    if (allowedObjectsOnly && !canbeFilled)
                        this[p] = ZenLandscaperForbiddenObject
                    else if (this[p] is ZenLandscaperForbiddenObject)
                        this[p] = ZenLandscaperEmptyObject
                    canbeFilled = false
                }
        }
        for (r in 0 until rows) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols)
                if (this[r, c] is ZenLandscaperFilledObject)
                    n1++
            // 4. The numbers on the border tell you how many filled cells are present
            // on that Row.
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
            if (n1 == n2 && allowedObjectsOnly)
                for (c in 0 until cols)
                    if (this[r, c] !is ZenLandscaperFilledObject)
                        this[r, c] = ZenLandscaperForbiddenObject
        }
        for (c in 0 until cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows)
                if (this[r, c] is ZenLandscaperFilledObject)
                    n1++
            // 4. The numbers on the border tell you how many filled cells are present
            // on that Column.
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
            if (n1 == n2 && allowedObjectsOnly)
                for (r in 0 until rows)
                    if (this[r, c] !is ZenLandscaperFilledObject)
                        this[r, c] = ZenLandscaperForbiddenObject
        }
    }
}