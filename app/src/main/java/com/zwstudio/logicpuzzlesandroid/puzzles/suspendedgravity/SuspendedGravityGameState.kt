package com.zwstudio.logicpuzzlesandroid.puzzles.suspendedgravity

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SuspendedGravityGameState(game: SuspendedGravityGame) : CellsGameState<SuspendedGravityGame, SuspendedGravityGameMove, SuspendedGravityGameState>(game) {
    var objArray = Array(rows * cols) { SuspendedGravityObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: SuspendedGravityObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: SuspendedGravityObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: SuspendedGravityGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SuspendedGravityGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            SuspendedGravityObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) SuspendedGravityObject.Marker else SuspendedGravityObject.Stone
            SuspendedGravityObject.Stone -> if (markerOption == MarkerOptions.MarkerLast) SuspendedGravityObject.Marker else SuspendedGravityObject.Empty
            SuspendedGravityObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) SuspendedGravityObject.Stone else SuspendedGravityObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Suspended Gravity

        Summary
        Falling Blocks

        Description
        1. Each region contains the number of stones, which can be indicated by a number.
        2. Regions without a number contain at least one stone.
        3. Stones inside a region are all connected either vertically or horizontally.
        4. Stones in two adjacent regions cannot touch horizontally or vertically.
        5. Lastly, if we apply gravity to the puzzle and the stones fall down to
           the bottom of the board they fit together exactly and cover the bottom
           half of the board.
        6. Think "Tetris": all the blocks will fall as they are
           (they won't break into single stones)
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        // 3. Stones inside a region are all connected either vertically or horizontally.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (this[p]) {
                    SuspendedGravityObject.Forbidden ->
                        this[p] = SuspendedGravityObject.Empty
                    SuspendedGravityObject.Stone -> {
                        pos2stateAllowed[p] = AllowedObjectState.Normal
                        val node = Node(p.toString())
                        g.addNode(node)
                        pos2node[p] = node
                    }
                    else -> {}
                }
                if (game.pos2hint.contains(p))
                    pos2stateHint[p] = HintState.Normal
            }
        for (p in pos2node.keys)
            for (os in SuspendedGravityGame.offset) {
                val p2 = p + os
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        val area2blocks = mutableMapOf<Int, MutableList<List<Position>>>()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val block = pos2node.filter { nodeList.contains(it.value) }.keys.toList()
            for (p in block)
                pos2node.remove(p)
            // 4. Stones in two adjacent regions cannot touch horizontally or vertically.
            val nArea = game.pos2area[block[0]]!!
            if (!block.all {
                game.pos2area[it] == nArea
            }) {
                for (p in block)
                    pos2stateAllowed[p] = AllowedObjectState.Error
                isSolved = false
                continue
            }
            area2blocks.getOrPut(nArea) { mutableListOf() }.add(block)
        }
        // 3. Stones inside a region are all connected either vertically or horizontally.
        for (nArea in game.areas.indices) {
            // 2. Regions without a number contain at least one stone.
            val blocks = area2blocks[nArea]
            if (blocks == null) { isSolved = false; continue }
            if (blocks.size != 1) {
                for (block in blocks)
                    for (p in block)
                        pos2stateAllowed[p] = AllowedObjectState.Error
                isSolved = false
            }
            // 4. Stones in two adjacent regions cannot touch horizontally or vertically.
            if (allowedObjectsOnly) {
                val rng = blocks.flatMap { it }
                    .flatMap { p -> SuspendedGravityGame.offset.map { p + it } }
                    .filter { isValid(it) && this[it] == SuspendedGravityObject.Empty && game.pos2area[it] != nArea }
                for (p in rng)
                    this[p] = SuspendedGravityObject.Forbidden
            }
            // 1. Each region contains the number of stones, which can be indicated by a number.
            val n1 = blocks.fold(0) { acc, block -> acc + block.size }
            val pHint = game.area2hint[nArea] ?: continue
            val n2 = game.pos2hint[pHint]!!
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2stateHint[pHint] = s
        }
        if (!isSolved) return
        // 5. Lastly, if we apply gravity to the puzzle and the stones fall down to
        //    the bottom of the board they fit together exactly and cover the bottom
        //    half of the board.
        // 6. Think "Tetris": all the blocks will fall as they are
        //    (they won't break into single stones)
        val objArrayTemp = objArray.copyOf()

        // key: index of the area
        // value.elem: position of the stone
        val area2stones = mutableMapOf<Int, MutableList<Position>>()
        // key: index of the area where stones should fall later
        // value.elem: index of the area where stones should fall sooner
        val area2areas = mutableMapOf<Int, MutableList<Int>>()
        for (c in 0..<cols) {
            var n1 = -1
            for (r in 0..<rows) {
                val p = Position(r, c)
                if (this[p] != SuspendedGravityObject.Stone) continue
                val n2 = game.pos2area[p]!!
                area2stones.getOrPut(n2) { mutableListOf() }.add(p)
                if (n1 == -1)
                    n1 = n2
                else if (n1 != n2) {
                    area2areas.getOrPut(n1) { mutableListOf() }.add(n2)
                    n1 = n2
                }
            }
        }

        // make the stones fall down
        while (area2stones.isNotEmpty())
            for ((i, stones) in area2stones) {
                if (area2areas.contains(i)) continue

                var j = 0
                while (true) {
                    if (!stones.all {
                        val p2 = it + Position(j + 1, 0)
                        stones.contains(p2) || isValid(p2) && this[p2] != SuspendedGravityObject.Stone
                    }) break
                    j++
                }

                if (j > 0) {
                    for (p in stones)
                        this[p] = SuspendedGravityObject.Empty
                    for (p in stones)
                        this[p + Position(j, 0)] = SuspendedGravityObject.Stone
                }

                for ((area, areas) in area2areas) {
                    val areas = areas.filter { it != i }.toMutableList()
                    if (areas.isEmpty())
                        area2stones.remove(area)
                    else
                        area2areas[area] = areas
                }
                area2stones.remove(i)
                break;
            }

        if (!run {
            // After falling down, they fit together exactly and
            // cover the bottom half of the board.
            for (c in 0..<cols) {
                var r = 0
                while (r < rows / 2) {
                    if (this[r, c] == SuspendedGravityObject.Stone)
                        return@run false
                    r++
                }
                while (r < rows) {
                    if (this[r, c] != SuspendedGravityObject.Stone)
                        return@run false
                    r++
                }
            }
            return@run true
        }) isSolved = false

        objArray = objArrayTemp.copyOf()
    }
}