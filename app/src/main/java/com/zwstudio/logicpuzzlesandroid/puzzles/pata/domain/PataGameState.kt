package com.zwstudio.logicpuzzlesandroid.puzzles.pata.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import fj.data.HashMap
import fj.data.Stream
import java.util.*

class PataGameState(game: PataGame) : CellsGameState<PataGame, PataGameMove, PataGameState>(game) {
    var objArray: Array<PataObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: PataObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: PataObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: PataGameMove): Boolean {
        val p: Position = move.p
        val objOld: PataObject? = get(p)
        val objNew: PataObject = move.obj
        if (objOld is PataHintObject || objOld == objNew) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: PataGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<PataObject, PataObject> = label@ F<PataObject, PataObject> { obj: PataObject? ->
            if (obj is PataEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) PataMarkerObject() else PataWallObject()
            if (obj is PataWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) PataMarkerObject() else PataEmptyObject()
            if (obj is PataMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) PataWallObject() else PataEmptyObject()
            obj
        }
        move.obj = f.f(get(move.p))
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
        val computeHint: F<List<Int>, List<Int>> = F<List<Int>, List<Int>> { emptied: List<Int> ->
            val hint = mutableListOf<Int>()
            if (emptied.isEmpty()) hint.add(0) else {
                for (j in emptied.indices) if (j == 0 || emptied[j] - emptied[j - 1] != 1) hint.add(1) else hint[hint.size - 1] = hint[hint.size - 1] + 1
                if (emptied.size > 1 && hint.size > 1 && emptied[emptied.size - 1] - emptied[0] == 7) {
                    hint[0] = hint[0] + hint[hint.size - 1]
                    hint.removeAt(hint.size - 1)
                }
                Collections.sort(hint)
                // hint.sort(Comparator.naturalOrder());
            }
            hint
        }
        val isCompatible: F2<List<Int>, List<Int>, Boolean> = label@ F2<List<Int>, List<Int>, Boolean> { computedHint: List<Int>, givenHint: List<Int> ->
            if (computedHint == givenHint) return@label true
            if (computedHint.size != givenHint.size) return@label false
            val h1: Set<Int> = HashSet(computedHint)
            val h2: MutableSet<Int> = HashSet(givenHint)
            h2.remove(-1)
            h1.containsAll(h2)
        }
        HashMap.fromMap<Position, List<Int>>(game.pos2hint).foreachDoEffect(Effect1<P2<Position, List<Int>>> { kv: P2<Position?, List<Int?>?> ->
            val p: Position = kv._1()
            val arr2: List<Int> = kv._2()
            val emptied = Stream.range(0, 8).filter(F<Int, Boolean> { i: Int? ->
                val p2 = p.add(PataGame.offset.get(i!!))
                if (!isValid(p2)) return@filter false
                val o: PataObject? = get(p2)
                o is PataEmptyObject || o is PataHintObject
            }).toJavaList()
            val arr: List<Int> = computeHint.f(emptied)
            val filled = Stream.range(0, 8).filter(F<Int, Boolean> { i: Int? ->
                val p2 = p.add(PataGame.offset.get(i!!))
                isValid(p2) && get(p2) is PataWallObject
            }).toJavaList()
            val arr3: List<Int> = computeHint.f(filled)
            val s: HintState = if (arr3.size == 1 && arr3[0] == 0) HintState.Normal else if (isCompatible.f(arr, arr2)) HintState.Complete else HintState.Error
            val o = PataHintObject()
            o.state = s
            set(p, o)
            if (s != HintState.Complete) isSolved = false
        })
        if (!isSolved) return
        // 6. You can't have a 2*2 space of filled tiles.
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            if (fj.data.Array.array<Position>(*PataGame.offset2).forall(F<Position, Boolean> { os: Position? ->
                    val o: PataObject? = get(p.add(os))
                    o is PataWallObject
                })) {
                isSolved = false
                return
            }
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
            if (get(p) is PataWallObject) rngWalls.add(p)
        }
        for (p in rngWalls) for (os in PataGame.offset) {
            val p2 = p.add(os)
            if (rngWalls.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        // 5. The filled tiles are continuous.
        g.setRootNode(pos2node[rngWalls[0]])
        val nodeList = g.bfs()
        if (rngWalls.size != nodeList.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls<PataObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = PataEmptyObject()
        for (p in game.pos2hint.keys) set(p, PataHintObject())
    }
}