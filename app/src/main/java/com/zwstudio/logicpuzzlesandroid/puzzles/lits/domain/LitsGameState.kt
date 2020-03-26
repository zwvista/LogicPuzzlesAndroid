package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.Ord
import fj.P2
import fj.data.Stream
import fj.function.Effect1
import java.util.*

class LitsGameState(game: LitsGame?) : CellsGameState<LitsGame?, LitsGameMove?, LitsGameState?>(game) {
    var objArray: Array<LitsObject?>
    var pos2state: Map<Position?, HintState?> = HashMap()
    operator fun get(row: Int, col: Int): LitsObject? {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): LitsObject? {
        return get(p!!.row, p.col)
    }

    operator fun set(row: Int, col: Int, dotObj: LitsObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position?, obj: LitsObject?) {
        set(p!!.row, p.col, obj)
    }

    private inner class LitsAreaInfo {
        var trees: MutableList<Position> = ArrayList()
        var blockIndexes: MutableSet<Int> = HashSet()
        var neighborIndexes: MutableSet<Int> = HashSet()
        var tetrominoIndex = -1
    }

    fun setObject(move: LitsGameMove?): Boolean {
        if (!isValid(move!!.p) || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: LitsGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: LitsObject? ->
            if (obj is LitsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) LitsMarkerObject() else LitsTreeObject()
            if (obj is LitsTreeObject) return@label if (markerOption == MarkerOptions.MarkerLast) LitsMarkerObject() else LitsEmptyObject()
            if (obj is LitsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) LitsTreeObject() else LitsEmptyObject()
            obj
        }
        val o = get(move!!.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly = game!!.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node: MutableMap<Position, Node> = HashMap()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o = get(p)
            if (o is LitsForbiddenObject) set(r, c, LitsEmptyObject()) else if (o is LitsTreeObject) {
                o.state = AllowedObjectState.Normal
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (os in LitsGame.Companion.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        val blocks: MutableList<List<Position>> = ArrayList()
        while (!pos2node.isEmpty()) {
            g.setRootNode(fj.data.List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val block = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            blocks.add(block)
            for (p in block) pos2node.remove(p)
        }
        // 5. All the shaded cells should form a valid Nurikabe.
        if (blocks.size != 1) isSolved = false
        val infos = Stream.range(0, game!!.areas.size.toLong()).map { i: Int? -> LitsAreaInfo() }.toJavaList()
        for (i in blocks.indices) {
            val block = blocks[i]
            for (p in block) {
                val n = game!!.pos2area[p]
                val info = infos[n!!]
                info.trees.add(p)
                info.blockIndexes.add(i)
            }
        }
        for (i in infos.indices) {
            val info = infos[i]
            for (p in info.trees) for (os in LitsGame.Companion.offset) {
                val p2 = p.add(os)
                val index = fj.data.List.iterableList(infos).toStream().indexOf { info2: LitsAreaInfo -> info2.trees.contains(p2) }
                if (index.isSome && index.some() != i) info.neighborIndexes.add(index.some())
            }
        }
        val notSolved = Effect1 { info: LitsAreaInfo ->
            isSolved = false
            for (p in info.trees) {
                val o = get(p) as LitsTreeObject?
                o!!.state = AllowedObjectState.Error
            }
        }
        for (i in infos.indices) {
            val info = infos[i]
            val treeCount = info.trees.size
            if (treeCount >= 4 && allowedObjectsOnly) for (p in game!!.areas[i]) {
                val o = get(p)
                if (o is LitsEmptyObject || o is LitsMarkerObject) set(p, LitsForbiddenObject())
            }
            if (treeCount > 4 || treeCount == 4 && info.blockIndexes.size > 1) notSolved.f(info)
            // 3. The board is divided into many areas. You have to place a tetromino
            // into each area.
            if (treeCount == 4 && info.blockIndexes.size == 1) {
                Collections.sort(info.trees) { obj: Position, other: Position? -> obj.compareTo(other) }
                val treeOffsets: MutableList<Position> = ArrayList()
                val p2 = Position(fj.data.List.iterableList(info.trees).map { p: Position -> p.row }.minimum(Ord.intOrd),
                    fj.data.List.iterableList(info.trees).map { p: Position -> p.col }.minimum(Ord.intOrd))
                for (p in info.trees) treeOffsets.add(p.subtract(p2))
                info.tetrominoIndex = fj.data.Array.array<Array<Array<Position>>>(*LitsGame.Companion.tetrominoes).toStream()
                    .indexOf { arr: Array<Array<Position>> -> fj.data.Array.array(*arr).exists { arr2: Array<Position>? -> Arrays.equals(arr2, treeOffsets.toTypedArray()) } }.orSome(-1)
                if (info.tetrominoIndex == -1) notSolved.f(info)
            }
            if (treeCount < 4) isSolved = false
        }
        // 4. No two adjacent (touching horizontally / vertically) tetromino should
        // be of equal shape, even counting rotations or reflections.
        for (i in infos.indices) {
            val info = infos[i]
            val index = info.tetrominoIndex
            if (index == -1) continue
            if (fj.data.List.iterableList(info.neighborIndexes).exists { j: Int? -> infos[j!!].tetrominoIndex == index }) notSolved.f(info)
        }
        if (!isSolved) return
        val block = blocks[0]
        // 5. All the shaded cells should form a valid Nurikabe.
        rule2x2@ for (p in block) {
            for (os in LitsGame.Companion.offset3) if (block.contains(p.add(os))) continue@rule2x2
            isSolved = false
            for (os in LitsGame.Companion.offset3) {
                val o = LitsTreeObject()
                o.state = AllowedObjectState.Error
                set(p.add(os), o)
            }
        }
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) objArray[i] = LitsEmptyObject()
        updateIsSolved()
    }
}