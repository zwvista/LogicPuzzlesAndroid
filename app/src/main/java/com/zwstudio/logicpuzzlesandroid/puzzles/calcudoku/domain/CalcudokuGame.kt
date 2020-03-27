package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import fj.P2
import java.util.*

class CalcudokuGame(layout: List<String>, gi: GameInterface<CalcudokuGame, CalcudokuGameMove, CalcudokuGameState>, gdi: GameDocumentInterface) : CellsGame<CalcudokuGame, CalcudokuGameMove, CalcudokuGameState>(gi, gdi) {
    var areas: MutableList<List<Position>> = ArrayList()
    var pos2area: MutableMap<Position?, Int?> = HashMap()
    var dots: GridDots
    var objArray: CharArray
    var pos2hint: MutableMap<Position, CalcudokuHint> = HashMap()
    operator fun get(row: Int, col: Int): Char {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position): Char {
        return get(p.row, p.col)
    }

    operator fun set(row: Int, col: Int, obj: Char) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Char) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: CalcudokuGameMove?, f: F2<CalcudokuGameState?, CalcudokuGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: CalcudokuGameMove?): Boolean {
        return changeObject(move, F2 { obj: CalcudokuGameState?, move: CalcudokuGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: CalcudokuGameMove?): Boolean {
        return changeObject(move, F2 { obj: CalcudokuGameState?, move: CalcudokuGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Int {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Int {
        return state()!![row, col]
    }

    fun getRowState(row: Int): HintState? {
        return state()!!.row2state[row]
    }

    fun getColState(col: Int): HintState? {
        return state()!!.col2state[col]
    }

    fun getPosState(p: Position?): HintState? {
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
        size = Position(layout.size, layout[0].length / 4)
        dots = GridDots(rows() + 1, cols() + 1)
        objArray = CharArray(rows() * cols())
        val g = Graph()
        val pos2node: MutableMap<Position, Node> = HashMap()
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch1 = str[c * 4]
                val s = str.substring(c * 4 + 1, c * 4 + 3)
                val ch2 = str[c * 4 + 3]
                set(p, ch1)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (s == "  ") continue
                pos2hint[p] = object : CalcudokuHint() {
                    init {
                        op = ch2
                        result = if (s == "  ") 0 else s.trim { it <= ' ' }.toInt()
                    }
                }
            }
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val ch = get(p)
            if (ch == ' ') continue
            for (os in offset) {
                val p2 = p.add(os)
                if (isValid(p2) && get(p2) == ch) g.connectNode(pos2node[p], pos2node[p2])
            }
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(fj.data.List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            val n = areas.size
            val ch = get(area[0])
            for (p in area) {
                pos2area[p] = n
                pos2node.remove(p)
                for (i in 0..3) {
                    val p2 = p.add(offset[i])
                    val ch2 = if (!isValid(p2)) '.' else get(p2)
                    if (ch2 != ch) dots[p.add(offset2[i]), dirs[i]] = GridLineObject.Line
                }
            }
            areas.add(area)
        }
        for (r in 0 until rows()) {
            dots[r, 0, 2] = GridLineObject.Line
            dots[r + 1, 0, 0] = GridLineObject.Line
            dots[r, cols(), 2] = GridLineObject.Line
            dots[r + 1, cols(), 0] = GridLineObject.Line
        }
        for (c in 0 until cols()) {
            dots[0, c, 1] = GridLineObject.Line
            dots[0, c + 1, 3] = GridLineObject.Line
            dots[rows(), c, 1] = GridLineObject.Line
            dots[rows(), c + 1, 3] = GridLineObject.Line
        }
        val state = CalcudokuGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}