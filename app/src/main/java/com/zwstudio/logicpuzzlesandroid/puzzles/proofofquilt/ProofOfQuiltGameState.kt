package com.zwstudio.logicpuzzlesandroid.puzzles.proofofquilt

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ProofOfQuiltGameState(game: ProofOfQuiltGame) : CellsGameState<ProofOfQuiltGame, ProofOfQuiltGameMove, ProofOfQuiltGameState>(game) {
    protected var cloner = Cloner()
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ProofOfQuiltObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ProofOfQuiltObject) {this[p.row, p.col] = obj}

    override fun setObject(move: ProofOfQuiltGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ProofOfQuiltObject.Empty || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ProofOfQuiltGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ProofOfQuiltObject.Empty) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            ProofOfQuiltObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) ProofOfQuiltObject.Marker else ProofOfQuiltObject.TriangleA
            ProofOfQuiltObject.TriangleA -> ProofOfQuiltObject.TriangleB
            ProofOfQuiltObject.TriangleB -> ProofOfQuiltObject.TriangleC
            ProofOfQuiltObject.TriangleC -> ProofOfQuiltObject.TriangleD
            ProofOfQuiltObject.TriangleD -> if (markerOption == MarkerOptions.MarkerLast) ProofOfQuiltObject.Marker else ProofOfQuiltObject.Empty
            ProofOfQuiltObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) ProofOfQuiltObject.TriangleA else ProofOfQuiltObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 8/Proof of Quilt

        Summary
        Quilt the board, following the hints

        Description
         1. The goal is to place triangles in some cells in the end generating a pattern
            similar to a Quilt.
         2. The numbered tiles tell you how many triangles share an edge with it,
            horizontally and vertically
         3, For example, if a tile says 4, it has triangles all around it.
         4. If a tile says 1, it has only one triangle somewhere.
         5. Some tiles will remain blank and will form, along with the triangles, rectangles
            and squares.
         6. These can be tilted by 45 degrees.
         7. Some other tiles are filled but contain no number. These and the hints are
            the only tiles that can be completely filled.
         8. Rectangles or squares can't touch orthogonally, but can touch diagonally
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val triangleAs = mutableSetOf<Position>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (this[p]) {
                    ProofOfQuiltObject.Forbidden -> this[p] = ProofOfQuiltObject.Empty
                    ProofOfQuiltObject.TriangleA -> triangleAs.add(p)
                    else -> {}
                }
            }
        for ((p, n2) in game.pos2hint) {
            val area = ProofOfQuiltGame.offset.map { p + it }.filter { isValid(it) }
            val n1 = area.count { this[it].isTriangle }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            if (allowedObjectsOnly && s != HintState.Normal)
                for (p2 in area)
                    if (this[p2] == ProofOfQuiltObject.Empty || this[p2] == ProofOfQuiltObject.Marker)
                        this[p2] = ProofOfQuiltObject.Forbidden
        }
        if (!isSolved) return
        val allPositions = cloner.deepClone(game.allPositions)
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (!this[p].isBlank) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (os in ProofOfQuiltGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val blanks = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            var (r1, r2) = rows to 0
            var (c1, c2) = cols to 0
            for (p in blanks) {
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
                pos2node.remove(p)
                allPositions.remove(p)
            }
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            if (rs * cs != blanks.size) { isSolved = false; return }
        }
        outer@ for (p in triangleAs) {
            for (o in game.patterns)
                if (p.row + o.len < rows && p.col + o.len < cols &&
                o.pattern.all { (dp, o2) ->
                    this[p + dp] == o2
                }) {
                    val area = o.pattern.map { p + it.key }
                    for (p2 in area)
                        allPositions.remove(p2)
                    continue@outer
                }
            isSolved = false; return
        }
        if (allPositions.isNotEmpty()) isSolved = false
    }
}
