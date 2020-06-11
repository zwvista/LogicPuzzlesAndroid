package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class BWTapaGameState(game: BWTapaGame) : CellsGameState<BWTapaGame, BWTapaGameMove, BWTapaGameState>(game) {
    var objArray = Array<BWTapaObject>(rows() * cols()) { BWTapaEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BWTapaObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: BWTapaObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = BWTapaHintObject()
        updateIsSolved()
    }

    fun setObject(move: BWTapaGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld is BWTapaHintObject || objOld == objNew) return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    fun switchObject(move: BWTapaGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p];
        move.obj = when (o) {
            is BWTapaEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) BWTapaMarkerObject() else BWTapaWallObject()
            is BWTapaWallObject -> if (markerOption == MarkerOptions.MarkerLast) BWTapaMarkerObject() else BWTapaEmptyObject()
            is BWTapaMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) BWTapaWallObject() else BWTapaEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/B&W Tapa

        Summary
        Black and white Tapas

        Description
        1. Play with the same rules as Tapa with these variations:
        2. Both Black and White cells must form a single continuous region.
        3. There can't be any 2*2 of white or black cells.
    */
    private fun updateIsSolved() {
        isSolved = true
        // A number indicates how many of the surrounding tiles are filled. If a
        // tile has more than one number, it hints at multiple separated groups
        // of filled tiles.
        fun computeHint(filled: List<Int>): List<Int> {
            if (filled.isEmpty()) return listOf(0)
            val hint = mutableListOf<Int>()
                for (j in filled.indices)
                    if (j == 0 || filled[j] - filled[j - 1] != 1)
                        hint.add(1)
                    else
                        hint[hint.size - 1]++
            if (filled.size > 1 && hint.size > 1 && filled.last() - filled.first() == 7) {
                hint[0] += hint.last()
                hint.removeAt(hint.size - 1)
            }
            hint.sort()
            return hint
        }
        fun isCompatible(computedHint: List<Int>, givenHint: List<Int>): Boolean {
            if (computedHint == givenHint) return true
            if (computedHint.size != givenHint.size) return false
            val h1 = computedHint.toHashSet()
            val h2 = givenHint.toHashSet()
            h2.remove(-1)
            return h1.containsAll(h2)
        }
        for ((p, arr2) in game.pos2hint) {
            val filled = (0 until 8).filter {
                val p2 = p.add(BWTapaGame.offset[it])
                isValid(p2) && this[p2] is BWTapaWallObject
            }
            val arr = computeHint(filled)
            val s = if (arr == listOf(0)) HintState.Normal else if (isCompatible(arr, arr2)) HintState.Complete else HintState.Error
            this[p] = BWTapaHintObject(s)
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // 3. There can't be any 2*2 of white or black cells.
        for (r in 0 until rows() - 1)
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                if (BWTapaGame.offset2.all { this[p.add(it)] is BWTapaWallObject } || BWTapaGame.offset2.all {
                    val o = this[p.add(it)]
                    o is BWTapaEmptyObject || o is BWTapaHintObject
                }) {
                    isSolved = false
                    return
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        val rngEmpty = mutableListOf<Position>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (this[p] is BWTapaWallObject)
                    rngWalls.add(p)
                else
                    rngEmpty.add(p)
            }
        for (p in rngWalls)
            for (os in BWTapaGame.offset) {
                val p2 = p.add(os)
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        for (p in rngEmpty)
            for (os in BWTapaGame.offset) {
                val p2 = p.add(os)
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        // 2. Both Black and White cells must form a single continuous region.
        g.rootNode = pos2node[rngWalls[0]]
        var nodeList = g.bfs()
        if (rngWalls.size != nodeList.size) {
            isSolved = false
            return
        }
        g.rootNode = pos2node[rngEmpty[0]]
        nodeList = g.bfs()
        if (rngEmpty.size != nodeList.size) isSolved = false
    }
}
