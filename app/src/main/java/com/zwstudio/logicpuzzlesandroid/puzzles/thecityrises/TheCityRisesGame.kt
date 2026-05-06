package com.zwstudio.logicpuzzlesandroid.puzzles.thecityrises

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheCityRisesGame(layout: List<String>, gi: GameInterface<TheCityRisesGame, TheCityRisesGameMove, TheCityRisesGameState>, gdi: GameDocumentInterface) : CellsGame<TheCityRisesGame, TheCityRisesGameMove, TheCityRisesGameState>(gi, gdi) {
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
    val pos2hint = mutableMapOf<Position, Int>()
    val area2areas: Array<IntArray>
    val area2hint: Array<Position?>

    init {
        size = Position(layout.size / 2, layout[0].length / 2)
        dots = GridDots(rows + 1, cols + 1)
        for (r in 0..<rows + 1) {
            var str = layout[r * 2]
            for (c in 0..<cols) {
                val ch = str[c * 2 + 1]
                if (ch == '-') {
                    dots[r, c, 1] = GridLineObject.Line
                    dots[r, c + 1, 3] = GridLineObject.Line
                }
            }
            if (r == rows) break
            str = layout[r * 2 + 1]
            for (c in 0..<cols + 1) {
                val ch = str[c * 2]
                if (ch == '|') {
                    dots[r, c, 2] = GridLineObject.Line
                    dots[r + 1, c, 0] = GridLineObject.Line
                }
                if (c == cols) break
                val ch2 = str[c * 2 + 1]
                if (ch2 != ' ')
                    pos2hint[Position(r, c)] = if (ch2.isDigit()) ch2 - '0' else ch2 - 'A' + 10
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

        area2areas = Array(areas.size) { IntArray(0) }
        area2hint = Array(areas.size) { null }
        for ((i, area) in areas.withIndex()) {
            area2areas[i] = area
                .asSequence()
                .flatMap { p -> offset.map { p + it } }
                .filter { isValid(it) }
                .map { pos2area[it]!! }
                .filter { it != i }
                .toSortedSet().toIntArray()
            area2hint[i] = area.firstOrNull { pos2hint[it] != null }
        }

        val state = TheCityRisesGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2StateHint(p: Position) = currentState.pos2stateHint[p]
    fun pos2StateAllowed(p: Position) = currentState.pos2stateAllowed[p]
}
