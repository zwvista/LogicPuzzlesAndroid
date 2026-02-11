package com.zwstudio.logicpuzzlesandroid.puzzles.wildlifepark

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WildlifeParkGameState(game: WildlifeParkGame) : CellsGameState<WildlifeParkGame, WildlifeParkGameMove, WildlifeParkGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: WildlifeParkGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + WildlifeParkGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: WildlifeParkGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 2/Puzzle Set 1/Wildlife Park

        Summary
        One rises, the other falls

        Description
        1. At the last game of Wildlife Football, Lemurs accused Giraffes of cheating,
           Monkeys ran away with the coin after the toss and Lions ate the ball.
        2. So you're given the task to raise some fencing between the different species,
           while spirits cool down.
        3. Fences should encompass at least one animal of a certain species, and all
           animals of a certain species must be in the same enclosure.
        4. There can't be empty enclosures.
        5. Where three or four fences meet, a fence post is put in place. On the Park
           all posts are already marked with a dot.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun isBorder(p: Position) =
            p.row == 0 || p.row == rows - 1 || p.col == 0 || p.col == cols - 1
        val pos2dirs = mutableMapOf<Position, MutableList<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val isB = isBorder(p)
                val dirs = (0..<4).filter { this[p][it] == GridLineObject.Line }.toMutableList()
                if (!when(dirs.size) {
                    0 ->
                        true
                    2 ->
                        // 4. Lines only turn at posts (dots).
                        // 6. Not all posts must be used.
                        isB || dirs[1] - dirs[0] == 2 || game.posts.contains(p)
                    3 ->
                        // 3. The lines (fencing) of the enclosures start and end on the edges of the
                        //    grid.
                        isB
                    4 ->
                        // 5. Lines can cross each other except posts (dots).
                        !game.posts.contains(p)
                    else ->
                        false
                }) { isSolved = false; return }
                if (isB)
                    dirs.removeAll { isBorder(p + WildlifeParkGame.offset[it]) }
                if (dirs.isNotEmpty())
                    pos2dirs[p] = dirs
            }
        // Check the lines
        while (pos2dirs.isNotEmpty()) {
            val p = pos2dirs.firstNotNullOfOrNull { if (it.value.size == 1) it.key else null }
            if (p == null) { isSolved = false; return }
            var p2 = p
            var n = -1
            while (true) {
                val dirs = pos2dirs[p2]
                if (dirs == null) { isSolved = false; return }
                if (dirs.size == 4) {
                    dirs.remove(n)
                    dirs.remove((n + 2) % 4)
                } else {
                    pos2dirs.remove(p2)
                    if (p2 != p && dirs.size == 1) break
                    n = dirs.first { (it + 2) % 4 != n }
                }
                p2 += WildlifeParkGame.offset[n]
            }
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                for (i in 0 until 4)
                    if (this[p + WildlifeParkGame.offset2[i]][WildlifeParkGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + WildlifeParkGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rngWolves = area.filter { game.wolves.contains(it) }
            val rngSheep = area.filter { game.sheep.contains(it) }
            // 2. Each enclosure must contain either sheep or wolves (but not both) and
            //    must not be empty.
            if (rngSheep.isEmpty() == rngWolves.isEmpty()) { isSolved = false; return }
        }
    }
}