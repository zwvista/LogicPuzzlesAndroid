package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class KropkiGame(layout: List<String>, bordered: Boolean, gi: GameInterface<KropkiGame?, KropkiGameMove?, KropkiGameState?>?, gdi: GameDocumentInterface?) : CellsGame<KropkiGame?, KropkiGameMove?, KropkiGameState?>(gi, gdi) {
    var pos2horzHint: MutableMap<Position?, KropkiHint?> = HashMap()
    var pos2vertHint: Map<Position, KropkiHint> = HashMap()
    var areas: MutableList<List<Position>> = ArrayList()
    var pos2area: MutableMap<Position, Int> = HashMap()
    var dots: GridDots? = null
    var bordered: Boolean
    private fun changeObject(move: KropkiGameMove?, f: F2<KropkiGameState?, KropkiGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: KropkiGameMove?): Boolean {
        return changeObject(move, F2 { obj: KropkiGameState?, move: KropkiGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: KropkiGameMove?): Boolean {
        return changeObject(move, F2 { obj: KropkiGameState?, move: KropkiGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Int {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Int {
        return state()!![row, col]
    }

    fun getHorzState(p: Position?): HintState? {
        return state()!!.pos2horzHint[p]
    }

    fun getVertState(p: Position?): HintState? {
        return state()!!.pos2vertHint[p]
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
        size = Position(if (bordered) layout.size / 4 else layout.size / 2 + 1, layout[0].length)
        this.bordered = bordered
        for (r in 0 until rows() * 2 - 1) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r / 2, c)
                val ch = str[c]
                val kh = if (ch == 'W') KropkiHint.Consecutive else if (ch == 'B') KropkiHint.Twice else KropkiHint.None
                (if (r % 2 == 0) pos2horzHint else pos2vertHint).put(p, kh)
            }
        }
        if (bordered) {
            dots = GridDots(rows() + 1, cols() + 1)
            for (r in 0 until rows() + 1) {
                var str = layout[rows() * 2 - 1 + 2 * r]
                for (c in 0 until cols()) {
                    val p = Position(r, c)
                    val ch = str[c * 2 + 1]
                    if (ch == '-') {
                        dots!![r, c, 1] = GridLineObject.Line
                        dots!![r, c + 1, 3] = GridLineObject.Line
                    }
                }
                if (r == rows()) break
                str = layout[rows() * 2 - 1 + 2 * r + 1]
                for (c in 0 until cols() + 1) {
                    val p = Position(r, c)
                    val ch = str[c * 2]
                    if (ch == '|') {
                        dots!![r, c, 2] = GridLineObject.Line
                        dots!![r + 1, c, 0] = GridLineObject.Line
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
                for (i in 0..3) if (dots!![p.add(offset2[i]), dirs[i]] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(offset[i])])
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
        }
        val state = KropkiGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}