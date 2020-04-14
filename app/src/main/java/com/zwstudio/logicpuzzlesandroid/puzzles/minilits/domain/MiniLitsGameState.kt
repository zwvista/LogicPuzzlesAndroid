package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.data.Stream
import java.util.*

class MiniLitsGameState(game: MiniLitsGame?) : CellsGameState<MiniLitsGame?, MiniLitsGameMove?, MiniLitsGameState?>(game) {
    var objArray: Array<MiniLitsObject?>
    var pos2state: Map<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: MiniLitsObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: MiniLitsObject?) {
        set(p!!.row, p.col, obj)
    }

    private inner class MiniLitsAreaInfo {
        var trees = mutableListOf<Position>()
        var blockIndexes = mutableSetOf<Int>()
        var neighborIndexes = mutableSetOf<Int>()
        var tetrominoIndex = -1
    }

    fun setObject(move: MiniLitsGameMove): Boolean {
        if (!isValid(move.p) || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: MiniLitsGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<MiniLitsObject, MiniLitsObject> = label@ F<MiniLitsObject, MiniLitsObject> { obj: MiniLitsObject? ->
            if (obj is MiniLitsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) MiniLitsMarkerObject() else MiniLitsTreeObject()
            if (obj is MiniLitsTreeObject) return@label if (markerOption == MarkerOptions.MarkerLast) MiniLitsMarkerObject() else MiniLitsEmptyObject()
            if (obj is MiniLitsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) MiniLitsTreeObject() else MiniLitsEmptyObject()
            obj
        }
        val o: MiniLitsObject? = get(move.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: MiniLitsObject? = get(p)
            if (o is MiniLitsForbiddenObject) set(r, c, MiniLitsEmptyObject()) else if (o is MiniLitsTreeObject) {
                o.state = AllowedObjectState.Normal
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (os in MiniLitsGame.Companion.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        val blocks = mutableListOf<List<Position>>()
        while (!pos2node.isEmpty()) {
            g.setRootNode(fj.data.List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val block = fj.data.HashMap.fromMap(pos2node).toStream().filter(F<P2<Position, Node>, Boolean> { e: P2<Position?, Node?> -> nodeList.contains(e._2()) }).map<Position>(F<P2<Position, Node>, Position> { e: P2<Position?, Node?> -> e._1() }).toJavaList()
            blocks.add(block)
            for (p in block) pos2node.remove(p)
        }
        // 4. All the shaded cells should form a valid Nurikabe.
        if (blocks.size != 1) isSolved = false
        val infos = Stream.range(0, game.areas.size.toLong()).map<MiniLitsAreaInfo>(F<Int, MiniLitsAreaInfo> { i: Int? -> MiniLitsAreaInfo() }).toJavaList()
        for (i in blocks.indices) {
            val block = blocks[i]
            for (p in block) {
                val n: Int = game.pos2area.get(p)
                val info = infos[n]
                info.trees.add(p)
                info.blockIndexes.add(i)
            }
        }
        for (i in infos.indices) {
            val info = infos[i]
            for (p in info.trees) for (os in MiniLitsGame.Companion.offset) {
                val p2 = p.add(os)
                val index = fj.data.List.iterableList(infos).toStream().indexOf(F<MiniLitsAreaInfo, Boolean> { info2: MiniLitsAreaInfo -> info2.trees.contains(p2) })
                if (index.isSome && index.some() != i) info.neighborIndexes.add(index.some())
            }
        }
        val notSolved: Effect1<MiniLitsAreaInfo> = Effect1<MiniLitsAreaInfo> { info: MiniLitsAreaInfo ->
            isSolved = false
            for (p in info.trees) {
                val o: MiniLitsTreeObject? = get(p) as MiniLitsTreeObject?
                o!!.state = AllowedObjectState.Error
            }
        }
        for (i in infos.indices) {
            val info = infos[i]
            val treeCount = info.trees.size
            if (treeCount >= 3 && allowedObjectsOnly) for (p in game.areas.get(i)) {
                val o: MiniLitsObject? = get(p)
                if (o is MiniLitsEmptyObject || o is MiniLitsMarkerObject) set(p, MiniLitsForbiddenObject())
            }
            if (treeCount > 3 || treeCount == 3 && info.blockIndexes.size > 1) notSolved.f(info)
            // 2. The board is divided into many areas. You have to place a triomino
            // into each area.
            if (treeCount == 3 && info.blockIndexes.size == 1) {
                Collections.sort(info.trees) { obj: Position, other: Position? -> obj.compareTo(other) }
                val treeOffsets = mutableListOf<Position>()
                val p2 = Position(fj.data.List.iterableList(info.trees).map<Int>(F<Position, Int> { p: Position -> p.row }).minimum(Ord.intOrd),
                    fj.data.List.iterableList(info.trees).map<Int>(F<Position, Int> { p: Position -> p.col }).minimum(Ord.intOrd))
                for (p in info.trees) treeOffsets.add(p.subtract(p2))
                info.tetrominoIndex = fj.data.Array.array<Array<Position>>(*MiniLitsGame.Companion.triominos).toStream()
                    .indexOf(F<Array<Position>, Boolean> { arr: Array<Position?>? -> Arrays.equals(arr, treeOffsets.toTypedArray()) }).orSome(-1)
                if (info.tetrominoIndex == -1) notSolved.f(info)
            }
            if (treeCount < 3) isSolved = false
        }
        // 3. No two adjacent (touching horizontally / vertically) triominos should
        // be of equal shape & orientation.
        for (i in infos.indices) {
            val info = infos[i]
            val index = info.tetrominoIndex
            if (index == -1) continue
            if (fj.data.List.iterableList(info.neighborIndexes).exists(F<Int, Boolean> { j: Int? -> infos[j!!].tetrominoIndex == index })) notSolved.f(info)
        }
        if (!isSolved) return
        // 4. All the shaded cells should form a valid Nurikabe.
        val block = blocks[0]
        rule2x2@ for (p in block) {
            for (os in MiniLitsGame.Companion.offset3) if (block.contains(p.add(os))) continue@rule2x2
            isSolved = false
            for (os in MiniLitsGame.Companion.offset3) {
                val o = MiniLitsTreeObject()
                o.state = AllowedObjectState.Error
                set(p.add(os), o)
            }
        }
    }

    init {
        objArray = arrayOfNulls<MiniLitsObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = MiniLitsEmptyObject()
        updateIsSolved()
    }
}