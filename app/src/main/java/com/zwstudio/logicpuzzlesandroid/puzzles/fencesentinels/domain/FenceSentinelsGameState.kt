package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.data.List
import java.util.*

class FenceSentinelsGameState(game: FenceSentinelsGame?) : CellsGameState<FenceSentinelsGame?, FenceSentinelsGameMove?, FenceSentinelsGameState?>(game) {
    var objArray: Array<Array<GridLineObject?>>
    var pos2state = mutableMapOf<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject?>) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: Array<GridLineObject?>) {
        set(p.row, p.col, obj)
    }

    private fun isValidMove(move: FenceSentinelsGameMove?) = !(move!!.p!!.row == rows() - 1 && move.dir == 2 || move.p!!.col == cols() - 1 && move.dir == 1)

    fun setObject(move: FenceSentinelsGameMove): Boolean {
        if (!isValidMove(move)) return false
        val p1 = move!!.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val o = get(p1)[dir]
        if (o == move.obj) return false
        val p2 = p1!!.add(FenceSentinelsGame.Companion.offset.get(dir))
        get(p1)[dir] = move.obj
        get(p2)[dir2] = get(p1)[dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: FenceSentinelsGameMove): Boolean {
        if (!isValidMove(move)) return false
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: GridLineObject? ->
            when (obj) {
                GridLineObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
                GridLineObject.Line -> return@label if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
                GridLineObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            }
            obj
        }
        val dotObj = get(move!!.p)
        move.obj = f.f(dotObj[move.dir])
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/Fence Sentinels

        Summary
        We used to guard a castle, you know?

        Description
        1. The goal is to draw a single, uninterrupted, closed loop.
        2. The loop goes around all the numbers.
        3. The number tells you how many cells you can see horizontally or
           vertically from there, including the cell itself.

        Variant
        4. Some levels are marked 'Inside Outside'. In this case some numbers
           are on the outside of the loop.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. The loop goes around all the numbers.
        // 3. The number tells you how many cells you can see horizontally or
        // vertically from there, including the cell itself.
        for ((p, n2) in game!!.pos2hint) {
            var n1 = -3
            for (i in 0..3) {
                val os: Position = FenceSentinelsGame.Companion.offset.get(i)
                val p2 = p!!.plus()
                while (isValid(p2)) {
                    n1++
                    if (get(p2.add(FenceSentinelsGame.Companion.offset2.get(i)))[FenceSentinelsGame.Companion.dirs.get(i)] == GridLineObject.Line) break
                    p2.addBy(os)
                }
            }
            pos2state[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val n = fj.data.Array.array(*get(p)).filter { o: GridLineObject -> o == GridLineObject.Line }.length()
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
        for (p in pos2node.keys) {
            val dotObj = get(p)
            for (i in 0..3) {
                if (dotObj[i] != GridLineObject.Line) continue
                val p2 = p.add(FenceSentinelsGame.Companion.offset.get(i))
                g.connectNode(pos2node[p], pos2node[p2])
            }
        }
        // 1. The goal is to draw a single, uninterrupted, closed loop.
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) {
            objArray[i] = arrayOfNulls(4)
            Arrays.fill(objArray[i], GridLineObject.Empty)
        }
        updateIsSolved()
    }
}