package com.zwstudio.logicpuzzlesandroid.puzzles.thegreylabyrinth

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheGreyLabyrinthGameState(game: TheGreyLabyrinthGame) : CellsGameState<TheGreyLabyrinthGame, TheGreyLabyrinthGameMove, TheGreyLabyrinthGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TheGreyLabyrinthObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TheGreyLabyrinthObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TheGreyLabyrinthGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != TheGreyLabyrinthObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TheGreyLabyrinthGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != TheGreyLabyrinthObject.Empty) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            TheGreyLabyrinthObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) TheGreyLabyrinthObject.Marker else TheGreyLabyrinthObject.Wall
            TheGreyLabyrinthObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) TheGreyLabyrinthObject.Marker else TheGreyLabyrinthObject.Empty
            TheGreyLabyrinthObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) TheGreyLabyrinthObject.Wall else TheGreyLabyrinthObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/The Grey Labyrinth

        Summary
        Maze Curator
    
        Description
        1. Find the walls that divide the board in a Labyrinth.
        2. The Labyrinth must have these rules:
        3. Walls can't touch each other orthogonally.
        4. From any location, there must only be one route to the treasure.
        5. You must follow the arrows, where present.
    */
    private fun updateIsSolved() {
        fun isEmpty(p: Position) = this[p] != TheGreyLabyrinthObject.Wall
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val walls = mutableListOf<Position>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (this[p]) {
                    TheGreyLabyrinthObject.Forbidden -> this[p] = TheGreyLabyrinthObject.Empty
                    TheGreyLabyrinthObject.Wall ->  {
                        pos2state[p] = AllowedObjectState.Normal
                        walls.add(p)
                    }
                    else -> {}
                }
            }
        // 3. Walls can't touch each other orthogonally.
        for (p in walls)
            for (os in TheGreyLabyrinthGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) continue
                when (this[p2]) {
                    TheGreyLabyrinthObject.Wall -> {
                        isSolved = false
                        pos2state[p] = AllowedObjectState.Error
                    }
                    TheGreyLabyrinthObject.Empty -> {
                        if (allowedObjectsOnly)
                            this[p2] = TheGreyLabyrinthObject.Forbidden
                    }
                    else -> {}
                }
            }
        if (!isSolved) return
        // 4. From any location, there must only be one route to the treasure.
        val rng = mutableSetOf<Position>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] != TheGreyLabyrinthObject.Wall)
                    rng.add(p)
            }
        val moves = mutableSetOf<Position>()
        fun dfs(p: Position, n: Int): Boolean {
            if (!moves.add(p)) return false
            // 5. You must follow the arrows, where present.
            val n2 = when (this[p]) {
                TheGreyLabyrinthObject.Up -> 0
                TheGreyLabyrinthObject.Right -> 1
                TheGreyLabyrinthObject.Down -> 2
                TheGreyLabyrinthObject.Left -> 3
                else -> -1
            }
            if (n2 != -1 && n2 != n) return false
            for (i in 0..<4) {
                if (i == n) continue
                val p2 = p + TheGreyLabyrinthGame.offset[i]
                if (!rng.contains(p2)) continue
                if (!dfs(p2, (i + 2) % 4)) return false
            }
            return true
        }
        if (!(dfs(game.treasure, -1) && moves.size == rng.size)) isSolved = false
    }
}