package com.zwstudio.logicpuzzlesandroid.puzzles.loopy

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*

class LoopyGameState(game: LoopyGame) : CellsGameState<LoopyGame, LoopyGameMove, LoopyGameState>(game) {
    var objArray = Cloner().deepClone(game.objArray)

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    private fun isValidMove(move: LoopyGameMove) = !(move.p.row == rows - 1 && move.dir == 2 || move.p.col == cols - 1 && move.dir == 1)

    init {
        updateIsSolved()
    }

    fun setObject(move: LoopyGameMove): Boolean {
        if (!isValidMove(move)) return false
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + LoopyGame.offset[dir]
        if (!isValid(p2) || game[p1][dir] == GridLineObject.Line || this[p1][dir] == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: LoopyGameMove): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 5/Loopy

        Summary
        Loop a loop! And touch all the dots!

        Description
        1. Draw a single looping path. You have to touch all the dots. As usual,
           the path cannot have branches or cross itself.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = this[p].filter { it == GridLineObject.Line }.size
                when (n) {
                    2 -> {
                        val node = Node(p.toString())
                        g.addNode(node)
                        pos2node[p] = node
                    }
                    else -> {
                        // 1. The path cannot have branches or cross itself.
                        // 1. You have to touch all the dots.
                        isSolved = false
                        return
                    }
                }
            }
        for (p in pos2node.keys) {
            val dotObj = get(p)
            for (i in 0 until 4) {
                if (dotObj[i] != GridLineObject.Line) continue
                val p2 = p + LoopyGame.offset[i]
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 1. Draw a single looping path.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}