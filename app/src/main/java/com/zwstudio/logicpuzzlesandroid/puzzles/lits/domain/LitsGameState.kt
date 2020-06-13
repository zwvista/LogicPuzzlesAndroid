package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class LitsGameState(game: LitsGame) : CellsGameState<LitsGame, LitsGameMove, LitsGameState>(game) {
    var objArray = Array<LitsObject>(rows * cols) { LitsEmptyObject() }
    var pos2state: Map<Position, HintState> = HashMap()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: LitsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: LitsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    private inner class LitsAreaInfo {
        var trees = mutableListOf<Position>()
        var blockIndexes = mutableSetOf<Int>()
        var neighborIndexes = mutableSetOf<Int>()
        var tetrominoIndex = -1
    }

    fun setObject(move: LitsGameMove): Boolean {
        if (!isValid(move.p) || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: LitsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when(o) {
            is LitsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) LitsMarkerObject() else LitsTreeObject()
            is LitsTreeObject -> if (markerOption == MarkerOptions.MarkerLast) LitsMarkerObject() else LitsEmptyObject()
            is LitsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) LitsTreeObject() else LitsEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/Lits

        Summary
        Tetris without the fat guy

        Description
        1. You play the game with all the Tetris pieces, except the square one.
        2. So in other words you use pieces of four squares (tetrominoes) in the
           shape of L, I, T and S, which can also be rotated or reflected (mirrored).
        3. The board is divided into many areas. You have to place a tetromino
           into each area respecting these rules:
        4. No two adjacent (touching horizontally / vertically) tetromino should
           be of equal shape, even counting rotations or reflections.
        5. All the shaded cells should form a valid Nurikabe (hence no fat guy).
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is LitsForbiddenObject)
                    this[r, c] = LitsEmptyObject()
                else if (o is LitsTreeObject) {
                    o.state = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in LitsGame.offset) {
                val p2 = p + os
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
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
        // 5. All the shaded cells should form a valid Nurikabe.
        if (blocks.size != 1) isSolved = false
        val infos = (0 until game.areas.size).map { _ -> LitsAreaInfo() }
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
                for (os in LitsGame.offset) {
                    val p2 = p + os
                    val index = infos.indexOfFirst { it.trees.contains(p2) }
                    if (index != -1 && index != i)
                        info.neighborIndexes.add(index)
                }
        }
        fun notSolved(info: LitsAreaInfo) {
            isSolved = false
            for (p in info.trees) {
                val o = this[p] as LitsTreeObject
                o.state = AllowedObjectState.Error
            }
        }
        for (i in infos.indices) {
            val info = infos[i]
            val treeCount = info.trees.size
            if (treeCount >= 4 && allowedObjectsOnly)
                for (p in game.areas[i]) {
                    val o = this[p]
                    if (o is LitsEmptyObject || o is LitsMarkerObject)
                        this[p] = LitsForbiddenObject()
                }
            if (treeCount > 4 || treeCount == 4 && info.blockIndexes.size > 1)
                notSolved(info)
            // 3. The board is divided into many areas. You have to place a tetromino
            // into each area.
            if (treeCount == 4 && info.blockIndexes.size == 1) {
                info.trees.sort()
                val treeOffsets = mutableListOf<Position>()
                val p2 = Position(info.trees.map { it.row }.min()!!, info.trees.map { it.col }.min()!!)
                for (p in info.trees)
                    treeOffsets.add(p - p2)
                info.tetrominoIndex = LitsGame.tetrominoes.indexOfFirst { it.any { it == treeOffsets } }
                if (info.tetrominoIndex == -1)
                    notSolved(info)
            }
            if (treeCount < 4) isSolved = false
        }
        // 4. No two adjacent (touching horizontally / vertically) tetromino should
        // be of equal shape, even counting rotations or reflections.
        for (i in infos.indices) {
            val info = infos[i]
            val index = info.tetrominoIndex
            if (index == -1) continue
            if (info.neighborIndexes.any {infos[it].tetrominoIndex == index })
                notSolved(info)
        }
        if (!isSolved) return
        val block = blocks[0]
        // 5. All the shaded cells should form a valid Nurikabe.
        rule2x2@ for (p in block) {
            for (os in LitsGame.offset3)
                if (block.contains(p + os))
                    continue@rule2x2
            isSolved = false
            for (os in LitsGame.offset3)
                this[p + os] = LitsTreeObject(AllowedObjectState.Error)
        }
    }
}