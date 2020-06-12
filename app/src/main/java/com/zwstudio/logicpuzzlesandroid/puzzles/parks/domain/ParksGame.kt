package com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*

class ParksGame(layout: List<String>, treesInEachArea: Int, gi: GameInterface<ParksGame, ParksGameMove, ParksGameState>, gdi: GameDocumentInterface) : CellsGame<ParksGame, ParksGameMove, ParksGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    var areas = mutableListOf<List<Position>>()
    var pos2area = mutableMapOf<Position, Int>()
    var dots: GridDots
    var treesInEachArea = 1

    init {
        size = Position(layout.size / 2, layout[0].length / 2)
        dots = GridDots(rows() + 1, cols() + 1)
        this.treesInEachArea = treesInEachArea
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
                    if (dots[p.add(offset2[i]), dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p.add(offset[i * 2])]!!)
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
        val state = ParksGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: ParksGameMove, f: (ParksGameState, ParksGameMove) -> Boolean): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state: ParksGameState = cloner.deepClone(state())
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

    fun switchObject(move: ParksGameMove) = changeObject(move, ParksGameState::switchObject)
    fun setObject(move: ParksGameMove) = changeObject(move, ParksGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun pos2State(p: Position) = state().pos2state[p]
}
