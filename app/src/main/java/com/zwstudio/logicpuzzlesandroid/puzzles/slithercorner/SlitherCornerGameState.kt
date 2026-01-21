package com.zwstudio.logicpuzzlesandroid.puzzles.slithercorner

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SlitherCornerGameState(game: SlitherCornerGame) : CellsGameState<SlitherCornerGame, SlitherCornerGameMove, SlitherCornerGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { GridLineObject.Empty } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<GridLineObject>) {this[p.row, p.col] = obj}
    private fun isValidMove(move: SlitherCornerGameMove) = !(move.p.row == rows - 1 && move.dir == 2 || move.p.col == cols - 1 && move.dir == 1)

    init {
        updateIsSolved()
    }

    override fun setObject(move: SlitherCornerGameMove): GameOperationType {
        if (!isValidMove(move)) return GameOperationType.Invalid
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + SlitherCornerGame.offset[dir]
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SlitherCornerGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 4/Puzzle Set 2/SlitherCorner

        Summary
        Corners instead of sides

        Description
        1. Draw a path like a SlitherLink (non intercepting loop) with the
           following hints:
        2. The number in a cell tells you how many tiles the path turn by 90
           degrees around it.
        3. Note that around 0s that can be a line but it just don't won't turn.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. Each number in a tile tells you on how many of its four sides are touched
        // by the path.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            for (i in 0 until 4)
                if (this[p + SlitherCornerGame.offset2[i]][SlitherCornerGame.dirs[i]] == GridLineObject.Line) n1++
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
                        // 1. The path cannot have branches or cross itself.
                        isSolved = false
                        return
                    }
                }
            }
        for (p in pos2node.keys) {
            val dotObj = this[p]
            for (i in 0 until 4) {
                if (dotObj[i] != GridLineObject.Line) continue
                val p2 = p + SlitherCornerGame.offset[i]
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 1. Draw a single looping path with the aid of the numbered hints.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}