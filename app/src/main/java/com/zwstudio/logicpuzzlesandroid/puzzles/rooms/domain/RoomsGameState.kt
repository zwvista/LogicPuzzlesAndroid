package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class RoomsGameState(game: RoomsGame) : CellsGameState<RoomsGame, RoomsGameMove, RoomsGameState>(game) {
    var objArray = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    fun setObject(move: RoomsGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + RoomsGame.offset[dir]
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: RoomsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
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
        iOS Game: Logic Games/Puzzle Set 5/Rooms

        Summary
        Close the doors between Rooms

        Description
        1. The view of the board is a castle with every tile identifying a Room.
           Between Rooms there are doors that can be open or closed. At the start
           of the game all doors are open.
        2. Each number inside a Room tells you how many other Rooms you see from
           there, in a straight line horizontally or vertically when the appropriate
           doors are closed.
        3. At the end of the solution, each Room must be reachable from the others.
           That means no single Room or group of Rooms can be divided by the others.
        4. In harder levels some tiles won't tell you how many Rooms are visible
           at all.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. Each number inside a Room tells you how many other Rooms you see from
        // there, in a straight line horizontally or vertically when the appropriate
        // doors are closed.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            for (i in 0 until 4) {
                var p2 = +p
                while (this[p2 + RoomsGame.offset2[i]][RoomsGame.dirs[i]] != GridLineObject.Line) {
                    n1++
                    p2 += RoomsGame.offset[i]
                }
            }
            pos2state[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        val rng = mutableSetOf<Position>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                rng.add(+p)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                for (i in 0 until 4)
                    if (this[p + RoomsGame.offset2[i]][RoomsGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + RoomsGame.offset[i]]!!)
            }
        // 3. At the end of the solution, each Room must be reachable from the others.
        // That means no single Room or group of Rooms can be divided by the others.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}