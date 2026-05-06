package com.zwstudio.logicpuzzlesandroid.puzzles.funnynumbers

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FunnyNumbersGame(layout: List<String>, gi: GameInterface<FunnyNumbersGame, FunnyNumbersGameMove, FunnyNumbersGameState>, gdi: GameDocumentInterface) : CellsGame<FunnyNumbersGame, FunnyNumbersGameMove, FunnyNumbersGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 0, 3, 2)
    }

    val areas = mutableListOf<List<Position>>()
    val pos2area = mutableMapOf<Position, Int>()
    val dots: GridDots
    val row2hint: IntArray
    val col2hint: IntArray
    val objArray: IntArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size / 2 - 1, layout[0].length / 2 - 1)
        dots = GridDots(rows + 1, cols + 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        objArray = IntArray(rows * cols)
        for (r in 0..<rows + 1) {
            var str = layout[r * 2]
            for (c in 0..<cols) {
                val ch = str[c * 2 + 1]
                if (ch == '-') {
                    dots[r, c, 1] = GridLineObject.Line
                    dots[r, c + 1, 3] = GridLineObject.Line
                }
            }
            str = layout[r * 2 + 1]
            if (r < rows) {
                for (c in 0..<cols + 1) {
                    val ch = str[c * 2]
                    if (ch == '|') {
                        dots[r, c, 2] = GridLineObject.Line
                        dots[r + 1, c, 0] = GridLineObject.Line
                    }
                    if (c == cols) continue
                    val ch2 = str[c * 2 + 1]
                    if (ch2.isDigit())
                        this[r, c] = ch2 - '0'
                }
                val s = str.substring(cols * 2 + 1, cols * 2 + 3).trim()
                row2hint[r] = if (s.isEmpty()) 0 else s.toInt()
            } else {
                for (c in 0..<cols) {
                    val s = str.substring(c * 2, c * 2 + 2).trim()
                    col2hint[c] = if (s.isEmpty()) 0 else s.toInt()
                }
            }
        }
        val rng = mutableSetOf<Position>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                rng.add(+p)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                for (i in 0..<4)
                    if (dots[p + offset2[i], dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + offset[i]]!!)
            }
        while (rng.isNotEmpty()) {
            g.rootNode = pos2node[rng.first()]!!
            val nodeList = g.bfs()
            val area = rng.filter { nodeList.contains(pos2node[it]) }
            val n = areas.size
            for (p in area) pos2area[p] = n
            areas.add(area)
            rng.removeAll(area)
        }
        val state = FunnyNumbersGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
