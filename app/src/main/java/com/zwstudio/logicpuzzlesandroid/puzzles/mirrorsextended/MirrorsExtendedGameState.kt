package com.zwstudio.logicpuzzlesandroid.puzzles.mirrorsextended

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MirrorsExtendedGameState(game: MirrorsExtendedGame) : CellsGameState<MirrorsExtendedGame, MirrorsExtendedGameMove, MirrorsExtendedGameState>(game) {
    val objArray = game.objArray.copyOf()
    val letter2state = HashMap<Char, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MirrorsExtendedObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: MirrorsExtendedObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: MirrorsExtendedGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: MirrorsExtendedGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            MirrorsExtendedObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) MirrorsExtendedObject.Marker else MirrorsExtendedObject.Backward
            MirrorsExtendedObject.Backward -> MirrorsExtendedObject.Forward
            MirrorsExtendedObject.Forward -> if (markerOption == MarkerOptions.MarkerLast) MirrorsExtendedObject.Marker else MirrorsExtendedObject.Empty
            MirrorsExtendedObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) MirrorsExtendedObject.Backward else MirrorsExtendedObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 4/Mirrors, extended

        Summary
        with lasers, of course

        Description
        1. On the border there are some lasers, marked with the letter and number.
        2. The letter tells you where that laser beam will start and end (it is paired with the same
           letter somewhere else).
        3. The number tells you how many mirrors the laser beam will bounce off before reaching the
           other letter.
        4. Each area contains one mirror.
        5. Each mirror reflects at least one laser beam.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val dot2dot = HashMap<MirrorsExtendedLaserDot, MirrorsExtendedLaserDot>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (val o = this[p]) {
                    MirrorsExtendedObject.Forbidden ->
                        this[p] = MirrorsExtendedObject.Empty
                    MirrorsExtendedObject.Backward, MirrorsExtendedObject.Forward -> {
                        val md = MirrorsExtendedGame.mirrorDirs[if (o == MirrorsExtendedObject.Forward) 0 else 1]
                        for (i in 0..<4) {
                            val d = md[i]
                            dot2dot[MirrorsExtendedLaserDot(p, i)] = MirrorsExtendedLaserDot(p + MirrorsExtendedGame.offset[d], d)
                        }
                    }
                    else -> {}
                }
            }
        for (area in game.areas) {
            val rng = area.filter { this[it].isMirror }
            if (rng.size != 1) isSolved = false
            if (rng.isNotEmpty() && allowedObjectsOnly)
                for (p in area)
                    if (!this[p].isMirror)
                        this[p] = MirrorsExtendedObject.Forbidden
        }
        for ((ch, o) in game.letter2laser) {
            var dt = o.dots[0]
            val p2 = o.dots[1].p
            var n1 = 0
            val n2 = o.number
            while (true) {
                val dt2 = dot2dot[dt]
                if (dt2 != null) {
                    dt = dt2
                    n1 += 1
                } else {
                    dt = MirrorsExtendedLaserDot(dt.p + MirrorsExtendedGame.offset[dt.dir], dt.dir)
                }
                val p = dt.p
                val o2 = this[p]
                if (o2 == MirrorsExtendedObject.Boundary) {
                    letter2state[ch] = HintState.Normal
                    isSolved = false
                    break
                } else if (o2 == MirrorsExtendedObject.Hint) {
                    val s = if (p == p2 && n1 == n2) HintState.Complete else HintState.Error
                    letter2state[ch] = s
                    if (s != HintState.Complete) isSolved = false
                    break
                }
            }
        }
    }
}