package com.zwstudio.logicpuzzlesandroid.puzzles.hedgemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HedgeMazeGameState(game: HedgeMazeGame) : CellsGameState<HedgeMazeGame, HedgeMazeGameMove, HedgeMazeGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: HedgeMazeObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: HedgeMazeObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: HedgeMazeGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != HedgeMazeObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: HedgeMazeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != HedgeMazeObject.Empty) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            HedgeMazeObject.Empty -> HedgeMazeObject.Up
            HedgeMazeObject.Up -> HedgeMazeObject.Right
            HedgeMazeObject.Right -> HedgeMazeObject.Down
            HedgeMazeObject.Down -> HedgeMazeObject.Left
            HedgeMazeObject.Left -> HedgeMazeObject.Empty
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
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 3. Arrows in an area should all be different, i.e. there can't be two
        //    similar arrows in an area.
        for (area in game.areas) {
            val symbol2range = mutableMapOf<HedgeMazeObject, MutableList<Position>>()
            for (p in area)
                symbol2range.getOrPut(this[p]) { mutableListOf() }.add(p)
            for ((_, range) in symbol2range)
                if (range.size > 1) {
                    isSolved = false
                    for (p in range)
                        pos2state[p] = AllowedObjectState.Error
                }
            if (symbol2range.contains(HedgeMazeObject.Empty))
                isSolved = false
        }
        if (!isSolved) return
        // 1. All the roads lead to HedgeMaze.
        // 2. Hence you should fill the remaining spaces with arrows and in the
        //    end, starting at any tile and following the arrows, you should get
        //    at the HedgeMaze icon.
        val validRange = mutableSetOf<Position>()
        val invalidRange = mutableSetOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                var p = Position(r, c)
                val range = mutableSetOf<Position>()
                while (true) {
                    val o = this[p]
                    if (o == HedgeMazeObject.HedgeMaze || validRange.contains(p)) {
                        for (p2 in range) { validRange.add(p2) }
                        break
                    }
                    if (!isValid(p) || invalidRange.contains(p) || range.contains(p)) {
                        isSolved = false
                        for (p2 in range) { invalidRange.add(p2) }
                        break
                    }
                    range.add(p)
                    val os = HedgeMazeGame.offset[o.ordinal - 2]
                    p += os
                }
            }
        for (p in invalidRange)
            pos2state[p] = AllowedObjectState.Error
    }
}
