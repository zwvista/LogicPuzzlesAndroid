package com.zwstudio.logicpuzzlesandroid.puzzles.floorplan

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FloorPlanGameState(game: FloorPlanGame) : CellsGameState<FloorPlanGame, FloorPlanGameMove, FloorPlanGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FloorPlanGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != FloorPlanGame.PUZ_EMPTY || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FloorPlanGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != FloorPlanGame.PUZ_EMPTY) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            FloorPlanGame.PUZ_EMPTY -> if (markerOption == MarkerOptions.MarkerFirst) FloorPlanGame.PUZ_MARKER else 1
            FloorPlanGame.PUZ_MARKER -> if (markerOption == MarkerOptions.MarkerFirst) 1 else FloorPlanGame.PUZ_EMPTY
            4 -> if (markerOption == MarkerOptions.MarkerLast) FloorPlanGame.PUZ_MARKER else FloorPlanGame.PUZ_EMPTY
            else -> o + 1
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Floor Plan

        Summary
        Blueprints to fill in

        Description
        1. The board represents a blueprint of an office floor.
        2. Cells with a number represent an office. On the floor every office is
           interconnected and can be reached by every other office.
        3. The number on a cell indicates how many offices it connects to. No two
           offices with the same number can be adjacent.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                pos2state[p] = HintState.Normal
                if (this[p] == FloorPlanGame.PUZ_FORBIDDEN)
                    this[p] = FloorPlanGame.PUZ_EMPTY
            }
        // 2. Cells with a number represent an office. On the floor every office is
        //    interconnected and can be reached by every other office.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val n2 = this[p]
                if (n2 <= 0) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                // 3. The number on a cell indicates how many offices it connects to. No two
                //    offices with the same number can be adjacent.
                val rng = FloorPlanGame.offset.map { p + it }.filter { isValid(it) }
                val rng2 = rng.filter { this[it] == n2 }
                if (rng2.isNotEmpty()) {
                    isSolved = false
                    pos2state[p] = HintState.Error
                    for (p2 in rng2)
                        pos2state[p2] = HintState.Error
                }
                if (pos2state[p] == HintState.Error) continue
                val n1 = rng.filter { this[it] > 0 }.size
                val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                if (s != HintState.Complete) isSolved = false
                pos2state[p] = s
                if (allowedObjectsOnly && s != HintState.Normal)
                    rng.filter { this[it] == FloorPlanGame.PUZ_EMPTY }.forEach {
                        this[it] = FloorPlanGame.PUZ_FORBIDDEN
                    }
            }
        if (!isSolved) return
        for ((p, node) in pos2node)
            for (i in 0..<4) {
                val p2 = p + FloorPlanGame.offset[i]
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}