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
                        // 5. On the Park all posts are already marked with a dot.
                        isB || !game.posts.contains(p)
                    3, 4 ->
                        // 5. Where three or four fences meet, a fence post is put in place.
                        game.posts.contains(p)
                    else ->
                        false
                }) { isSolved = false; return }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                for (i in 0..<4)
                    if (this[p + WildlifeParkGame.offset2[i]][WildlifeParkGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + WildlifeParkGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            // 3. Fences should encompass at least one animal of a certain species, and all
            //    animals of a certain species must be in the same enclosure.
            // 4. There can't be empty enclosures.
            if (game.animals.count {
                it.any { area.contains(it) }
            } != 1) { isSolved = false; return }
        }
    }
}