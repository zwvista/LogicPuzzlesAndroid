package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class FenceSentinelsGameState(game: FenceSentinelsGame) : CellsGameState<FenceSentinelsGame, FenceSentinelsGameMove, FenceSentinelsGameState>(game) {
    var objArray = MutableList(rows * cols) { MutableList(4) { GridLineObject.Empty } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: MutableList<GridLineObject>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: MutableList<GridLineObject>) {this[p.row, p.col] = obj}
    private fun isValidMove(move: FenceSentinelsGameMove) = !(move.p.row == rows - 1 && move.dir == 2 || move.p.col == cols - 1 && move.dir == 1)

    init {
        updateIsSolved()
    }

    fun setObject(move: FenceSentinelsGameMove): Boolean {
        if (!isValidMove(move)) return false
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + FenceSentinelsGame.offset[dir]
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: FenceSentinelsGameMove): Boolean {
        if (!isValidMove(move)) return false
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p][move.dir]
        move.obj = when (o) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
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
        for ((p, n2) in game.pos2hint) {
            var n1 = -3
            for (i in 0 until 4) {
                val os: Position = FenceSentinelsGame.offset[i]
                var p2 = +p
                while (isValid(p2)) {
                    n1++
                    if (this[p2 + FenceSentinelsGame.offset2[i]][FenceSentinelsGame.dirs[i]] == GridLineObject.Line) break
                    p2 += os
                }
            }
            pos2state[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = this[p].filter { it == GridLineObject.Line }.size
                when (n) {
                    0 -> {}
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
            val dotObj = this[p]
            for (i in 0 until 4) {
                if (dotObj[i] != GridLineObject.Line) continue
                val p2 = p + FenceSentinelsGame.offset[i]
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 1. The goal is to draw a single, uninterrupted, closed loop.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}