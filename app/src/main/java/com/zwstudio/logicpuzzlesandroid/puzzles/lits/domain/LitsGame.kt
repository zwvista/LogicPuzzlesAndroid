package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class LitsGame(layout: List<String>, gi: GameInterface<LitsGame, LitsGameMove, LitsGameState>, gdi: GameDocumentInterface) : CellsGame<LitsGame, LitsGameMove, LitsGameState>(gi, gdi) {
    var areas: MutableList<List<Position>> = ArrayList()
    var pos2area: MutableMap<Position?, Int?> = HashMap()
    var dots: GridDots
    var treesInEachArea = 1
    private fun changeObject(move: LitsGameMove, f: (LitsGameState, LitsGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
        val changed = f.f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: LitsGameMove) = changeObject(move, LitsGameState::switchObject)
    fun setObject(move: LitsGameMove) = changeObject(move, LitsGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int)  = state()[row, col]
    fun pos2State(p: Position?): HintState? {
        return state()!!.pos2state[p]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0))
        var dirs = intArrayOf(1, 0, 3, 2)
        var offset3 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1))
        var tetrominoes = arrayOf(arrayOf(arrayOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1)), arrayOf(Position(0, 1), Position(1, 1), Position(2, 0), Position(2, 1)), arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 0)), arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 2)), arrayOf(Position(0, 0), Position(0, 1), Position(1, 0), Position(2, 0)), arrayOf(Position(0, 0), Position(0, 1), Position(1, 1), Position(2, 1)), arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(1, 2)), arrayOf(Position(0, 2), Position(1, 0), Position(1, 1), Position(1, 2))), arrayOf(arrayOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)), arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3))), arrayOf(arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 1)), arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(2, 1)), arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(1, 2)), arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 0))), arrayOf(arrayOf(Position(0, 0), Position(0, 1), Position(1, 1), Position(1, 2)), arrayOf(Position(0, 1), Position(0, 2), Position(1, 0), Position(1, 1)), arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 1)), arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(2, 0))))
    }

    init {
        size = Position(layout.size / 2, layout[0].length / 2)
        dots = GridDots(rows() + 1, cols() + 1)
        for (r in 0 until rows() + 1) {
            var str = layout[r * 2]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c * 2 + 1]
                if (ch == '-') {
                    dots[r, c, 1] = GridLineObject.Line
                    dots[r, c + 1, 3] = GridLineObject.Line
                }
            }
            if (r == rows()) break
            str = layout[r * 2 + 1]
            for (c in 0 until cols() + 1) {
                val p = Position(r, c)
                val ch = str[c * 2]
                if (ch == '|') {
                    dots[r, c, 2] = GridLineObject.Line
                    dots[r + 1, c, 0] = GridLineObject.Line
                }
            }
        }
        val rng: MutableSet<Position> = HashSet()
        val g = Graph()
        val pos2node: MutableMap<Position, Node> = HashMap()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            rng.add(p.plus())
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            for (i in 0..3) if (dots[p.add(offset2[i]), dirs[i]] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(offset[i])])
        }
        while (!rng.isEmpty()) {
            g.setRootNode(pos2node[fj.data.List.iterableList(rng).head()])
            val nodeList = g.bfs()
            val area = fj.data.List.iterableList(rng).filter { p: Position -> nodeList.contains(pos2node[p]) }.toJavaList()
            val n = areas.size
            for (p in area) pos2area[p] = n
            areas.add(area)
            rng.removeAll(area)
        }
        val state = LitsGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}