package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.data.List
import java.util.*

class RoomsGameState(game: RoomsGame) : CellsGameState<RoomsGame, RoomsGameMove, RoomsGameState>(game) {
    var objArray: Array<Array<GridLineObject?>>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    fun setObject(move: RoomsGameMove): Boolean {
        val p1: Position = move.p
        val dir: Int = move.dir
        val dir2 = (dir + 2) % 4
        val o: GridLineObject? = get(p1)[dir]
        if (o == move.obj) return false
        val p2 = p1.add(RoomsGame.offset.get(dir))
        get(p1)[dir] = move.obj
        get(p2)[dir2] = get(p1)[dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: RoomsGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<GridLineObject, GridLineObject> = label@ F<GridLineObject, GridLineObject> { obj: GridLineObject? ->
            when (obj) {
                GridLineObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
                GridLineObject.Line -> return@label if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
                GridLineObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            }
            obj
        }
        val dotObj: Array<GridLineObject?> = get(move.p)
        move.obj = f.f(dotObj[move.dir])
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
        for ((p, n2) in game.pos2hint.entries) {
            var n1 = 0
            for (i in 0..3) {
                val p2 = p.plus()
                while (get(p2.add(RoomsGame.offset2.get(i)))[RoomsGame.dirs.get(i)] != GridLineObject.Line) {
                    n1++
                    p2.addBy(RoomsGame.offset.get(i))
                }
            }
            pos2state[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        val rng = mutableSetOf<Position>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            rng.add(p.plus())
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
        }
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            for (i in 0..3) if (get(p.add(RoomsGame.offset2.get(i)))[RoomsGame.dirs.get(i)] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(ParksGame.offset.get(i))])
        }
        // 3. At the end of the solution, each Room must be reachable from the others.
        // That means no single Room or group of Rooms can be divided by the others.
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = Cloner().deepClone(game.objArray)
        updateIsSolved()
    }
}