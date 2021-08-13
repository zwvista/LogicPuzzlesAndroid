package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import java.util.*

class TapaIslandsGameState(game: TapaIslandsGame) : CellsGameState<TapaIslandsGame, TapaIslandsGameMove, TapaIslandsGameState>(game) {
    var objArray = Array<TapaIslandsObject>(rows * cols) { TapaIslandsEmptyObject }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TapaIslandsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: TapaIslandsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = TapaIslandsHintObject()
        updateIsSolved()
    }

    override fun setObject(move: TapaIslandsGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld is TapaIslandsHintObject || objOld == objNew) return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    override fun switchObject(move: TapaIslandsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is TapaIslandsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) TapaIslandsMarkerObject else TapaIslandsWallObject()
            is TapaIslandsWallObject -> if (markerOption == MarkerOptions.MarkerLast) TapaIslandsMarkerObject else TapaIslandsEmptyObject
            is TapaIslandsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) TapaIslandsWallObject() else TapaIslandsEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Tapa Islands

        Summary
        Tap-Archipelago

        Description
        1. Plays with the same rules as Tapa with these variations.
        2. Empty tiles from 'islands', or separated areas, are surrounded by the
           filled tiles.
        3. Each separated area may contain at most one clue tile.
        4. If there is a clue tile in an area, at least one digit should give the
           size of that area in unit squares.
    */
    private fun updateIsSolved() {
        isSolved = true
        // A number indicates how many of the surrounding tiles are filled. If a
        // tile has more than one number, it hints at multiple separated groups
        // of filled tiles.
        fun computeHint(filled: List<Int>): List<Int> {
            val hint = mutableListOf<Int>()
            if (filled.isEmpty())
                hint.add(0)
            else {
                for (j in filled.indices) if (j == 0 || filled[j] - filled[j - 1] != 1)
                    hint.add(1)
                else
                    hint[hint.size - 1] = hint[hint.size - 1] + 1
                if (filled.size > 1 && hint.size > 1 && filled[filled.size - 1] - filled[0] == 7) {
                    hint[0] = hint[0] + hint[hint.size - 1]
                    hint.removeAt(hint.size - 1)
                }
                hint.sort()
            }
            return hint
        }
        fun isCompatible(computedHint: List<Int>, givenHint: List<Int>): Boolean {
            if (computedHint == givenHint) return true
            if (computedHint.size != givenHint.size) return false
            val h1 = HashSet(computedHint)
            val h2 = HashSet(givenHint)
            h2.remove(-1)
            return h1.containsAll(h2)
        }
        for ((p, arr2) in game.pos2hint) {
            val filled = (0 until 8).filter {
                val p2 = p + TapaIslandsGame.offset[it]
                isValid(p2) && this[p2] is TapaIslandsWallObject
            }
            val arr = computeHint(filled)
            val s = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible(arr, arr2)) HintState.Complete else HintState.Error
            this[p] = TapaIslandsHintObject(s)
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                if (TapaIslandsGame.offset2.all {
                    val o = this[p + it]
                    o is TapaIslandsWallObject
                }) {
                    isSolved = false
                    return
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        var rngEmpty = mutableListOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (get(p) is TapaIslandsWallObject)
                    rngWalls.add(p)
                else
                    rngEmpty.add(p)
            }
        for (p in rngWalls)
            for (os in TapaIslandsGame.offset) {
                val p2 = p + os
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        for (p in rngEmpty)
            for (os in TapaIslandsGame.offset) {
                val p2 = p + os
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        if (rngWalls.isEmpty()) {
            isSolved = false
            return
        }
        run {
            // The goal is to fill some tiles forming a single orthogonally continuous
            // path. Just like Nurikabe.
            g.rootNode = pos2node[rngWalls[0]]!!
            val nodeList = g.bfs()
            if (rngWalls.size != nodeList.size) {
                isSolved = false
                return
            }
        }
        while (rngEmpty.isNotEmpty()) {
            g.rootNode = pos2node[rngEmpty[0]]!!
            val nodeList = g.bfs()
            rngEmpty = rngEmpty.filter { !nodeList.contains(pos2node[it]) }.toMutableList()
            val n2 = nodeList.size
            val rng = mutableListOf<Position>()
            for (p in game.pos2hint.keys)
                if (nodeList.contains(pos2node[p]))
                    rng.add(p)
            when (rng.size) {
                0 -> isSolved = false
                1 -> {

                    // 3. Each separated area may contain at most one clue tile.
                    // 4. If there is a clue tile in an area, at least one digit should give the
                    // size of that area in unit squares.
                    val p = rng[0]
                    val arr2 = game.pos2hint[p]!!
                    val s = if (arr2.contains(n2)) HintState.Complete else HintState.Error
                    this[p] = TapaIslandsHintObject(s)
                    if (s != HintState.Complete) isSolved = false
                }
                else -> {
                    for (p in rng)
                        this[p] = TapaIslandsHintObject()
                    isSolved = false
                }
            }
        }
    }
}