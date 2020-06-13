package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.*

class RobotCrosswordsGame(layout: List<String>, gi: GameInterface<RobotCrosswordsGame, RobotCrosswordsGameMove, RobotCrosswordsGameState>, gdi: GameDocumentInterface) : CellsGame<RobotCrosswordsGame, RobotCrosswordsGameMove, RobotCrosswordsGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var objArray: IntArray
    var areas = mutableListOf<List<Position>>()
    var horzAreaCount = 0

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = IntArray(rows() * cols())
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val ch = str[c]
                this[r, c] = if (ch == '.') -1 else if (ch == ' ') 0 else ch - '0'
            }
        }
        val area = mutableListOf<Position>()
        fun f(isHorz: Boolean) {
            if (area.isEmpty()) return
            if (area.size > 1) {
                areas.add(ArrayList(area))
                if (isHorz) horzAreaCount++
            }
            area.clear()
        }
        for (r in 0 until rows()) {
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (this[p] == -1)
                    f(true)
                else
                    area.add(p)
            }
            f(true)
        }
        for (c in 0 until cols()) {
            for (r in 0 until rows()) {
                val p = Position(r, c)
                if (this[p] == -1)
                    f(false)
                else
                    area.add(p)
            }
            f(false)
        }
        val state = RobotCrosswordsGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: RobotCrosswordsGameMove, f: (RobotCrosswordsGameState, RobotCrosswordsGameMove) -> Boolean): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: RobotCrosswordsGameMove) = changeObject(move, RobotCrosswordsGameState::switchObject)
    fun setObject(move: RobotCrosswordsGameMove) = changeObject(move, RobotCrosswordsGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun getHorzState(p: Position) = state().pos2horzState[p]
    fun getVertState(p: Position) = state().pos2vertState[p]
}
