package com.zwstudio.logicpuzzlesandroid.puzzles.mirrorsextended

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MirrorsExtendedGame(layout: List<String>, gi: GameInterface<MirrorsExtendedGame, MirrorsExtendedGameMove, MirrorsExtendedGameState>, gdi: GameDocumentInterface) : CellsGame<MirrorsExtendedGame, MirrorsExtendedGameMove, MirrorsExtendedGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 0, 3, 2)
        val offset3 = Position.Square2x2Offset
        const val PUZ_UNKNOWN = -1
    }

    override fun isValid(row: Int, col: Int) = row in 1..<size.row - 1 && col in 1..<size.col - 1

    val areas = mutableListOf<List<Position>>()
    val pos2area = mutableMapOf<Position, Int>()
    val dots: GridDots
    val letter2laser = mutableMapOf<Char, MirrorsExtendedLaser>()

    init {
        size = Position(layout.size / 2 + 1, layout[0].length / 2)
        dots = GridDots(rows + 1, cols + 1)
        for (r in 1..<rows) {
            var str = layout[r * 2 - 1]
            for (c in 1..<cols - 1) {
                val ch = str[c * 2 + 1]
                if (ch == '-') {
                    dots[r, c, 1] = GridLineObject.Line
                    dots[r, c + 1, 3] = GridLineObject.Line
                }
            }
            if (r == rows) break
            str = layout[r * 2]
            for (c in 1..<cols) {
                val ch = str[c * 2]
                if (ch == '|') {
                    dots[r, c, 2] = GridLineObject.Line
                    dots[r + 1, c, 0] = GridLineObject.Line
                }
            }
        }
        val rng = mutableSetOf<Position>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 1..<rows - 1)
            for (c in 1..<cols - 1) {
                val p = Position(r, c)
                rng.add(+p)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 1..<rows - 1)
            for (c in 1..<cols - 1) {
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

        fun f(r: Int, c: Int, d: Int) {
            val p = Position(r, c)
            val str = layout[2 * r]
            val c2 = 2 * c + (if (c == cols - 1) 1 else 0)
            val (ch1, ch2) = str[c2] to str[c2 + 1]
            if (ch1 == ' ') return
            val n = if (ch2.isDigit()) ch2 - '0' else ch2 - 'A' + 10
            letter2laser.getOrPut(ch1) { MirrorsExtendedLaser(n) }.dots.add(MirrorsExtendedLaserDot(p, d))
        }
        for (i in 0..<rows) {
            f(0, i, 2)
            f(rows - 1, i, 0)
            f(i, 0, 1)
            f(i, cols - 1, 3)
        }

        val state = MirrorsExtendedGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
