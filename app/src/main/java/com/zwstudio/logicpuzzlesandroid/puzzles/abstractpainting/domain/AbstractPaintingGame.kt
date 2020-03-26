package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

import fj.F2

import fj.data.List.iterableList

class AbstractPaintingGame(layout: List<String>, gi: GameInterface<AbstractPaintingGame, AbstractPaintingGameMove, AbstractPaintingGameState>, gdi: GameDocumentInterface) : CellsGame<AbstractPaintingGame, AbstractPaintingGameMove, AbstractPaintingGameState>(gi, gdi) {

    companion object {
        val offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 0, 3, 2)
    }

    var areas: MutableList<List<Position>> = ArrayList()
    var pos2area = mutableMapOf<Position, Int>()
    var dots: GridDots
    var row2hint: IntArray
    var col2hint: IntArray

    init {
        size = Position(layout.size / 2 - 1, layout[0].length / 2 - 1)
        dots = GridDots(rows() + 1, cols() + 1)
        row2hint = IntArray(rows())
        col2hint = IntArray(cols())
        for (r in 0 until rows() + 1) {
            var str = layout[r * 2]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c * 2 + 1]
                if (ch == '-') {
                    dots.set(r, c, 1, GridLineObject.Line)
                    dots.set(r, c + 1, 3, GridLineObject.Line)
                }
            }
            if (r == rows()) break
            str = layout[r * 2 + 1]
            for (c in 0 until cols() + 1) {
                val p = Position(r, c)
                val ch = str[c * 2]
                if (ch == '|') {
                    dots.set(r, c, 2, GridLineObject.Line)
                    dots.set(r + 1, c, 0, GridLineObject.Line)
                }
            }
        }
        val rng = HashSet<Position>()
        val g = Graph()
        val pos2node = HashMap<Position, Node>()
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
                    if (dots.get(p.add(AbstractPaintingGame.offset2[i]), AbstractPaintingGame.dirs[i]) != GridLineObject.Line)
                        g.connectNode(pos2node[p], pos2node[p.add(AbstractPaintingGame.offset[i])])
            }
        while (!rng.isEmpty()) {
            g.setRootNode(pos2node[iterableList(rng).head()])
            val nodeList = g.bfs()
            val area = iterableList(rng).filter { p -> nodeList.contains(pos2node[p]) }.toJavaList()
            val n = areas.size
            for (p in area)
                pos2area[p] = n
            areas.add(area)
            rng.removeAll(area)
        }
        for (r in 0 until rows()) {
            val ch = layout[2 * r + 1][2 * cols() + 1]
            row2hint[r] = if (ch >= '0' && ch <= '9') ch - '0' else -1
        }
        for (c in 0 until cols()) {
            val ch = layout[2 * rows() + 1][2 * c + 1]
            col2hint[c] = if (ch >= '0' && ch <= '9') ch - '0' else -1
        }
        val state = AbstractPaintingGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: AbstractPaintingGameMove, f: (AbstractPaintingGameState, AbstractPaintingGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: AbstractPaintingGameMove) = changeObject(move, AbstractPaintingGameState::switchObject)
    fun setObject(move: AbstractPaintingGameMove) = changeObject(move, AbstractPaintingGameState::setObject)
    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun getRowState(row: Int) = state().row2state[row]
    fun getColState(col: Int) = state().col2state[col]
}
