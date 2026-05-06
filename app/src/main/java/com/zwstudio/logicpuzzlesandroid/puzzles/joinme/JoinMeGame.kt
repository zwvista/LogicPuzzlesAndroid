package com.zwstudio.logicpuzzlesandroid.puzzles.joinme

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class JoinMeGame(layout: List<String>, val stitches: Int, gi: GameInterface<JoinMeGame, JoinMeGameMove, JoinMeGameState>, gdi: GameDocumentInterface) : CellsGame<JoinMeGame, JoinMeGameMove, JoinMeGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 0, 3, 2)
        const val PUZ_UNKNOWN = -1
    }

    val areas = mutableListOf<List<Position>>()
    val pos2area = mutableMapOf<Position, Int>()
    val dots: GridDots
    val row2hint: IntArray
    val col2hint: IntArray
    val area2areas: Array<IntArray>

    init {
        size = Position(layout.size / 2 - 1, layout[0].length / 2 - 1)
        dots = GridDots(rows + 1, cols + 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        for (r in 0 until rows + 1) {
            var str = layout[r * 2]
            for (c in 0 until cols) {
                val ch = str[c * 2 + 1]
                if (ch == '-') {
                    dots[r, c, 1] = GridLineObject.Line
                    dots[r, c + 1, 3] = GridLineObject.Line
                }
            }
            str = layout[r * 2 + 1]
            if (r < rows) {
                for (c in 0 until cols + 1) {
                    val ch = str[c * 2]
                    if (ch == '|') {
                        dots[r, c, 2] = GridLineObject.Line
                        dots[r + 1, c, 0] = GridLineObject.Line
                    }
                }
                val s = str.substring(cols * 2 + 1, cols * 2 + 3).trim()
                row2hint[r] = if (s.isEmpty()) PUZ_UNKNOWN else s.toInt()
            } else {
                for (c in 0 until cols) {
                    val s = str.substring(c * 2, c * 2 + 2).trim()
                    col2hint[c] = if (s.isEmpty()) PUZ_UNKNOWN else s.toInt()
                }
            }
        }
        val rng = mutableSetOf<Position>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                rng.add(+p)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                for (i in 0 until 4)
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
        area2areas = Array(areas.size) { IntArray(0) }
        for ((i, area) in areas.withIndex())
            area2areas[i] = area
                .asSequence()
                .flatMap { p -> offset.map { p + it } }
                .filter { isValid(it) }
                .map { pos2area[it]!! }
                .filter { it != i }
                .toSortedSet().toIntArray()
        val state = JoinMeGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
}
