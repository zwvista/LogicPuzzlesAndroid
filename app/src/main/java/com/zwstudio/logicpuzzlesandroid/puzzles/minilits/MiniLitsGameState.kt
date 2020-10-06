package com.zwstudio.logicpuzzlesandroid.puzzles.minilits

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class MiniLitsGameState(game: MiniLitsGame) : CellsGameState<MiniLitsGame, MiniLitsGameMove, MiniLitsGameState>(game) {
    var objArray = Array<MiniLitsObject>(rows * cols) { MiniLitsEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

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

    override fun setObject(move: MiniLitsGameMove): Boolean {
        if (!isValid(move.p) || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    override fun switchObject(move: MiniLitsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is MiniLitsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) MiniLitsMarkerObject() else MiniLitsTreeObject()
            is MiniLitsTreeObject -> if (markerOption == MarkerOptions.MarkerLast) MiniLitsMarkerObject() else MiniLitsEmptyObject()
            is MiniLitsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) MiniLitsTreeObject() else MiniLitsEmptyObject()
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
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o: MiniLitsObject? = this[p]
                if (o is MiniLitsForbiddenObject)
                    this[r, c] = MiniLitsEmptyObject()
                else if (o is MiniLitsTreeObject) {
                    o.state = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in MiniLitsGame.offset) {
                val p2 = p + os
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
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
        val infos = (0 until game.areas.size).map { _ -> MiniLitsAreaInfo() }
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
                (this[p] as MiniLitsTreeObject).state = AllowedObjectState.Error
        }
        for (i in infos.indices) {
            val info = infos[i]
            val treeCount = info.trees.size
            if (treeCount >= 3 && allowedObjectsOnly)
                for (p in game.areas[i]) {
                    val o = this[p]
                    if (o is MiniLitsEmptyObject || o is MiniLitsMarkerObject)
                        this[p] = MiniLitsForbiddenObject()
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
                this[p + os] = MiniLitsTreeObject(AllowedObjectState.Error)
        }
    }
}