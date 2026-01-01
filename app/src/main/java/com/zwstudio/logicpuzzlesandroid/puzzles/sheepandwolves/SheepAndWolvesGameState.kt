package com.zwstudio.logicpuzzlesandroid.puzzles.sheepandwolves

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SheepAndWolvesGameState(game: SheepAndWolvesGame) : CellsGameState<SheepAndWolvesGame, SheepAndWolvesGameMove, SheepAndWolvesGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { GridLineObject.Empty } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<GridLineObject>) {this[p.row, p.col] = obj}
    private fun isValidMove(move: SheepAndWolvesGameMove) = !(move.p.row == rows - 1 && move.dir == 2 || move.p.col == cols - 1 && move.dir == 1)

    init {
        updateIsSolved()
    }

    override fun setObject(move: SheepAndWolvesGameMove): GameOperationType {
        if (!isValidMove(move)) return GameOperationType.Invalid
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + SheepAndWolvesGame.offset[dir]
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SheepAndWolvesGameMove): GameOperationType {
        if (!isValidMove(move)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[move.p][move.dir]) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 12/Sheep & Wolves

        Summary
        Where's a dog when you need one?

        Description
        1. Plays like SlitherLink:
        2. Draw a single looping path with the aid of the numbered hints. The
           path cannot have branches or cross itself.
        3. Each number tells you on how many of its four sides are touched by
           the path.
        4. With this added rule:
        5. In the end all the sheep must be corralled inside the loop, while
           all the wolves must be outside.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 3. Each number tells you on how many of its four sides are touched by
        //    the path.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            if (this[p][1] == GridLineObject.Line) n1++
            if (this[p][2] == GridLineObject.Line) n1++
            if (this[p + Position(1, 1)][0] == GridLineObject.Line) n1++
            if (this[p + Position(1, 1)][3] == GridLineObject.Line) n1++
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
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
                        // 2. The path cannot have branches or cross itself.
                        isSolved = false
                        return
                    }
                }
            }
        for (p in pos2node.keys) {
            val dotObj = this[p]
            for (i in 0 until 4) {
                if (dotObj[i] != GridLineObject.Line) continue
                val p2 = p + SheepAndWolvesGame.offset[i]
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 2. Draw a single looping path with the aid of the numbered hints.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        if (!isSolved) return
        val sheep0 = game.sheep.first()
        val d = 0
        var n = 0
        val os = SheepAndWolvesGame.offset[d]
        var p2 = sheep0
        while (isValid(p2)) {
            if (this[p2 + SheepAndWolvesGame.offset2[d]][SheepAndWolvesGame.dirs[d]] == GridLineObject.Line) { n += 1 }
            p2 += os
        }
        if (n % 2 == 0) isSolved = false
        if (!isSolved) return
        // 5. In the end all the sheep must be corralled inside the loop, while
        //    all the wolves must be outside.
        val g2 = Graph()
        val pos2node2 = mutableMapOf<Position, Node>()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g2.addNode(node)
                pos2node2[p] = node
            }
        for ((p, node) in pos2node2)
            for (i in 0 until 4) {
                if (this[p + SheepAndWolvesGame.offset2[i]][SheepAndWolvesGame.dirs[i]] == GridLineObject.Line) continue
                val p2 = p + SheepAndWolvesGame.offset[i]
                val node2 = pos2node2[p2]
                if (node2 != null)
                    g2.connectNode(node, node2)
            }
        g2.rootNode = pos2node2[sheep0]!!
        val nodeList2 = g2.bfs()
        if (!game.sheep.all { nodeList2.contains(pos2node2[it]!!) } ||
            game.wolves.any { nodeList2.contains(pos2node2[it]!!) })
            isSolved = false
    }
}