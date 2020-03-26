package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

import java.util.ArrayList
import java.util.HashMap

import fj.Ord
import fj.P
import fj.P2
import fj.data.Stream

import fj.data.Array.array
import fj.data.List.iterableList
import fj.data.TreeMap.iterableTreeMap
import java.nio.file.Files.exists

class ABCPathGameState(game: ABCPathGame) : CellsGameState<ABCPathGame, ABCPathGameMove, ABCPathGameState>(game) {
    private val objArray = game.objArray
    var pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    fun setObject(move: ABCPathGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != ' ' || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: ABCPathGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return false
        val o = this[p]
        // 1.  Enter every letter from A to Y into the grid.
        val chars = ('A' until 'Z').toMutableList()
        for (r in 1 until rows() - 1)
            for (c in 1 until cols() - 1) {
                val p2 = Position(r, c)
                if (p2 != p) chars.remove(this[p2])
            }
        val i = if (chars.contains(o)) chars.indexOf(o) else chars.size - 1
        move.obj = if (o == ' ') chars[0] else if (i == chars.size - 1) ' ' else chars[i + 1]
        return setObject(move)
    }

    /*
        https://www.brainbashers.com/showabcpath.asp
        ABC Path

        Description
        1.  Enter every letter from A to Y into the grid.
        2.  Each letter is next to the previous letter either horizontally, vertically or diagonally.
        3.  The clues around the edge tell you which row, column or diagonal each letter is in.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols())
                pos2state[Position(r, c)] = HintState.Normal
        var ch2rng = mutableMapOf<Char, MutableList<Position>>()
        for (r in 1 until rows() - 1)
            for (c in 1 until cols() - 1) {
                val p = Position(r, c)
                val ch = this[p]
                if (ch == ' ')
                    isSolved = false
                else {
                    val rng = ch2rng[ch] ?: mutableListOf();
                    rng.add(p);
                    ch2rng[ch] = rng
                }
            }
        ch2rng = ch2rng.filter { (_, rng) -> rng.size > 1 }.toMutableMap()
        if (!ch2rng.isEmpty()) isSolved = false
        for (rng in ch2rng.values)
            for (p in rng)
                pos2state[p] = HintState.Error
        // 2.  Each letter is next to the previous letter either horizontally, vertically or diagonally.
        for (r in 1 until rows() - 1)
            for (c in 1 until cols() - 1) {
                val p = Position(r, c)
                val ch = this[p]
                if (pos2state[p] == HintState.Normal && ch == 'A' || ABCPathGame.offset.any {
                        val p2 = p.add(it)
                        isValid(p2) && this[p2] == ch - 1
                    })
                    pos2state[p] = HintState.Complete
                else
                    isSolved = false
            }
        // 3.  The clues around the edge tell you which row, column or diagonal each letter is in.
        for ((ch, p) in game.ch2pos) {
            val r = p.row
            val c = p.col
            if ((r == 0 || r == rows() - 1) && r == c && (1 until rows() - 1).any { this[it, it] == ch } ||
                (r == 0 || r == rows() - 1) && r == rows() - 1 - c && (1 until rows() - 1).any { this[it, rows() - 1 - it] == ch } ||
                (r == 0 || r == rows() - 1) && (1 until rows() - 1).any { this[it, c] == ch } ||
                (c == 0 || c == cols() - 1) && (1 until cols() - 1).any { this[r, it] == ch })
                pos2state[p] = HintState.Complete
            else
                isSolved = false
        }
    }

}
