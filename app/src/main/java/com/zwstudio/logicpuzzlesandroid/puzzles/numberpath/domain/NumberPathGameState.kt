package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.data.List
import java.util.*

class NumberPathGameState(game: NumberPathGame?) : CellsGameState<NumberPathGame?, NumberPathGameMove?, NumberPathGameState?>(game) {
    var objArray: Array<Array<Boolean?>>
    var pos2state: Map<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Array<Boolean?>) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Array<Boolean?>) {
        set(p.row, p.col, obj)
    }

    fun setObject(move: NumberPathGameMove): Boolean {
        val p: Position = move.p
        val dir: Int = move.dir
        val p2 = p.add(NumberPathGame.Companion.offset.get(dir))
        val dir2 = (dir + 2) % 4
        if (!isValid(p2)) return false
        get(p)[dir] = !get(p)[dir]!!
        get(p2)[dir2] = !get(p2)[dir2]!!
        updateIsSolved()
        return true
    }

    /*
        iOS Game: Logic Games/Puzzle Set 15/Number Path

        Summary
        Tangled, Scrambled Path

        Description
        1. Connect the top left corner (1) to the bottom right corner (N), including
           all the numbers between 1 and N, only once.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val pStart = Position(0, 0)
        val pEnd = Position(rows() - 1, cols() - 1)
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val n = fj.data.Array.array(*get(p)).filter(F<Boolean, Boolean> { o: Boolean? -> o }).length()
            if (p == pStart || p == pEnd) {
                // 1. Connect the top left corner (1) to the bottom right corner (N).
                if (n != 1) {
                    isSolved = false
                    return
                }
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                continue
            }
            when (n) {
                0 -> continue
                2 -> {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
                else -> {
                    isSolved = false
                    return
                }
            }
        }
        val nums = mutableSetOf<Int>()
        for (p in pos2node.keys) {
            val o = get(p)
            nums.add(game.get(p))
            for (i in 0..3) {
                if (!o[i]!!) continue
                val p2 = p.add(NumberPathGame.Companion.offset.get(i))
                g.connectNode(pos2node[p], pos2node[p2])
            }
        }
        // 1. Connect the top left corner (1) to the bottom right corner (N), including
        // all the numbers between 1 and N, only once.
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        val n1: Int = game.get(pEnd)
        val n2 = nums.size
        val n3 = nodeList.size
        val n4 = pos2node.size
        if (n1 != n2 || n1 != n3 || n1 != n4) isSolved = false
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) {
            objArray[i] = arrayOfNulls(4)
            Arrays.fill(objArray[i], false)
        }
    }
}