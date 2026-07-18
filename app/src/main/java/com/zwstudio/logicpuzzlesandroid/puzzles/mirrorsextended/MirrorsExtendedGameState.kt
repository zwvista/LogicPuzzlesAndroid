package com.zwstudio.logicpuzzlesandroid.puzzles.mirrorsextended

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MirrorsExtendedGameState(game: MirrorsExtendedGame) : CellsGameState<MirrorsExtendedGame, MirrorsExtendedGameMove, MirrorsExtendedGameState>(game) {
    val objArray = Array(rows * cols) { MirrorsExtendedObject.Empty }
    val row2state = Array(rows) { HintState.Normal }
    val col2state = Array(cols) { HintState.Normal }
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

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
//        for (r in 0..<rows)
//            for (c in 0..<cols) {
//                val p = Position(r, c)
//                if (this[p] == MirrorsExtendedObject.Forbidden)
//                    this[p] = MirrorsExtendedObject.Empty
//                pos2state[p] = AllowedObjectState.Normal
//            }
//        // 2. You have to fill some water in it, considering that water pours down
//        //    and levels itself like in reality.
//        // 3. Areas of the same level which are horizontally connected will have
//        //    the same water level.
//        for (r in 0..<rows)
//            for (c in 0..<cols) {
//                val p = Position(r, c)
//                if (this[p] == MirrorsExtendedObject.Water && !listOf(1, 2, 3).all { i ->
//                    game.dots[p + MirrorsExtendedGame.offset2[i], MirrorsExtendedGame.dirs[i]] == GridLineObject.Line ||
//                            this[p + MirrorsExtendedGame.offset[i]] == MirrorsExtendedObject.Water
//                }) { pos2state[p] = AllowedObjectState.Error; isSolved = false }
//            }
//        // 4. The numbers on the border show you how many tiles of each row and
//        //    column are filled.
//        for (r in 0..<rows) {
//            val n2 = game.row2hint[r]
//            if (n2 == MirrorsExtendedGame.PUZ_UNKNOWN) continue
//            val n1 = (0..<cols).count { this[r, it] == MirrorsExtendedObject.Water }
//            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
//            row2state[r] = s
//            if (s != HintState.Complete) isSolved = false
//            if (s != HintState.Normal && allowedObjectsOnly)
//                (0..<cols).filter { this[r, it] == MirrorsExtendedObject.Empty }.forEach {
//                    this[r, it] = MirrorsExtendedObject.Forbidden
//                }
//        }
//        for (c in 0..<cols) {
//            val n2 = game.col2hint[c]
//            if (n2 == MirrorsExtendedGame.PUZ_UNKNOWN) continue
//            val n1 = (0..<rows).count { this[it, c] == MirrorsExtendedObject.Water }
//            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
//            col2state[c] = s
//            if (s != HintState.Complete) isSolved = false
//            if (s != HintState.Normal && allowedObjectsOnly)
//                (0..<rows).filter { this[it, c] == MirrorsExtendedObject.Empty }.forEach {
//                    this[it, c] = MirrorsExtendedObject.Forbidden
//                }
//        }
    }
}