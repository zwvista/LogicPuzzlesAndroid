package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domainimport

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.data.List
import java.util.*

class TataminoGameState(game: TataminoGame) : CellsGameState<TataminoGame?, TataminoGameMove?, TataminoGameState?>(game) {
    protected var cloner: Cloner = Cloner()
    var objArray: CharArray
    var dots: GridDots? = null
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Char) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Char) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TataminoGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TataminoGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != ' ') return false
        val o = get(p)
        move.obj = if (o == ' ') '1' else if (o == '3') ' ' else (o.toInt() + 1).toChar()
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
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            if (get(p) == ' ') isSolved = false else {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val ch = get(p)
            if (ch == ' ') continue
            for (os in TataminoGame.Companion.offset) {
                val p2 = p.add(os)
                if (isValid(p2) && get(p2) == ch) g.connectNode(pos2node[p], pos2node[p2])
            }
        }
        dots = cloner.deepClone(game.dots)
        while (!pos2node.isEmpty()) {
            g.setRootNode(List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter(F<P2<Position, Node>, Boolean> { e: P2<Position?, Node?> -> nodeList.contains(e._2()) }).map<Position>(F<P2<Position, Node>, Position> { e: P2<Position?, Node?> -> e._1() }).toJavaList()
            for (p in area) pos2node.remove(p)
            val ch = get(area[0])
            val n1 = area.size
            val n2 = ch - '0'
            val s: HintState = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            for (p in area) {
                pos2state[p] = s
                for (i in 0..3) {
                    val p2 = p.add(TataminoGame.Companion.offset.get(i))
                    val ch2 = if (!isValid(p2)) '.' else get(p2)
                    if (ch2 != ch && (n1 <= n2 || ch2 != ' ')) dots.set(p.add(TataminoGame.Companion.offset2.get(i)), TataminoGame.Companion.dirs.get(i), GridLineObject.Line)
                }
            }
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = CharArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        updateIsSolved()
    }
}