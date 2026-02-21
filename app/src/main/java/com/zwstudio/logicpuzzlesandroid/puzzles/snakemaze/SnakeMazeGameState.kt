package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.yalooniq.YalooniqGame

class SnakeMazeGameState(game: SnakeMazeGame) : CellsGameState<SnakeMazeGame, SnakeMazeGameMove, SnakeMazeGameState>(game) {
    private var objArray = Array(rows * cols) { SnakeMazeObject.Normal }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: SnakeMazeObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: SnakeMazeObject) {this[p.row, p.col] = obj}

    override fun setObject(move: SnakeMazeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SnakeMazeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            SnakeMazeObject.Normal -> if (markerOption == MarkerOptions.MarkerFirst) SnakeMazeObject.Marker else SnakeMazeObject.Shaded
            SnakeMazeObject.Shaded -> if (markerOption == MarkerOptions.MarkerLast) SnakeMazeObject.Marker else SnakeMazeObject.Normal
            SnakeMazeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) SnakeMazeObject.Shaded else SnakeMazeObject.Normal
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
        isSolved = true
        // 3. You can shade tiles with arrows and numbers.
        // 5. A cell containing a number and an arrow tells you how many tiles are shaded
        //    in that direction.
        // 6. However not all tiles that are shaded tell you lies.
        for ((p, hint) in game.pos2hint) {
            if (this[p].isShaded) {
                pos2stateHint[p] = HintState.Complete
                continue
            }
            val n2 = hint.num
            val os = SnakeMazeGame.offset[hint.dir]
            var n1 = 0
            var p2 = p + os
            while (isValid(p2)) {
                if (this[p2].isShaded) n1++
                p2 += os
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        // 2. Shaded tiles must not be orthogonally connected.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (!this[p].isShaded) continue
                val s = if (!YalooniqGame.offset.any {
                    val p2 = p + it
                    isValid(p2) && this[p2].isShaded
                }) AllowedObjectState.Normal else AllowedObjectState.Error
                pos2stateAllowed[p] = s
                if (s == AllowedObjectState.Error) isSolved = false
            }
        if (!isSolved) return
        // 4. All tiles which are not shaded must form an orthogonally continuous area.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngDarken = mutableListOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (!this[p].isShaded) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for (p in pos2node.keys)
            for (os in SnakeMazeGame.offset) {
                val p2 = p + os
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
