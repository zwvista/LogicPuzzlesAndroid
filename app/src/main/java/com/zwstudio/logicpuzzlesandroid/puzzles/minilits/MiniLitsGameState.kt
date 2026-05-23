package com.zwstudio.logicpuzzlesandroid.puzzles.minilits

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MiniLitsGameState(game: MiniLitsGame) : CellsGameState<MiniLitsGame, MiniLitsGameMove, MiniLitsGameState>(game) {
    val objArray = Array(rows * cols) { MiniLitsObject.Empty }
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: MiniLitsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: MiniLitsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    private inner class MiniLitsAreaInfo {
        var trees = mutableListOf<Position>()
        var blockIndexes = mutableSetOf<Int>()
        var neighborIndexes = mutableSetOf<Int>()
        var tetrominoIndex = -1
    }

    override fun setObject(move: MiniLitsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: MiniLitsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            MiniLitsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) MiniLitsObject.Marker else MiniLitsObject.Tree
            MiniLitsObject.Tree -> if (markerOption == MarkerOptions.MarkerLast) MiniLitsObject.Marker else MiniLitsObject.Empty
            MiniLitsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) MiniLitsObject.Tree else MiniLitsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 14/Mini-Lits

        Summary
        Lits Jr.

        Description
        1. You play the game with triominos (pieces of three squares).
        2. The board is divided into many areas. You have to place a triomino
           into each area respecting these rules:
        3. No two adjacent (touching horizontally / vertically) triominos should
           be of equal shape & orientation.
        4. All the shaded cells should form a valid Nurikabe.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o: MiniLitsObject? = this[p]
                if (o == MiniLitsObject.Forbidden)
                    this[r, c] = MiniLitsObject.Empty
                else if (o == MiniLitsObject.Tree) {
                    pos2state[p] = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in MiniLitsGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        }
        val blocks = mutableListOf<List<Position>>()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val block = pos2node.filter { nodeList.contains(it.value) }.keys.toList()
            blocks.add(block)
            for (p in block)
                pos2node.remove(p)
        }
        // 4. All the shaded cells should form a valid Nurikabe.
        if (blocks.size != 1) isSolved = false
        val infos = (0..<game.areas.size).map { _ -> MiniLitsAreaInfo() }
        for (i in blocks.indices) {
            val block = blocks[i]
            for (p in block) {
                val n = game.pos2area[p]!!
                val info = infos[n]
                info.trees.add(p)
                info.blockIndexes.add(i)
            }
        }
        for (i in infos.indices) {
            val info = infos[i]
            for (p in info.trees)
                for (os in MiniLitsGame.offset) {
                    val p2 = p + os
                    val index = infos.indexOfFirst { it.trees.contains(p2) }
                    if (index != -1 && index != i)
                        info.neighborIndexes.add(index)
                }
        }
        fun notSolved(info: MiniLitsAreaInfo) {
            isSolved = false
            for (p in info.trees)
                pos2state[p] = AllowedObjectState.Error
        }
        for (i in infos.indices) {
            val info = infos[i]
            val treeCount = info.trees.size
            if (treeCount >= 3 && allowedObjectsOnly)
                for (p in game.areas[i]) {
                    val o = this[p]
                    if (o == MiniLitsObject.Empty || o == MiniLitsObject.Marker)
                        this[p] = MiniLitsObject.Forbidden
                }
            if (treeCount > 3 || treeCount == 3 && info.blockIndexes.size > 1)
                notSolved(info)
            // 2. The board is divided into many areas. You have to place a triomino
            // into each area.
            if (treeCount == 3 && info.blockIndexes.size == 1) {
                info.trees.sort()
                val treeOffsets = mutableListOf<Position>()
                val p2 = Position(info.trees.map { it.row }.minOrNull()!!, info.trees.map { it.col }.minOrNull()!!)
                for (p in info.trees)
                    treeOffsets.add(p - p2)
                info.tetrominoIndex = MiniLitsGame.triominos.indexOfFirst { it == treeOffsets }
                if (info.tetrominoIndex == -1)
                    notSolved(info)
            }
            if (treeCount < 3) isSolved = false
        }
        // 3. No two adjacent (touching horizontally / vertically) triominos should
        // be of equal shape & orientation.
        for (i in infos.indices) {
            val info = infos[i]
            val index = info.tetrominoIndex
            if (index == -1) continue
            if (info.neighborIndexes.any { infos[it].tetrominoIndex == index })
                notSolved(info)
        }
        if (!isSolved) return
        // 4. All the shaded cells should form a valid Nurikabe.
        val block = blocks[0]
        rule2x2@ for (p in block) {
            for (os in MiniLitsGame.offset3)
                if (block.contains(p + os))
                    continue@rule2x2
            isSolved = false
            for (os in MiniLitsGame.offset3)
                pos2state[p + os] = AllowedObjectState.Error
        }
    }
}