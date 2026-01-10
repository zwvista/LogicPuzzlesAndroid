package com.zwstudio.logicpuzzlesandroid.puzzles.trebuchet

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.GardenerGame

class TrebuchetGameState(game: TrebuchetGame) : CellsGameState<TrebuchetGame, TrebuchetGameMove, TrebuchetGameState>(game) {
    var objArray = Array<TrebuchetObject>(rows * cols) { TrebuchetEmptyObject }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TrebuchetObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TrebuchetObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = TrebuchetHintObject()
        updateIsSolved()
    }

    override fun setObject(move: TrebuchetGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TrebuchetGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is TrebuchetEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) TrebuchetMarkerObject else TrebuchetTargetObject()
            is TrebuchetTargetObject -> if (markerOption == MarkerOptions.MarkerLast) TrebuchetMarkerObject else TrebuchetEmptyObject
            is TrebuchetMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) TrebuchetTargetObject() else TrebuchetEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Trebuchet

        Summary
        Fire!

        Description
        1. On the board you can see some trebuchets.
        2. The number on a Trebuchet indicates the distance it shoots. Only one of
           the four directions can be marked with a target, the others should be empty.
        3. Two target cells must not be orthogonally adjacent.
        4. All of the non-targeted cells must be connected.
        5. Please note you can't target other trebuchets (yes it's a pointless war maybe)
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is TrebuchetForbiddenObject)
                    this[r, c] = TrebuchetEmptyObject
        // 3. Two target cells must not be orthogonally adjacent.
        val targets = mutableSetOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] !is TrebuchetTargetObject) continue
                targets.add(p)
                for (os in TrebuchetGame.offset) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    if (this[p2] is TrebuchetTargetObject) {
                        isSolved = false
                        this[p] = TrebuchetTargetObject(AllowedObjectState.Error)
                        this[p2] = TrebuchetTargetObject(AllowedObjectState.Error)
                    } else if (allowedObjectsOnly && this[p2] is TrebuchetEmptyObject)
                        this[p2] = TrebuchetForbiddenObject
                }
            }
        // 4. All of the non-targeted cells must be connected.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] is TrebuchetTargetObject) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node) {
            for (os in GardenerGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        // 2. The number on a Trebuchet indicates the distance it shoots. Only one of
        //    the four directions can be marked with a target, the others should be empty.
        // 5. Please note you can't target other trebuchets (yes it's a pointless war maybe)
        for ((p, _) in game.pos2hint) {
            val possibleTargets = game.pos2targets[p]!!
            val realTargets = possibleTargets.filter { this[it] is TrebuchetTargetObject }
            val emptyTargets = possibleTargets.filter { this[it] is TrebuchetEmptyObject }
            val n1 = realTargets.size
            val s: HintState = if (n1 < 1) HintState.Normal else if (n1 == 1) HintState.Complete else HintState.Error
            if (s != HintState.Complete) {
                isSolved = false
                for (p2 in realTargets)
                    this[p2] = TrebuchetTargetObject(AllowedObjectState.Error)
            }
            this[p] = TrebuchetHintObject(state = s)
            for (p2 in realTargets)
                targets.remove(p2)
            if (allowedObjectsOnly && s != HintState.Normal)
                for (p2 in emptyTargets)
                    this[p2] = TrebuchetForbiddenObject
        }
        for (p in targets)
            this[p] = TrebuchetTargetObject(AllowedObjectState.Error)
    }
}