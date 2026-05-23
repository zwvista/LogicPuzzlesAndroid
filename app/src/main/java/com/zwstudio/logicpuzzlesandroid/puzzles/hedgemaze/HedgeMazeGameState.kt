package com.zwstudio.logicpuzzlesandroid.puzzles.hedgemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HedgeMazeGameState(game: HedgeMazeGame) : CellsGameState<HedgeMazeGame, HedgeMazeGameMove, HedgeMazeGameState>(game) {
    val objArray = game.objArray.copyOf()
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        // 4. On the board there can't be a 2x2 area all made of hedges or all without hedges (empty).
        // 5. Tiles with any icon count as empty and cannot be filled with hedges.
        invalid2x2Squares.clear()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val hedgeAreas = mutableSetOf<Int>()
                val iconAreas = mutableSetOf<Int>()
                val emptyAreas = mutableSetOf<Int>()
                for (os in HedgeMazeGame.offset3) {
                    val p2 = p + os
                    val id = game.pos2area[p2]!!
                    val o = this[p2]
                    if (o == HedgeMazeObject.Hedge)
                        hedgeAreas.add(id)
                    else if (!game.iconlessAreas.contains(id))
                        iconAreas.add(id)
                    else
                        emptyAreas.add(id)
                }
                if (hedgeAreas.isEmpty() || iconAreas.isEmpty() && emptyAreas.isEmpty()) {
                    invalid2x2Squares.add(p + Position.SouthEast); isSolved = false
                } else if (allowedObjectsOnly && iconAreas.isEmpty() && emptyAreas.size == 1) {
                    val area = game.areas[emptyAreas.first()]
                    for (p2 in area)
                        this[p2] = HedgeMazeObject.Forbidden
                }
            }
        if (!isSolved) return
        // 2. The maze should be one tile wide. It can branch itself, but not close in a loop.
        val rng = mutableSetOf<Position>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] != HedgeMazeObject.Hedge)
                    rng.add(p)
            }
        val moves = mutableSetOf<Position>()
        fun dfs(p: Position, n: Int): Boolean {
            if (!moves.add(p)) return false
            for (i in 0..<4) {
                if (i == n) continue
                val p2 = p + HedgeMazeGame.offset[i]
                if (!rng.contains(p2)) continue
                if (!dfs(p2, (i + 2) % 4)) return false
            }
            return true
        }
        if (!(dfs(rng.first(), -1) && moves.size == rng.size)) { isSolved = false; return }
        // 3. There should be a path between the two gates. This path should pass on
        //    all the steps and not on any fountain.
        val gate1 = game.gates[0]
        val gate2 = game.gates[1]
        moves.clear()
        fun dfs2(p: Position, n: Int): Boolean {
            val o = this[p]
            if (o == HedgeMazeObject.Fountain) return false
            moves.add(p)
            if (p == gate2) return true
            for (i in 0..<4) {
                if (i == n) continue
                val p2 = p + HedgeMazeGame.offset[i]
                if (!rng.contains(p2)) continue
                if (dfs2(p2, (i + 2) % 4)) return true
            }
            moves.remove(p)
            return false
        }
        if (!(dfs2(gate1, -1) && game.steps.all {
            moves.contains(it)
        })) isSolved = false
    }
}
