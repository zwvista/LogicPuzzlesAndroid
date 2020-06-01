package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.data.HashMap
import fj.data.Stream
import java.util.*

class TapaIslandsGameState(game: TapaIslandsGame) : CellsGameState<TapaIslandsGame, TapaIslandsGameMove, TapaIslandsGameState>(game) {
    var objArray: Array<TapaIslandsObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: TapaIslandsObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: TapaIslandsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TapaIslandsGameMove): Boolean {
        val p: Position = move.p
        val objOld: TapaIslandsObject? = get(p)
        val objNew: TapaIslandsObject = move.obj
        if (objOld is TapaIslandsHintObject || objOld == objNew) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TapaIslandsGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<TapaIslandsObject, TapaIslandsObject> = label@ F<TapaIslandsObject, TapaIslandsObject> { obj: TapaIslandsObject? ->
            if (obj is TapaIslandsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TapaIslandsMarkerObject() else TapaIslandsWallObject()
            if (obj is TapaIslandsWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) TapaIslandsMarkerObject() else TapaIslandsEmptyObject()
            if (obj is TapaIslandsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TapaIslandsWallObject() else TapaIslandsEmptyObject()
            obj
        }
        move.obj = f.f(get(move.p))
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
        val computeHint: F<List<Int>, List<Int>> = F<List<Int>, List<Int>> { filled: List<Int> ->
            val hint = mutableListOf<Int>()
            if (filled.isEmpty()) hint.add(0) else {
                for (j in filled.indices) if (j == 0 || filled[j] - filled[j - 1] != 1) hint.add(1) else hint[hint.size - 1] = hint[hint.size - 1] + 1
                if (filled.size > 1 && hint.size > 1 && filled[filled.size - 1] - filled[0] == 7) {
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
            val filled = Stream.range(0, 8).filter(F<Int, Boolean> { i: Int? ->
                val p2 = p.add(TapaIslandsGame.offset.get(i!!))
                isValid(p2) && get(p2) is TapaIslandsWallObject
            }).toJavaList()
            val arr: List<Int> = computeHint.f(filled)
            val s: HintState = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible.f(arr, arr2)) HintState.Complete else HintState.Error
            val o = TapaIslandsHintObject()
            o.state = s
            set(p, o)
            if (s != HintState.Complete) isSolved = false
        })
        if (!isSolved) return
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            if (fj.data.Array.array<Position>(*TapaIslandsGame.offset2).forall(F<Position, Boolean> { os: Position? ->
                    val o: TapaIslandsObject? = get(p.add(os))
                    o is TapaIslandsWallObject
                })) {
                isSolved = false
                return
            }
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        var rngEmpty = mutableListOf<Position>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
            if (get(p) is TapaIslandsWallObject) rngWalls.add(p) else rngEmpty.add(p)
        }
        for (p in rngWalls) for (os in TapaIslandsGame.offset) {
            val p2 = p.add(os)
            if (rngWalls.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        for (p in rngEmpty) for (os in TapaIslandsGame.offset) {
            val p2 = p.add(os)
            if (rngEmpty.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        if (rngWalls.isEmpty()) {
            isSolved = false
            return
        }
        run {

            // The goal is to fill some tiles forming a single orthogonally continuous
            // path. Just like Nurikabe.
            g.setRootNode(pos2node[rngWalls[0]])
            val nodeList = g.bfs()
            if (rngWalls.size != nodeList.size) {
                isSolved = false
                return
            }
        }
        while (!rngEmpty.isEmpty()) {
            g.setRootNode(pos2node[rngEmpty[0]])
            val nodeList = g.bfs()
            rngEmpty = fj.data.List.iterableList(rngEmpty).filter(F<Position, Boolean> { p: Position -> !nodeList.contains(pos2node[p]) }).toJavaList()
            val n2 = nodeList.size
            val rng = mutableListOf<Position>()
            for (p in game.pos2hint.keys) if (nodeList.contains(pos2node[p])) rng.add(p)
            when (rng.size) {
                0 -> isSolved = false
                1 -> {

                    // 3. Each separated area may contain at most one clue tile.
                    // 4. If there is a clue tile in an area, at least one digit should give the
                    // size of that area in unit squares.
                    val p = rng[0]
                    val arr2: List<Int> = game.pos2hint.get(p)
                    val s: HintState = if (arr2.contains(n2)) HintState.Complete else HintState.Error
                    val o = TapaIslandsHintObject()
                    o.state = s
                    set(p, o)
                    if (s != HintState.Complete) isSolved = false
                }
                else -> {
                    for (p in rng) {
                        val o = TapaIslandsHintObject()
                        o.state = HintState.Normal
                        set(p, o)
                    }
                    isSolved = false
                }
            }
        }
    }

    init {
        objArray = arrayOfNulls<TapaIslandsObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = TapaIslandsEmptyObject()
        for (p in game.pos2hint.keys) set(p, TapaIslandsHintObject())
    }
}