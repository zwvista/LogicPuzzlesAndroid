package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TurnTwiceGameState(game: TurnTwiceGame) : CellsGameState<TurnTwiceGame, TurnTwiceGameMove, TurnTwiceGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TurnTwiceObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TurnTwiceObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TurnTwiceGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] !is TurnTwiceEmptyObject || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TurnTwiceGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] !is TurnTwiceEmptyObject) return GameOperationType.Invalid
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is TurnTwiceEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) TurnTwiceMarkerObject else TurnTwiceWallObject()
            is TurnTwiceWallObject -> if (markerOption == MarkerOptions.MarkerLast) TurnTwiceMarkerObject else TurnTwiceEmptyObject
            is TurnTwiceMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) TurnTwiceSignPostObject() else TurnTwiceEmptyObject
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
        fun isEmpty(p: Position) = this[p] !is TurnTwiceWallObject
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val walls = mutableListOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (val o = this[p]) {
                    is TurnTwiceForbiddenObject -> this[p] = TurnTwiceEmptyObject
                    is TurnTwiceSignPostObject -> o.state = AllowedObjectState.Normal
                    is TurnTwiceWallObject ->  {
                        o.state = AllowedObjectState.Normal
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
        for ((p, node) in pos2node) {
            for (os in TurnTwiceGame.offset) {
                val p2 = p + os
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
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
                (this[p1] as TurnTwiceSignPostObject).state = AllowedObjectState.Error
                (this[p2] as TurnTwiceSignPostObject).state = AllowedObjectState.Error
            }

        // 4. Walls can't touch horizontally or vertically.
        for (p in walls)
            for (os in TurnTwiceGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) continue
                when (this[p2]) {
                    is TurnTwiceWallObject -> {
                        isSolved = false
                        (this[p] as TurnTwiceWallObject).state = AllowedObjectState.Error
                        (this[p2] as TurnTwiceWallObject).state = AllowedObjectState.Error
                    }
                    is TurnTwiceEmptyObject -> {
                        if (allowedObjectsOnly)
                            this[p2] = TurnTwiceForbiddenObject
                    }
                    else -> {}
                }
            }
    }
}