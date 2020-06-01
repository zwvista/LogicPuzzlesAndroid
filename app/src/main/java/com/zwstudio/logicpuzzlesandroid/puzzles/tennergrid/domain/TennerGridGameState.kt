package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.data.Array
import fj.data.List
import fj.data.Stream
import java.util.*

class TennerGridGameState(game: TennerGridGame) : CellsGameState<TennerGridGame, TennerGridGameMove, TennerGridGameState>(game) {
    var objArray: IntArray
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Int) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Int) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TennerGridGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) >= 0 || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TennerGridGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) >= 0) return false
        val o = get(p)
        move.obj = if (o == 9) -1 else o + 1
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 7/TennerGrid

        Summary
        Counting up to 10

        Description
        1. You goal is to enter every digit, from 0 to 9, in each row of the Grid.
        2. The number on the bottom row gives you the sum for that column.
        3. Digit can repeat on the same column, however digits in contiguous tiles
           must be different, even diagonally. Obviously digits can't repeat on
           the same row.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows() - 1) {
            val cs = List.range(0, cols()).groupBy<Int>(F<Int, Int> { c: Int -> get(r, c) }, Ord.intOrd).toStream()
                .filter(F<P2<Int, List<Int>>, Boolean> { kv: P2<Int?, List<Int?>?> -> kv._1() != -1 && kv._2().length() > 1 })
                .bind<Int>(F<P2<Int, List<Int>>, Stream<Int>> { kv: P2<Int?, List<Int?>?> -> kv._2().toStream() }).toJavaList()
            // 3. Obviously digits can't repeat on the same row.
            if (!cs.isEmpty()) isSolved = false
            for (c in 0 until cols()) {
                val p = Position(r, c)
                pos2state[p] = if (cs.contains(c)) HintState.Error else HintState.Normal
            }
        }
        for (c in 0 until cols()) {
            val h = get(rows() - 1, c)
            var n = 0
            var isDirty = false
            var allFixed = true
            for (r in 0 until rows() - 1) {
                val p = Position(r, c)
                val o1: Int = game.get(p)
                val o2 = get(p)
                if (o1 == -1) {
                    allFixed = false
                    if (o2 == -1) isSolved = false else isDirty = true
                }
                n += if (o2 == -1) 0 else o2
                // 3. Digit can repeat on the same column, however digits in contiguous tiles
                // must be different, even diagonally.
                if (r < rows() - 2) {
                    val rng = Array.array<Position>(*TennerGridGame.offset).toStream()
                        .map<Position>(F<Position, Position> { os: Position? -> p.add(os) }).filter(F<Position, Boolean> { p2: Position? -> isValid(p2) && o2 == get(p2) }).toJavaList()
                    if (!rng.isEmpty()) {
                        isSolved = false
                        pos2state[p] = HintState.Error
                        for (p2 in rng) pos2state[p2] = HintState.Error
                    }
                }
            }
            // 2. The number on the bottom row gives you the sum for that column.
            val s: HintState = if (!isDirty && !allFixed) HintState.Normal else if (n == h) HintState.Complete else HintState.Error
            pos2state[Position(rows() - 1, c)] = s
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = IntArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        updateIsSolved()
    }
}