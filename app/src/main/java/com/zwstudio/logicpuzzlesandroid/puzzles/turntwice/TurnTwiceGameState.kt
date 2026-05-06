package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TurnTwiceGameState(game: TurnTwiceGame) : CellsGameState<TurnTwiceGame, TurnTwiceGameMove, TurnTwiceGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TurnTwiceObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TurnTwiceObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TurnTwiceGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != TurnTwiceObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TurnTwiceGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != TurnTwiceObject.Empty) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            TurnTwiceObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) TurnTwiceObject.Marker else TurnTwiceObject.Wall
            TurnTwiceObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) TurnTwiceObject.Marker else TurnTwiceObject.Empty
            TurnTwiceObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) TurnTwiceObject.SignPost else TurnTwiceObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 15/Turn Twice

        Summary
        Think and Turn Twice (or more)

        Description
        1. In an effort to complicate signposts, you're given the task to have
           signposts reach other by no less than two turns.
        2. In other words, you have to place walls on the board so that a maze of
           signposts is formed. In this maze:
        3. In order to go from one signpost to the other, you have to turn at least
           twice.
        4. Walls can't touch horizontally or vertically.
        5. All the signposts and empty spaces must form an orthogonally continuous
           area.
    */
    private fun updateIsSolved() {
        fun isEmpty(p: Position) = this[p] != TurnTwiceObject.Wall
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val walls = mutableListOf<Position>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (this[p]) {
                    TurnTwiceObject.Forbidden -> this[p] = TurnTwiceObject.Empty
                    TurnTwiceObject.SignPost -> pos2state[p] = AllowedObjectState.Normal
                    TurnTwiceObject.Wall ->  {
                        pos2state[p] = AllowedObjectState.Normal
                        walls.add(p)
                    }
                    else -> {}
                }
                if (isEmpty(p)) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in TurnTwiceGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        // 5. All the signposts and empty spaces must form an orthogonally continuous
        // area.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false

        // 3. In order to go from one signpost to the other, you have to turn at least
        // twice.
        for ((p1, p2, path) in game.paths)
            if (path.all { isEmpty(it) }) {
                isSolved = false
                pos2state[p1] = AllowedObjectState.Error
                pos2state[p2] = AllowedObjectState.Error
            }

        // 4. Walls can't touch horizontally or vertically.
        for (p in walls)
            for (os in TurnTwiceGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) continue
                when (this[p2]) {
                    TurnTwiceObject.Wall -> {
                        isSolved = false
                        pos2state[p] = AllowedObjectState.Error
                        pos2state[p2] = AllowedObjectState.Error
                    }
                    TurnTwiceObject.Empty -> {
                        if (allowedObjectsOnly)
                            this[p2] = TurnTwiceObject.Forbidden
                    }
                    else -> {}
                }
            }
    }
}