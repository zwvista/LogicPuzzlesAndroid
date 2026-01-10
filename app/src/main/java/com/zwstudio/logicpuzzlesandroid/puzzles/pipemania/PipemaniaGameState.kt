package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PipemaniaGameState(game: PipemaniaGame) : CellsGameState<PipemaniaGame, PipemaniaGameMove, PipemaniaGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: PipemaniaObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: PipemaniaObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PipemaniaGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] !is PipemaniaEmptyObject || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PipemaniaGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] !is PipemaniaEmptyObject) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is PipemaniaEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) PipemaniaMarkerObject else PipemaniaFlowerObject()
            is PipemaniaFlowerObject -> if (markerOption == MarkerOptions.MarkerLast) PipemaniaMarkerObject else PipemaniaEmptyObject
            is PipemaniaMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) PipemaniaFlowerObject() else PipemaniaEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 5/Pipemania

        Summary
        Back to the 80s

        Description
        1. The former contractor for your present client left the work unfinished.
           In order not to waste what has bee done, you should complete the pipe
           loop, using the pieces available.
        2. Complete the board using all the tiles and form a single closed loop.
        3. The loop can cross itself.
        4. please note “a single closed loop" means that assuming the flow is straight
           even when the pipe crosses itself, i.e. following the pipe in straight lines
           (not turning at crossings).
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is PipemaniaForbiddenObject)
                    this[p] = PipemaniaEmptyObject
                else if (o is PipemaniaFlowerObject) {
                    o.state = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in PipemaniaGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        }
        // 2. More exactly, you have to join the existing flowers by adding more of
        // them, creating a single path of flowers touching horizontally or
        // vertically.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        val flowers = mutableListOf<Position>()
        // 3. At the same time, you can't line up horizontally or vertically more
        // than 3 flowers (thus Forbidden Four).
        fun areFlowersInvalid() = flowers.size > 3
        fun checkFlowers() {
            if (areFlowersInvalid()) {
                isSolved = false
                for (p in flowers)
                    (this[p] as PipemaniaFlowerObject).state = AllowedObjectState.Error
            }
            flowers.clear()
        }
        fun checkForbidden(p: Position, indexes: List<Int>) {
            if (!allowedObjectsOnly) return
            for (i in indexes) {
                val os = PipemaniaGame.offset[i]
                var p2 = p + os
                while (isValid(p2) && this[p2] is PipemaniaFlowerObject) {
                    flowers.add(p2)
                    p2 += os
                }
            }
            if (areFlowersInvalid()) this[p] = PipemaniaForbiddenObject
            flowers.clear()
        }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is PipemaniaFlowerObject)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o is PipemaniaEmptyObject || o is PipemaniaMarkerObject)
                        checkForbidden(p, listOf(1, 3))
                }
            }
            checkFlowers()
        }
        for (c in 0 until cols) {
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = get(p)
                if (o is PipemaniaFlowerObject)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o is PipemaniaEmptyObject || o is PipemaniaMarkerObject)
                        checkForbidden(p, listOf(0, 2))
                }
            }
            checkFlowers()
        }
    }
}