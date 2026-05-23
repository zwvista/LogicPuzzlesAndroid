package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.CloudsGame

class CloudsAndClearsGameState(game: CloudsAndClearsGame) : CellsGameState<CloudsAndClearsGame, CloudsAndClearsGameMove, CloudsAndClearsGameState>(game) {
    // https://stackoverflow.com/questions/43172947/kotlin-creating-a-mutable-list-with-repeating-elements
    val objArray = Array(rows * cols) { CloudsAndClearsObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CloudsAndClearsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: CloudsAndClearsObject) {this[p.row, p.col] = obj}

    override fun setObject(move: CloudsAndClearsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: CloudsAndClearsGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (this[p]) {
            CloudsAndClearsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) CloudsAndClearsObject.Marker else CloudsAndClearsObject.Cloud
            CloudsAndClearsObject.Cloud -> if (markerOption == MarkerOptions.MarkerLast) CloudsAndClearsObject.Marker else CloudsAndClearsObject.Empty
            CloudsAndClearsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) CloudsAndClearsObject.Cloud else CloudsAndClearsObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/Clouds and Clears

        Summary
        Holes in the sky

        Description
        1. Paint the clouds according to the numbers.
        2. Each cloud or empty Sky move contains a single number that is the extension of the region
           itself.
        3. On a region there can be other numbers. These will indicate how many empty (non-cloud) tiles
           around it (diagonal too) including itself.
    */
    private fun updateIsSolved() {
        isSolved = true
        val clouds = mutableListOf<List<Position>>()
        val empties = mutableListOf<List<Position>>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (os in CloudsGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let {
                    if (this[p2].isCloud == this[p].isCloud)
                        g.connectNode(node, it)
                }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.keys.toList()
            if (this[area[0]].isCloud)
                clouds.add(area)
            else
                empties.add(area)
            for (p in area)
                pos2node.remove(p)
        }
        // 2. Each cloud or empty Sky move contains a single number that is the extension of the region
        //    itself.
        // 3. On a region there can be other numbers. These will indicate how many empty (non-cloud) tiles
        //    around it (diagonal too) including itself.
        for ((p, n2) in game.pos2hint) {
            val area = clouds.firstOrNull { it.contains(p) } ?: empties.first { it.contains(p) }
            val n3 = area.size
            val n1 = CloudsAndClearsGame.offset2.count {
                val p2 = p + it
                isValid(p2) && !this[p2].isCloud
            }
            val s = if (n1 == n2 || n3 == n2) HintState.Complete else if (n1 > n2) HintState.Normal else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}
