package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.data.List
import fj.data.Stream
import java.util.*

class TatamiGameState(game: TatamiGame) : CellsGameState<TatamiGame?, TatamiGameMove?, TatamiGameState?>(game) {
    var objArray: CharArray
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Char) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Char) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TatamiGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != ' ' || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TatamiGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != ' ') return false
        val o = get(p)
        move.obj = if (o == ' ') '1' else if (o == '3') ' ' else (o.toInt() + 1).toChar()
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Tatami

        Summary
        1,2,3... 1,2,3... Fill the mats

        Description
        1. Each rectangle represents a mat(Tatami) which is of the same size.
           You must fill each Tatami with a number ranging from 1 to size.
        2. Each number can appear only once in each Tatami.
        3. In one row or column, each number must appear the same number of times.
        4. You can't have two identical numbers touching horizontally or vertically.
    */
    private fun updateIsSolved() {
        isSolved = true
        val chars2 = Arrays.asList('1', '2', '3')
        val chars3 = List.iterableList(chars2).bind<Char>(F<Char, List<Char>> { ch: Char -> List.iterableList(Collections.nCopies(rows() / 3, ch)) }).toJavaList()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            if (get(p) == ' ') isSolved = false
            pos2state[p] = HintState.Normal
        }
        for (r in 0 until rows()) {
            var lineSolved = true
            for (c in 0 until cols() - 1) {
                val p1 = Position(r, c)
                val p2 = Position(r, c + 1)
                val ch1 = get(p1)
                val ch2 = get(p2)
                if (ch1 != ' ' && ch2 != ' ' && ch1 == ch2) {
                    // 4. You can't have two identical numbers touching horizontally.
                    lineSolved = false
                    isSolved = lineSolved
                    pos2state[p1] = HintState.Error
                    pos2state[p2] = HintState.Error
                }
            }
            val chars = Stream.range(0, cols().toLong()).map<Char>(F<Int, Char> { c: Int -> get(r, c) }).sort(Ord.charOrd).toJavaList()
            // 3. In one row, each number must appear the same number of times.
            if (chars[0] != ' ' && chars != chars3) {
                lineSolved = false
                isSolved = lineSolved
                for (c in 0 until cols()) pos2state[Position(r, c)] = HintState.Error
            }
            if (lineSolved) for (c in 0 until cols()) pos2state[Position(r, c)] = HintState.Complete
        }
        for (c in 0 until cols()) {
            var lineSolved = true
            for (r in 0 until rows() - 1) {
                val p1 = Position(r, c)
                val p2 = Position(r + 1, c)
                val ch1 = get(p1)
                val ch2 = get(p2)
                if (ch1 != ' ' && ch2 != ' ' && ch1 == ch2) {
                    // 4. You can't have two identical numbers touching vertically.
                    lineSolved = false
                    isSolved = lineSolved
                    pos2state[p1] = HintState.Error
                    pos2state[p2] = HintState.Error
                }
            }
            val chars = Stream.range(0, rows().toLong()).map<Char>(F<Int, Char> { r: Int -> get(r, c) }).sort(Ord.charOrd).toJavaList()
            // 3. In one column, each number must appear the same number of times.
            if (chars[0] != ' ' && chars != chars3) {
                lineSolved = false
                isSolved = lineSolved
                for (r in 0 until rows()) pos2state[Position(r, c)] = HintState.Error
            }
            if (lineSolved) for (r in 0 until rows()) pos2state[Position(r, c)] = HintState.Complete
        }
        // 2. Each number can appear only once in each Tatami.
        for (a in game.areas) {
            val chars = List.iterableList<Position>(a).map<Char>(F<Position, Char> { p: Position -> get(p) }).sort(Ord.charOrd).toJavaList()
            if (chars[0] != ' ' && chars != chars2) {
                isSolved = false
                for (p in a) pos2state[p] = HintState.Error
            }
        }
    }

    init {
        objArray = CharArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        updateIsSolved()
    }
}