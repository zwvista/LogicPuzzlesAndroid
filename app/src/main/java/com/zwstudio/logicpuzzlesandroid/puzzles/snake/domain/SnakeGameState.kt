package com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class SnakeGameState(game: SnakeGame) : CellsGameState<SnakeGame, SnakeGameMove, SnakeGameState>(game) {
    var objArray = Array(rows() * cols()) { SnakeObject.Empty }
    var row2state = Array(rows()) { HintState.Normal }
    var col2state = Array(cols()) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: SnakeObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: SnakeObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2snake)
            this[p] = SnakeObject.Snake
        updateIsSolved()
    }

    fun setObject(move: SnakeGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game.pos2snake.contains(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: SnakeGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p) || game.pos2snake.contains(p)) return false
        val o = this[p]
        move.obj = when (o) {
            SnakeObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) SnakeObject.Marker else SnakeObject.Snake
            SnakeObject.Snake -> if (markerOption == MarkerOptions.MarkerLast) SnakeObject.Marker else SnakeObject.Empty
            SnakeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) SnakeObject.Snake else SnakeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Snake

        Summary
        Still lives inside your pocket-sized computer

        Description
        1. Complete the Snake, head to tail, inside the board.
        2. The two tiles given at the start are the head and the tail of the snake
           (it is irrelevant which is which).
        3. Numbers on the border tell you how many tiles the snake occupies in that
           row or column.
        4. The snake can't touch itself, not even diagonally.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols())
                if (this[r, c] == SnakeObject.Forbidden)
                    this[r, c] = SnakeObject.Empty
        // 3. Numbers on the border tell you how many tiles the snake occupies in that row.
        for (r in 0 until rows()) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols())
                if (this[r, c] == SnakeObject.Snake)
                    n1++
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        // 3. Numbers on the border tell you how many tiles the snake occupies in that column.
        for (c in 0 until cols()) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows())
                if (this[r, c] == SnakeObject.Snake)
                    n1++
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val o = this[r, c]
                if ((o == SnakeObject.Empty || o == SnakeObject.Marker) && allowedObjectsOnly &&
                        (row2state[r] != HintState.Normal && game.row2hint[r] != -1 || col2state[c] != HintState.Normal && game.col2hint[c] != -1))
                    this[r, c] = SnakeObject.Forbidden
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (this[p] != SnakeObject.Snake) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (p in pos2node.keys)
            for (os in SnakeGame.offset) {
                val p2 = p.add(os)
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        g.setRootNode(pos2node.values.first())
        val nodeList = g.bfs()
        val n1 = nodeList.size
        val n2 = pos2node.values.size
        if (n1 != n2) isSolved = false
        for (p in pos2node.keys) {
            val rngEmpty = mutableListOf<Position>()
            val rngSnake = mutableListOf<Position>()
            for (os in SnakeGame.offset) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                val o = this[p2]
                if (o == SnakeObject.Empty || o == SnakeObject.Marker)
                    rngEmpty.add(p2)
                else if (o == SnakeObject.Snake)
                    rngSnake.add(p2)
            }
            // 2. The two tiles given at the start are the head and the tail of the snake.
            // 4. The snake can't touch itself, not even diagonally.
            val b = game.pos2snake.contains(p)
            val cnt = rngSnake.size
            if (b && cnt >= 1 || !b && cnt >= 2) {
                for (p2 in rngEmpty)
                    if (allowedObjectsOnly)
                        this[p2] = SnakeObject.Forbidden
                if (b && cnt > 1 || !b && cnt > 2) isSolved = false
            }
        }
    }
}