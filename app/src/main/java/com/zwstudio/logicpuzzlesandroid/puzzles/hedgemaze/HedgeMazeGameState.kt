package com.zwstudio.logicpuzzlesandroid.puzzles.hedgemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HedgeMazeGameState(game: HedgeMazeGame) : CellsGameState<HedgeMazeGame, HedgeMazeGameMove, HedgeMazeGameState>(game) {
    var objArray = game.objArray.copyOf()
    val invalid2x2Squares = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: HedgeMazeObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: HedgeMazeObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: HedgeMazeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || !game.iconlessAreas.contains(game.pos2area[p]!!) || this[p] == move.obj) return GameOperationType.Invalid
        for (p2 in game.areas[game.pos2area[p]!!])
            this[p2] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: HedgeMazeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || !game.iconlessAreas.contains(game.pos2area[p]!!)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            HedgeMazeObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) HedgeMazeObject.Marker else HedgeMazeObject.Hedge
            HedgeMazeObject.Hedge -> if (markerOption == MarkerOptions.MarkerLast) HedgeMazeObject.Marker else HedgeMazeObject.Empty
            HedgeMazeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) HedgeMazeObject.Hedge else HedgeMazeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Hedge Maze

        Summary
        Wendy ?

        Description
        1. Fill some of the empty areas with hedges, thus forming a maze.
        2. The maze should be one tile wide. It can branch itself, but not close in a loop.
        3. There should be a path between the two gates. This path should pass on
           all the steps and not on any fountain.
        4. On the board there can't be a 2x2 area all made of hedges or all without hedges (empty).
        5. Tiles with any icon count as empty and cannot be filled with hedges.
    */
    private fun updateIsSolved() {
        isSolved = true
        invalid2x2Squares.clear()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                if (HedgeMazeGame.offset3.map { p + it }.all { this[it] == HedgeMazeObject.Hedge } ||
                    HedgeMazeGame.offset3.map { p + it }.all { this[it] != HedgeMazeObject.Hedge }) {
                    invalid2x2Squares.add(p + Position.SouthEast); isSolved = false
                }
            }
        if (!isSolved) return
    }
}
