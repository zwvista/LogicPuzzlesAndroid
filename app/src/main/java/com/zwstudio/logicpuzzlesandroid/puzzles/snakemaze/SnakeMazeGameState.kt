package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeMazeGameState(game: SnakeMazeGame) : CellsGameState<SnakeMazeGame, SnakeMazeGameMove, SnakeMazeGameState>(game) {
    private var objArray = Array(rows * cols) { SnakeMazeObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()
    var snakes = mutableListOf<List<Position>>()

    init {
        for (p in game.pos2hint.keys)
            this[p] = SnakeMazeObject.Hint
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: SnakeMazeObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: SnakeMazeObject) {this[p.row, p.col] = obj}

    override fun setObject(move: SnakeMazeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint.containsKey(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SnakeMazeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint.containsKey(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            SnakeMazeObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) SnakeMazeObject.Marker else SnakeMazeObject.Snake1
            SnakeMazeObject.Snake1 -> SnakeMazeObject.Snake2
            SnakeMazeObject.Snake2 -> SnakeMazeObject.Snake3
            SnakeMazeObject.Snake3 -> SnakeMazeObject.Snake4
            SnakeMazeObject.Snake4 -> SnakeMazeObject.Snake5
            SnakeMazeObject.Snake5 -> if (markerOption == MarkerOptions.MarkerLast) SnakeMazeObject.Marker else SnakeMazeObject.Empty
            SnakeMazeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) SnakeMazeObject.Snake1 else SnakeMazeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 7/Snake Maze

        Summary
        Find the snakes using the given hints.

        Description
        1. A Snake is a path of five tiles, numbered 1-2-3-4-5, where 1 is the head and 5 the tail.
           The snake's body segments are connected horizontally or vertically.
        2. A snake cannot see another snake or it would attack it. A snake sees straight in the
           direction 2-1, that is to say it sees in front of the number 1.
        3. A snake cannot touch another snake horizontally or vertically.
        4. Arrows show you the closest piece of Snake in that direction (before another arrow or the edge).
        5. Arrows with zero mean that there is no Snake in that direction.
        6. Arrows block snake sight and also block other arrows hints.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        snakes.clear()
        val pos2snake = mutableMapOf<Position, Int>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] == SnakeMazeObject.Forbidden)
                    this[p] = SnakeMazeObject.Empty
                else if (this[p].isSnake) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in SnakeMazeGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.keys.toList()
            for (p in area)
                pos2node.remove(p)
            // 1. A Snake is a path of five tiles, numbered 1-2-3-4-5, where 1 is the head and 5 the tail.
            //    The snake's body segments are connected horizontally or vertically.
            // 3. A snake cannot touch another snake horizontally or vertically.
            if (!(area.size == 5 && (1..5).all { n ->
                area.any { this[it].value == n }
            })) {
                for (p in area)
                    pos2stateAllowed[p] = AllowedObjectState.Error
                isSolved = false; continue
            }
            val snake = (1..5).map { n ->
                area.first { this[it].value == n }
            }
            if (!(0..<4).all { i ->
                val os = snake[i] - snake[i + 1]
                SnakeMazeGame.offset.contains(os)
            }) {
                for (p in area)
                    pos2stateAllowed[p] = AllowedObjectState.Error
                isSolved = false; continue
            }
            for (p in area)
                pos2stateAllowed[p] = AllowedObjectState.Normal
            val n = snakes.size
            snakes.add(snake)
            for (p in snake)
                pos2snake[p] = n
        }
        // 2. A snake cannot see another snake or it would attack it. A snake sees straight in the
        //    direction 2-1, that is to say it sees in front of the number 1.
        for (snake in snakes) {
            val os = snake[0] - snake[1]
            var p2 = snake[0] + os
            while (isValid(p2)) {
                if (this[p2] == SnakeMazeObject.Empty && allowedObjectsOnly)
                    this[p2] = SnakeMazeObject.Forbidden
                else if (this[p2] == SnakeMazeObject.Hint)
                    break
                else if (this[p2].isSnake) {
                    val n = pos2snake[p2] ?: break
                    for (p in snakes[n])
                        pos2stateAllowed[p] = AllowedObjectState.Error
                    break
                }
                p2 += os
            }
        }
        // 4. Arrows show you the closest piece of Snake in that direction (before another arrow or the edge).
        // 5. Arrows with zero mean that there is no Snake in that direction.
        // 6. Arrows block snake sight and also block other arrows hints.
        for ((p, hint) in game.pos2hint) {
            val n2 = hint.num
            val os = SnakeMazeGame.offset[hint.dir]
            var n1 = 0
            var p2 = p + os
            while (isValid(p2)) {
                if (this[p2] == SnakeMazeObject.Empty && n2 == 0 && allowedObjectsOnly)
                    this[p2] = SnakeMazeObject.Forbidden
                else if (this[p2] == SnakeMazeObject.Hint)
                    break
                else if (this[p2].isSnake) {
                    n1 = this[p2].value
                    break
                }
                p2 += os
            }
            val s = if (n1 == n2) HintState.Complete else if (n1 == 0) HintState.Normal else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}
