package com.zwstudio.logicpuzzlesandroid.common.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface

open class GameState {
    var isSolved = false
}

interface GameInterface<G : Game<G, GM, GS>, GM, GS : GameState> {
    fun moveAdded(game: G, move: GM)
    fun levelInitilized(game: G, state: GS)
    fun levelUpdated(game: G, stateFrom: GS, stateTo: GS)
    fun gameSolved(game: G)
}

@Suppress("UNCHECKED_CAST")
open class Game<G : Game<G, GM, GS>, GM, GS : GameState>(gi: GameInterface<G, GM, GS>, gdi: GameDocumentInterface) {
    protected var cloner = Cloner()
    protected var stateIndex = 0
    protected var states = mutableListOf<GS>()
    protected fun state(): GS {
        return states[stateIndex]
    }

    protected var moves = mutableListOf<GM>()
    protected fun move(): GM {
        return moves[stateIndex - 1]
    }

    val isSolved: Boolean
        get() = state().isSolved

    val canUndo: Boolean
        get() = stateIndex > 0

    val canRedo: Boolean
        get() = stateIndex < states.size - 1

    val moveIndex: Int
        get() = stateIndex

    val moveCount: Int
        get() = states.size - 1

    private val gi: GameInterface<G, GM, GS>?
    var gdi: GameDocumentInterface
    protected fun moveAdded(move: GM) {
        if (gi == null) return
        gi.moveAdded(this as G, move)
    }

    protected fun levelInitilized(state: GS) {
        if (gi == null) return
        gi.levelInitilized(this as G, state)
        if (isSolved) gi.gameSolved(this)
    }

    protected fun levelUpdated(stateFrom: GS, stateTo: GS) {
        if (gi == null) return
        gi.levelUpdated(this as G, stateFrom, stateTo)
        if (isSolved) gi.gameSolved(this)
    }

    fun undo() {
        if (!canUndo) return
        stateIndex--
        levelUpdated(states[stateIndex + 1], state())
    }

    fun redo() {
        if (!canRedo) return
        stateIndex++
        levelUpdated(states[stateIndex - 1], state())
    }

    init {
        cloner.dontClone(this.javaClass)
        this.gi = gi
        this.gdi = gdi
    }
}