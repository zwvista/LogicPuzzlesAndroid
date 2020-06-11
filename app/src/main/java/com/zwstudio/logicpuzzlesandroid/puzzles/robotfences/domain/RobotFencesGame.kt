package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain

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
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    var areas = mutableListOf<List<Position>>()
    var pos2area = mutableMapOf<Position, Int>()
    var dots: GridDots

    init {
        size = Position(layout.size / 2, layout[0].length / 2)
        objArray = IntArray(rows() * cols())
        dots = GridDots(rows() + 1, cols() + 1)
        for (r in 0 until rows() + 1) {
            var str = layout[r * 2]
            for (c in 0 until cols()) {
                val ch = str[c * 2 + 1]
                if (ch == '-') {
                    dots[r, c, 1] = GridLineObject.Line
                    dots[r, c + 1, 3] = GridLineObject.Line
                }
            }
            if (r == rows()) break
            str = layout[r * 2 + 1]
            for (c in 0 until cols() + 1) {
                val ch = str[c * 2]
                if (ch == '|') {
                    dots[r, c, 2] = GridLineObject.Line
                    dots[r + 1, c, 0] = GridLineObject.Line
                }
                if (c == cols()) break
                val ch2 = str[c * 2 + 1]
                set(r, c, if (ch2 == ' ') 0 else ch2 - '0')
            }
        }
        val rng = mutableSetOf<Position>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                rng.add(p.plus())
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                for (i in 0..3)
                    if (dots.get(p.add(offset2[i]), dirs[i]) != GridLineObject.Line)
                        g.connectNode(pos2node[p], pos2node[p.add(offset[i])])
            }
        while (rng.isNotEmpty()) {
            g.setRootNode(pos2node[rng.first()])
            val nodeList = g.bfs()
            val area = rng.filter { nodeList.contains(pos2node[it]) }
            val n = areas.size
            for (p in area)
                pos2area[p] = n
            areas.add(area)
            rng.removeAll(area)
        }
        val state = RobotFencesGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: RobotFencesGameMove, f: (RobotFencesGameState, RobotFencesGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state: RobotFencesGameState = cloner.deepClone(state())
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: RobotFencesGameMove) = changeObject(move, RobotFencesGameState::switchObject)
    fun setObject(move: RobotFencesGameMove) = changeObject(move, RobotFencesGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun getRowState(row: Int) = state().row2state[row]
    fun getColState(col: Int) = state().col2state[col]
    fun getPosState(p: Position) = state().area2state[pos2area[p]!!]
}
