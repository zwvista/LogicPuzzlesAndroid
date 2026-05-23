package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class KakuroGame(layout: List<String>, gi: GameInterface<KakuroGame, KakuroGameMove, KakuroGameState>, gdi: GameDocumentInterface) : CellsGame<KakuroGame, KakuroGameMove, KakuroGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val pos2horzHint = mutableMapOf<Position, Int>()
    val pos2vertHint = mutableMapOf<Position, Int>()
    val pos2num = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length / 4)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val s1 = str.substring(c * 4, c * 4 + 2)
                val s2 = str.substring(c * 4 + 2, c * 4 + 4)
                if (s1[0] == ' ')
                    pos2num[p] = 0
                else {
                    if (s1 != "00") pos2vertHint[p] = s1.toInt()
                    if (s2 != "00") pos2horzHint[p] = s2.toInt()
                }
            }
        }
        val state = KakuroGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getHorzState(p: Position) = currentState.pos2horzHint[p]
    fun getVertState(p: Position) = currentState.pos2vertHint[p]
}
