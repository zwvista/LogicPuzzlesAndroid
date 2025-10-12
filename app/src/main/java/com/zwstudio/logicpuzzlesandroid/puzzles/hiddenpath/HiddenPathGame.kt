package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.CastleBaileyObject
import java.nio.file.Files.size

class HiddenPathGame(layout: List<String>, gi: GameInterface<HiddenPathGame, HiddenPathGameMove, HiddenPathGameState>, gdi: GameDocumentInterface) : CellsGame<HiddenPathGame, HiddenPathGameMove, HiddenPathGameState>(gi, gdi) {
    companion object {
        var offset = Position.Directions8
    }

    var objArray: Array<HiddenPathObject>
    var pos2hint = mutableMapOf<Position, Int>()
    var maxNum: Int

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: HiddenPathObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: HiddenPathObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length / 3)
        objArray = Array(rows * cols) { HiddenPathObject() }
        maxNum = rows * cols
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val s = str.substring(c * 3, c * 3 + 2)
                this[p].obj = if (s == "  ") 0 else s.trim(' ').toInt()
                pos2hint[p] = str[c * 3 + 2] - '0'
            }
        }
        val state = HiddenPathGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
