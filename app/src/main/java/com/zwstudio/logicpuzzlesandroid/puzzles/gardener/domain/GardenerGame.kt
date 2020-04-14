package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import fj.P
import fj.P2
import java.util.*

class GardenerGame(layout: List<String>, gi: GameInterface<GardenerGame, GardenerGameMove, GardenerGameState>, gdi: GameDocumentInterface) : CellsGame<GardenerGame, GardenerGameMove, GardenerGameState>(gi, gdi) {
    var areas: MutableList<List<Position>> = ArrayList()
    var pos2area: MutableMap<Position?, Int?> = HashMap()
    var dots: GridDots
    var pos2hint: MutableMap<Position, P2<Int, Int?>> = HashMap()
    private fun changeObject(move: GardenerGameMove, f: (GardenerGameState, GardenerGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: GardenerGameMove) = changeObject(move, GardenerGameState::switchObject)
    fun setObject(move: GardenerGameMove) = changeObject(move, GardenerGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int)  = state()[row, col]
    fun pos2State(p: Position?): HintState? {
        return state()!!.pos2state[p]
    }

    fun invalidSpaces(p: Position?, isHorz: Boolean): Boolean {
        return (if (isHorz) state()!!.invalidSpacesHorz else state()!!.invalidSpacesVert).contains(p)
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
                var ch = str[c * 2]
                if (ch == '|') {
                    dots[r, c, 2] = GridLineObject.Line
                    dots[r + 1, c, 0] = GridLineObject.Line
                }
                if (c == cols()) break
                ch = str[c * 2 + 1]
                if (ch != ' ') pos2hint[Position(r, c)] = P.p(ch - '0', -1)
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
        for ((p, value) in pos2hint) {
            val n = value._1()
            pos2hint[p] = P.p(n, pos2area[p])
        }
        val state = GardenerGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}