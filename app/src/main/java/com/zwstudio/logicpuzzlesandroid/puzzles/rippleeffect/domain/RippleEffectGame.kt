package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGame
import fj.F2
import java.util.*

class RippleEffectGame(layout: List<String>, gi: GameInterface<RippleEffectGame, RippleEffectGameMove, RippleEffectGameState>, gdi: GameDocumentInterface) : CellsGame<RippleEffectGame, RippleEffectGameMove, RippleEffectGameState>(gi, gdi) {
    var areas: MutableList<List<Position>> = ArrayList()
    var pos2area: MutableMap<Position?, Int?> = HashMap()
    var dots: GridDots
    var objArray: IntArray
    operator fun get(row: Int, col: Int): Int {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): Int {
        return get(p!!.row, p.col)
    }

    operator fun set(row: Int, col: Int, obj: Int) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Int) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: RippleEffectGameMove, f: F2<RippleEffectGameState?, RippleEffectGameMove, Boolean>): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
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

    fun switchObject(move: RippleEffectGameMove): Boolean {
        return changeObject(move, F2 { obj: RippleEffectGameState?, move: RippleEffectGameMove -> obj!!.switchObject(move) })
    }

    fun setObject(move: RippleEffectGameMove): Boolean {
        return changeObject(move, F2 { obj: RippleEffectGameState?, move: RippleEffectGameMove -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Int {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Int {
        return state()!![row, col]
    }

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
    }

    init {
        size = Position(layout.size / 2, layout[0].length / 2)
        dots = GridDots(rows() + 1, cols() + 1)
        objArray = IntArray(rows() * cols())
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
                if (c == cols()) break
                val ch2 = str[c * 2 + 1]
                val n = if (ch2 >= '0' && ch2 <= '9') ch2 - '0' else 0
                set(Position(r, c), n)
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
            for (i in 0..3) if (dots[p.add(ParksGame.Companion.offset2.get(i)), ParksGame.Companion.dirs.get(i)] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(ParksGame.Companion.offset.get(i * 2))])
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
        val state = RippleEffectGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}