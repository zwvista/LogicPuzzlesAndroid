package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domainimport

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class TataminoGameState(game: TataminoGame) : CellsGameState<TataminoGame, TataminoGameMove, TataminoGameState>(game) {
    var cloner = Cloner()
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

    fun setObject(move: TataminoGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: TataminoGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return false
        val o = this[p]
        move.obj = if (o == ' ') '1' else if (o == '3') ' ' else o + 1
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Tatamino

        Summary
        Which is a little Tatami

        Description
        1. Plays like Fillomino, in which you have to guess areas on the board
           marked by their number.
        2. In Tatamino, however, you only have areas 1, 2 and 3 tiles big.
        3. Please remember two areas of the same number size can't be touching.
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
                for (os in TataminoGame.offset) {
                    val p2 = p.add(os)
                    if (isValid(p2) && this[p2] == ch)
                        g.connectNode(pos2node[p], pos2node[p2])
                }
            }
        dots = cloner.deepClone(game.dots)
        while (pos2node.isNotEmpty()) {
            g.setRootNode(pos2node.values.first())
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
                    val p2 = p.add(TataminoGame.offset[i])
                    val ch2 = if (!isValid(p2)) '.' else this[p2]
                    if (ch2 != ch && (n1 <= n2 || ch2 != ' '))
                        dots[p.add(TataminoGame.offset2[i]), TataminoGame.dirs[i]] = GridLineObject.Line
                }
            }
            if (s != HintState.Complete) isSolved = false
        }
    }
}