package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class KakuroGame(layout: List<String>, gi: GameInterface<KakuroGame, KakuroGameMove, KakuroGameState>, gdi: GameDocumentInterface) : CellsGame<KakuroGame, KakuroGameMove, KakuroGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var pos2horzHint = mutableMapOf<Position, Int>()
    var pos2vertHint = mutableMapOf<Position, Int>()
    var pos2num = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length / 4)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val s = str.substring(c * 4, c * 4 + 4)
                if (s == "    ")
                    pos2num[p] = 0
                else {
                    val s1 = s.substring(0, 2)
                    val s2 = s.substring(2, 4)
                    if (s1 != "00") pos2vertHint[p] = s1.toInt()
                    if (s2 != "00") pos2horzHint[p] = s2.toInt()
                }
            }
        }
        val state = KakuroGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getHorzState(p: Position) = currentState.pos2horzHint[p]
    fun getVertState(p: Position) = currentState.pos2vertHint[p]
}
