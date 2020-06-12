package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class FillominoGameState(game: FillominoGame) : CellsGameState<FillominoGame, FillominoGameMove, FillominoGameState>(game) {
    protected var cloner = Cloner()
    var objArray = game.objArray.copyOf()
    lateinit var dots: GridDots
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: FillominoGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: FillominoGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return false
        val o = this[p]
        move.obj = if (o == ' ') '1' else if (o == game.chMax) ' ' else (o.toInt() + 1).toChar()
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/Fillomino

        Summary
        Detect areas marked by their extension

        Description
        1. The goal is to detect areas marked with the tile count of the area
           itself.
        2. So for example, areas marked '1', will always consist of one single
           tile. Areas marked with '2' will consist of two (horizontally or
           vertically) adjacent tiles. Tiles numbered '3' will appear in a group
           of three and so on.
        3. Some areas can also be totally hidden at the start.

        Variation
        4. Fillomino has several variants.
        5. No Rectangles: Areas can't form Rectangles.
        6. Only Rectangles: Areas can ONLY form Rectangles.
        7. Non Consecutive: Areas can't touch another area which has +1 or -1
           as number (orthogonally).
        8. Consecutive: Areas MUST touch another area which has +1 or -1
           as number (orthogonally).
        9. All Odds: There are only odd numbers on the board.
        10.All Evens: There are only even numbers on the board.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (this[p] == ' ')
                    isSolved = false
                else {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = this[p]
                if (ch == ' ') continue
                for (os in FillominoGame.offset) {
                    val p2 = p.add(os)
                    if (isValid(p2) && this[p2] == ch)
                        g.connectNode(pos2node[p]!!, pos2node[p2]!!)
                }
            }
        dots = cloner.deepClone(game.dots)
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val ch = this[area[0]]
            val n1 = area.size
            val n2 = ch - '0'
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            for (p in area) {
                pos2state[p] = s
                for (i in 0..3) {
                    val p2 = p.add(FillominoGame.offset[i])
                    val ch2 = if (!isValid(p2)) '.' else this[p2]
                    if (ch2 != ch && (n1 <= n2 || ch2 != ' '))
                        dots[p.add(FillominoGame.offset2[i]), FillominoGame.dirs[i]] = GridLineObject.Line
                }
            }
            if (s != HintState.Complete) isSolved = false
        }
    }
}