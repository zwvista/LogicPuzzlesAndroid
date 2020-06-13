package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*

class RobotFencesGame(layout: List<String>, gi: GameInterface<RobotFencesGame, RobotFencesGameMove, RobotFencesGameState>, gdi: GameDocumentInterface) : CellsGame<RobotFencesGame, RobotFencesGameMove, RobotFencesGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    var objArray: IntArray
    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    var areas = mutableListOf<List<Position>>()
    var pos2area = mutableMapOf<Position, Int>()
    var dots: GridDots

    init {
        size = Position(layout.size / 2, layout[0].length / 2)
        objArray = IntArray(rows * cols)
        dots = GridDots(rows + 1, cols + 1)
        for (r in 0 until rows + 1) {
            var str = layout[r * 2]
            for (c in 0 until cols) {
                val ch = str[c * 2 + 1]
                if (ch == '-') {
                    dots[r, c, 1] = GridLineObject.Line
                    dots[r, c + 1, 3] = GridLineObject.Line
                }
            }
            if (r == rows) break
            str = layout[r * 2 + 1]
            for (c in 0 until cols + 1) {
                val ch = str[c * 2]
                if (ch == '|') {
                    dots[r, c, 2] = GridLineObject.Line
                    dots[r + 1, c, 0] = GridLineObject.Line
                }
                if (c == cols) break
                val ch2 = str[c * 2 + 1]
                set(r, c, if (ch2 == ' ') 0 else ch2 - '0')
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
            for (p in area)
                pos2area[p] = n
            areas.add(area)
            rng.removeAll(area)
        }
        val state = RobotFencesGameState(this)
        levelInitilized(state)
    }

    fun switchObject(move: RobotFencesGameMove) = changeObject(move, RobotFencesGameState::switchObject)
    fun setObject(move: RobotFencesGameMove) = changeObject(move, RobotFencesGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
    fun getPosState(p: Position) = currentState.area2state[pos2area[p]!!]
}
