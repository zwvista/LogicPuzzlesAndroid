package com.zwstudio.logicpuzzlesandroid.puzzles.planks

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PlanksGameState(game: PlanksGame) : CellsGameState<PlanksGame, PlanksGameMove, PlanksGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: PlanksGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + PlanksGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PlanksGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val o = this[move.p][move.dir]
        move.obj = when (o) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 16/Planks

        Summary
        Planks and Nails

        Description
        1. On the board there are a few nails. Each one nails a plank to
           the board.
        2. Planks are 3 tiles long and can be oriented vertically or
           horizontally. The Nail can be in any of the 3 tiles.
        3. Each Plank touches orthogonally exactly two other Planks.
        4. All the Planks form a ring, or a closed loop.
    */
    private fun updateIsSolved() {
        isSolved = true
//        // 2. Each number in a tile tells you on how many of its four sides are touched
//        // by the path.
//        for ((p, n2) in game.pos2hint) {
//            var n1 = 0
//            if (this[p][1] == GridLineObject.Line) n1++
//            if (this[p][2] == GridLineObject.Line) n1++
//            if (this[p + Position(1, 1)][0] == GridLineObject.Line) n1++
//            if (this[p + Position(1, 1)][3] == GridLineObject.Line) n1++
//            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
//            if (n1 != n2) isSolved = false
//        }
//        if (!isSolved) return
//        val g = Graph()
//        val pos2node = mutableMapOf<Position, Node>()
//        for (r in 0 until rows)
//            for (c in 0 until cols) {
//                val p = Position(r, c)
//                val n = this[p].filter { it == GridLineObject.Line }.size
//                when (n) {
//                    0 -> {}
//                    2 -> {
//                        val node = Node(p.toString())
//                        g.addNode(node)
//                        pos2node[p] = node
//                    }
//                    else -> {
//                        // 1. The path cannot have branches or cross itself.
//                        isSolved = false
//                        return
//                    }
//                }
//            }
//        for (p in pos2node.keys) {
//            val dotObj = this[p]
//            for (i in 0 until 4) {
//                if (dotObj[i] != GridLineObject.Line) continue
//                val p2 = p + PlanksGame.offset[i]
//                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
//            }
//        }
//        // 1. Draw a single looping path with the aid of the numbered hints.
//        g.rootNode = pos2node.values.first()
//        val nodeList = g.bfs()
//        if (nodeList.size != pos2node.size) isSolved = false
    }
}