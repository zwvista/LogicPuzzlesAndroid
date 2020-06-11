package com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class PataGameState(game: PataGame) : CellsGameState<PataGame, PataGameMove, PataGameState>(game) {
    var objArray = Array<PataObject>(rows() * cols()) { PataEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PataObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: PataObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = PataHintObject()
        updateIsSolved()
    }

    fun setObject(move: PataGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld is PataHintObject || objOld == objNew) return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    fun switchObject(move: PataGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is PataEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) PataMarkerObject() else PataWallObject()
            is PataWallObject -> if (markerOption == MarkerOptions.MarkerLast) PataMarkerObject() else PataEmptyObject()
            is PataMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) PataWallObject() else PataEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Pata

        Summary
        Yes, it's the opposite of Tapa

        Description
        1. Plays the opposite of Tapa, regarding the hints:
        2. A number indicates the groups of connected empty tiles that are around
           it, instead of filled ones.
        3. Different groups of empty tiles are separated by at least one filled cell.
        4. Same as Tapa:
        5. The filled tiles are continuous.
        6. You can't have a 2*2 space of filled tiles.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. A number indicates the groups of connected empty tiles that are around
        // it, instead of filled ones.
        fun computeHint(emptied: List<Int>): List<Int> {
            val hint = mutableListOf<Int>()
            if (emptied.isEmpty())
                hint.add(0)
            else {
                for (j in emptied.indices)
                    if (j == 0 || emptied[j] - emptied[j - 1] != 1)
                        hint.add(1)
                    else
                        hint[hint.size - 1] = hint[hint.size - 1] + 1
                if (emptied.size > 1 && hint.size > 1 && emptied[emptied.size - 1] - emptied[0] == 7) {
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
            val emptied = (0 until 8).filter {
                val p2 = p.add(PataGame.offset[it])
                if (!isValid(p2))
                    false
                else {
                    val o = this[p2]
                    o is PataEmptyObject || o is PataHintObject
                }
            }
            val arr: List<Int> = computeHint(emptied)
            val filled = (0 until 8).filter {
                val p2 = p.add(PataGame.offset[it])
                isValid(p2) && this[p2] is PataWallObject
            }
            val arr3 = computeHint(filled)
            val s = if (arr3.size == 1 && arr3[0] == 0) HintState.Normal else if (isCompatible(arr, arr2)) HintState.Complete else HintState.Error
            this[p] = PataHintObject(s)
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // 6. You can't have a 2*2 space of filled tiles.
        for (r in 0 until rows() - 1)
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                if (PataGame.offset2.all {
                    val o = this[p.add(it)]
                    o is PataWallObject
                }) {
                    isSolved = false
                    return
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (this[p] is PataWallObject)
                    rngWalls.add(p)
            }
        for (p in rngWalls)
            for (os in PataGame.offset) {
                val p2 = p.add(os)
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        // 5. The filled tiles are continuous.
        g.setRootNode(pos2node[rngWalls[0]])
        val nodeList = g.bfs()
        if (rngWalls.size != nodeList.size) isSolved = false
    }
}