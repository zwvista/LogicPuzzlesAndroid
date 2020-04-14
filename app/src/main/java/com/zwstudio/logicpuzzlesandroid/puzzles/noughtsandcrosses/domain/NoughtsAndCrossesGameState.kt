package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.Stream
import java.util.*

class NoughtsAndCrossesGameState(game: NoughtsAndCrossesGame) : CellsGameState<NoughtsAndCrossesGame?, NoughtsAndCrossesGameMove?, NoughtsAndCrossesGameState?>(game) {
    var objArray: CharArray
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Char) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position?, obj: Char) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: NoughtsAndCrossesGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != ' ' || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: NoughtsAndCrossesGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != ' ') return false
        val o = get(p).toInt()
        move.obj = if (o == ' '.toInt()) if (markerOption == MarkerOptions.MarkerFirst) '.' else '1' else if (o == '.'.toInt()) if (markerOption == MarkerOptions.MarkerFirst) '1' else ' ' else if (o == game.chMax.toInt()) if (markerOption == MarkerOptions.MarkerLast) '.' else ' ' else (o + 1).toChar()
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Noughts & Crosses

        Summary
        Spot the Number

        Description
        1. Place all numbers from 1 to N on each row and column - just once,
           without repeating.
        2. In other words, all numbers must appear just once on each row and column.
        3. A circle marks where a number must go.
        4. A cross marks where no number can go.
        5. All other cells can contain a number or be empty.
    */
    private fun updateIsSolved() {
        isSolved = true
        val f: F<List<Char>, HintState> = F<List<Char>, HintState> { nums: List<Char>? ->
            // 4. A cross marks where no number can go.
            // 5. All other cells can contain a number or be empty.
            val nums2 = fj.data.List.iterableList(nums).filter(F<Char, Boolean> { ch: Char -> !" .X".contains(ch.toString()) }).toJavaList()
            // 2. All numbers must appear just once.
            val s: HintState = if (nums2.size == game.chMax - '0' &&
                nums2.size == HashSet(nums2).size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            s
        }
        // 2. All numbers must appear just once on each row.
        Stream.range(0, rows().toLong()).foreachDoEffect(Effect1<Int> { r: Int -> row2state[r] = f.f(Stream.range(0, cols().toLong()).map<Char>(F<Int, Char> { c: Int -> get(r, c) }).toJavaList()) })
        // 2. All numbers must appear just once on each column.
        Stream.range(0, cols().toLong()).foreachDoEffect(Effect1<Int> { c: Int -> col2state[c] = f.f(Stream.range(0, rows().toLong()).map<Char>(F<Int, Char> { r: Int -> get(r, c) }).toJavaList()) })
        // 3. A circle marks where a number must go.
        for (p in game.noughts) {
            val ch = get(p)
            val s: HintState = if (ch == ' ' || ch == '.') HintState.Normal else HintState.Complete
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = CharArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        row2state = arrayOfNulls<HintState>(rows())
        col2state = arrayOfNulls<HintState>(cols())
        updateIsSolved()
    }
}