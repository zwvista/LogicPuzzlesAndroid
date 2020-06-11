package com.zwstudio.logicpuzzlesandroid.common.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import java.util.*

open class Game<G : Game<G, GM, GS>?, GM, GS : GameState?>(gi: GameInterface<G, GM, GS>?, gdi: GameDocumentInterface) {
    protected var cloner = Cloner()
    protected var stateIndex = 0
    protected var states: List<GS> = ArrayList()
    protected fun state(): GS {
        return states[stateIndex]
    }

    protected var moves: List<GM> = ArrayList()
    protected fun move(): GM {
        return moves[stateIndex - 1]
    }

    val isSolved: Boolean
        get() = state()!!.isSolved

    fun canUndo(): Boolean {
        return stateIndex > 0
    }

    fun canRedo(): Boolean {
        return stateIndex < states.size - 1
    }

    fun moveIndex(): Int {
        return stateIndex
    }

    fun moveCount(): Int {
        return states.size - 1
    }

    private val gi: GameInterface<G, GM, GS>?
    var gdi: GameDocumentInterface
    protected fun moveAdded(move: GM) {
        if (gi == null) return
        gi.moveAdded(this as G, move)
    }

    protected fun levelInitilized(state: GS) {
        if (gi == null) return
        gi.levelInitilized(this as G, state)
        if (isSolved) gi.gameSolved(this as G)
    }

    protected fun levelUpdated(stateFrom: GS, stateTo: GS) {
        if (gi == null) return
        gi.levelUpdated(this as G, stateFrom, stateTo)
        if (isSolved) gi.gameSolved(this as G)
    }

    fun undo() {
        if (!canUndo()) return
        stateIndex--
        levelUpdated(states[stateIndex + 1], state())
    }

    fun redo() {
        if (!canRedo()) return
        stateIndex++
        levelUpdated(states[stateIndex - 1], state())
    }

    init {
        cloner.dontClone(this.javaClass)
        this.gi = gi
        this.gdi = gdi
    }
}