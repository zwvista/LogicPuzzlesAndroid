package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import fj.data.HashMap
import fj.data.Stream
import java.util.*

class TapaGameState(game: TapaGame) : CellsGameState<TapaGame, TapaGameMove, TapaGameState>(game) {
    var objArray: Array<TapaObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: TapaObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: TapaObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TapaGameMove): Boolean {
        val p: Position = move.p
        val objOld: TapaObject? = get(p)
        val objNew: TapaObject = move.obj
        if (objOld is TapaHintObject || objOld == objNew) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TapaGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<TapaObject, TapaObject> = label@ F<TapaObject, TapaObject> { obj: TapaObject? ->
            if (obj is TapaEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TapaMarkerObject() else TapaWallObject()
            if (obj is TapaWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) TapaMarkerObject() else TapaEmptyObject()
            if (obj is TapaMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TapaWallObject() else TapaEmptyObject()
            obj
        }
        move.obj = f.f(get(move.p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Tapa

        Summary
        Turkish art of PAint(TAPA)

        Description
        1. The goal is to fill some tiles forming a single orthogonally continuous
           path. Just like Nurikabe.
        2. A number indicates how many of the surrounding tiles are filled. If a
           tile has more than one number, it hints at multiple separated groups
           of filled tiles.
        3. For example, a cell with a 1 and 3 means there is a continuous group
           of 3 filled cells around it and one more single filled cell, separated
           from the other 3. The order of the numbers in this case is irrelevant.
        4. Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
           Tiles with numbers can be considered 'empty'.

        Variations
        5. Tapa has plenty of variations. Some are available in the levels of this
           game. Stronger variations are B-W Tapa, Island Tapa and Pata and have
           their own game.
        6. Equal Tapa - The board contains an equal number of white and black tiles.
           Tiles with numbers or question marks are NOT counted as empty or filled
           for this rule (i.e. they're left out of the count).
        7. Four-Me-Tapa - Four-Me-Not rule apply: you can't have more than three
           filled tiles in line.
        8. No Square Tapa - No 2*2 area of the board can be left empty.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. A number indicates how many of the surrounding tiles are filled. If a
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
                val p2 = p.add(TapaGame.offset.get(i!!))
                isValid(p2) && get(p2) is TapaWallObject
            }).toJavaList()
            val arr: List<Int> = computeHint.f(filled)
            val s: HintState = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible.f(arr, arr2)) HintState.Complete else HintState.Error
            val o = TapaHintObject()
            o.state = s
            set(p, o)
            if (s != HintState.Complete) isSolved = false
        })
        if (!isSolved) return
        // 4. Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            if (fj.data.Array.array<Position>(*TapaGame.offset2).forall(F<Position, Boolean> { os: Position? ->
                    val o: TapaObject? = get(p.add(os))
                    o is TapaWallObject
                })) {
                isSolved = false
                return
            }
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            if (get(p) is TapaWallObject) {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (os in TapaGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        // 1. The goal is to fill some tiles forming a single orthogonally continuous
        // path. Just like Nurikabe.
        g.setRootNode(fj.data.List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls<TapaObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = TapaEmptyObject()
        for (p in game.pos2hint.keys) set(p, TapaHintObject())
    }
}