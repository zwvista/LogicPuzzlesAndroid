package com.zwstudio.logicpuzzlesandroid.puzzles.planets

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PlanetsGameState(game: PlanetsGame) : CellsGameState<PlanetsGame, PlanetsGameMove, PlanetsGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: PlanetsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: PlanetsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PlanetsGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != PlanetsObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PlanetsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != PlanetsObject.Empty) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            PlanetsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) PlanetsObject.Marker else PlanetsObject.Sun
            PlanetsObject.Sun -> PlanetsObject.Nebula
            PlanetsObject.Nebula -> if (markerOption == MarkerOptions.MarkerLast) PlanetsObject.Marker else PlanetsObject.Empty
            PlanetsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) PlanetsObject.Sun else PlanetsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 1/Planets

        Summary
        Planets, Stars and Nebulas

        Description
        1. In Planets you are given an interesting Galaxy, where Suns only
           shine their light in horizontal and vertical lines.
        2. On the board you can see the Planets of this Galaxy. Each Planet
           is lit on some side (or not lit at all).
        3. You should place one Sun on each row and column, according to how
           the Planets are lit.
        4. You should also place one Nebula on each row and column.
        5. Nebulas block sunlight, so if there is a Nebula between a Sun and
           a Planet, the Planet won't be lit.
        6. Finally, Planets block sunlight too. So if there is a Planet
           between a Sun and another Planet, the further Planet won't be lit
           by that Sun.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2state[p] = AllowedObjectState.Normal
                if (this[p] == PlanetsObject.Forbidden)
                    this[p] = PlanetsObject.Empty
            }
        fun checkSymbols(suns: List<Position>, nebulae: List<Position>, empties: List<Position>) {
            // 3. You should place one Sun on each row and column, according to how
            //    the Planets are lit.
            if (suns.size != 1) {
                isSolved = false
                for (p in suns)
                    pos2state[p] = AllowedObjectState.Error
            }
            // 4. You should also place one Nebula on each row and column.
            if (nebulae.size != 1) {
                isSolved = false
                for (p in nebulae)
                    pos2state[p] = AllowedObjectState.Error
            }
            if (allowedObjectsOnly && suns.isNotEmpty() && nebulae.isNotEmpty()) {
                for (p in empties)
                    this[p] = PlanetsObject.Forbidden
            }
        }
        for (r in 0 until rows) {
            val suns = mutableListOf<Position>()
            val nebulae = mutableListOf<Position>()
            val empties = mutableListOf<Position>()
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (val o = this[p]) {
                    PlanetsObject.Sun -> suns.add(p)
                    PlanetsObject.Nebula -> nebulae.add(p)
                    PlanetsObject.Empty, PlanetsObject.Marker -> empties.add(p)
                    else -> {}
                }
            }
            checkSymbols(suns, nebulae, empties)
        }
        // 3. You should place one Sun on each row and column, according to how
        //    the Planets are lit.
        // 5. Nebulas block sunlight, so if there is a Nebula between a Sun and
        //    a Planet, the Planet won't be lit.
        // 6. Finally, Planets block sunlight too. So if there is a Planet
        //    between a Sun and another Planet, the further Planet won't be lit
        //    by that Sun.
        for (c in 0 until cols) {
            val suns = mutableListOf<Position>()
            val nebulae = mutableListOf<Position>()
            val empties = mutableListOf<Position>()
            for (r in 0 until rows) {
                val p = Position(r, c)
                when (val o = this[p]) {
                    PlanetsObject.Sun -> suns.add(p)
                    PlanetsObject.Nebula -> nebulae.add(p)
                    PlanetsObject.Empty, PlanetsObject.Marker -> empties.add(p)
                    else -> {}
                }
            }
            checkSymbols(suns, nebulae, empties)
        }
        for (p in game.planets) {
            val o = this[p]
            val isLit = mutableListOf<Int>()
            for (i in 0 until 4) {
                val os = PlanetsGame.offset[i]
                var p2 = p + os
                while (isValid(p2)) {
                    val o2 = this[p2]
                    if (o2 == PlanetsObject.Sun) {
                        isLit.add(i); break
                    }
                    if (o2 != PlanetsObject.Empty && o2 != PlanetsObject.Marker) break
                    p2 += os
                }
            }
            if (PlanetsGame.isLitMap[isLit] != o) {
                isSolved = false
                pos2state[p] = AllowedObjectState.Error
            }
        }
    }
}