package com.zwstudio.logicpuzzlesandroid.puzzles.snake.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.List
import java.util.*

class SnakeGameState(game: SnakeGame) : CellsGameState<SnakeGame, SnakeGameMove, SnakeGameState>(game) {
    var objArray: Array<SnakeObject?>
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: SnakeObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: SnakeObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: SnakeGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.pos2snake.contains(p) || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: SnakeGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<SnakeObject, SnakeObject> = label@ F<SnakeObject, SnakeObject> { obj: SnakeObject? ->
            when (obj) {
                SnakeObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) SnakeObject.Marker else SnakeObject.Snake
                SnakeObject.Snake -> return@label if (markerOption == MarkerOptions.MarkerLast) SnakeObject.Marker else SnakeObject.Empty
                SnakeObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) SnakeObject.Snake else SnakeObject.Empty
            }
            obj
        }
        val p: Position = move.p
        if (!isValid(p) || game.pos2snake.contains(p)) return false
        move.obj = f.f(get(p))
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
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) if (get(r, c) == SnakeObject.Forbidden) set(r, c, SnakeObject.Empty)
        // 3. Numbers on the border tell you how many tiles the snake occupies in that row.
        for (r in 0 until rows()) {
            var n1 = 0
            val n2: Int = game.row2hint.get(r)
            for (c in 0 until cols()) if (get(r, c) == SnakeObject.Snake) n1++
            val s: HintState = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        // 3. Numbers on the border tell you how many tiles the snake occupies in that column.
        for (c in 0 until cols()) {
            var n1 = 0
            val n2: Int = game.col2hint.get(c)
            for (r in 0 until rows()) if (get(r, c) == SnakeObject.Snake) n1++
            val s: HintState = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o: SnakeObject? = get(r, c)
            if ((o == SnakeObject.Empty || o == SnakeObject.Marker) && allowedObjectsOnly && (row2state[r] != HintState.Normal && game.row2hint.get(r) != -1 ||
                    col2state[c] != HintState.Normal && game.col2hint.get(c) != -1)) set(r, c, SnakeObject.Forbidden)
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            if (get(p) != SnakeObject.Snake) continue
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
        }
        for (p in pos2node.keys) for (os in SnakeGame.Companion.offset) {
            val p2 = p.add(os)
            if (pos2node.containsKey(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        val n1 = nodeList.size
        val n2 = pos2node.values.size
        if (n1 != n2) isSolved = false
        for (p in pos2node.keys) {
            val rngEmpty = mutableListOf<Position>()
            val rngSnake = mutableListOf<Position>()
            for (os in SnakeGame.Companion.offset) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                val o: SnakeObject? = get(p2)
                if (o == SnakeObject.Empty || o == SnakeObject.Marker) rngEmpty.add(p2) else if (o == SnakeObject.Snake) rngSnake.add(p2)
            }
            // 2. The two tiles given at the start are the head and the tail of the snake.
            // 4. The snake can't touch itself, not even diagonally.
            val b: Boolean = game.pos2snake.contains(p)
            val cnt = rngSnake.size
            if (b && cnt >= 1 || !b && cnt >= 2) {
                for (p2 in rngEmpty) if (allowedObjectsOnly) set(p2, SnakeObject.Forbidden)
                if (b && cnt > 1 || !b && cnt > 2) isSolved = false
            }
        }
    }

    init {
        objArray = arrayOfNulls<SnakeObject>(rows() * cols())
        Arrays.fill(objArray, SnakeObject.Empty)
        for (p in game.pos2snake) set(p, SnakeObject.Snake)
        row2state = arrayOfNulls<HintState>(rows())
        col2state = arrayOfNulls<HintState>(cols())
        updateIsSolved()
    }
}