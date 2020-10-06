package com.zwstudio.logicpuzzlesandroid.common.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface

open class GameState<GM> {
    var isSolved = false
    open fun setObject(move: GM) = false
    open fun switchObject(move: GM) = false
}

interface GameInterface<G : Game<G, GM, GS>, GM, GS : GameState<GM>> {
    fun moveAdded(game: G, move: GM)
    fun levelInitilized(game: G, state: GS)
    fun levelUpdated(game: G, stateFrom: GS, stateTo: GS)
    fun gameSolved(game: G)
}

@Suppress("UNCHECKED_CAST")
open class Game<G : Game<G, GM, GS>, GM, GS : GameState<GM>>(val gi: GameInterface<G, GM, GS>, val gdi: GameDocumentInterface) {
    protected var cloner = Cloner()
    protected var stateIndex = 0
    protected var states = mutableListOf<GS>()
    protected val currentState get() = states[stateIndex]
    protected var moves = mutableListOf<GM>()

    val isSolved get() = currentState.isSolved
    val canUndo get() = stateIndex > 0
    val canRedo get() = stateIndex < states.size - 1
    val moveIndex get() = stateIndex
    val moveCount get() = states.size - 1

    protected fun moveAdded(move: GM) {
        gi.moveAdded(this as G, move)
    }

    protected fun levelInitilized(state: GS) {
        states.add(state)
        gi.levelInitilized(this as G, state)
        if (isSolved) gi.gameSolved(this)
    }

    protected fun levelUpdated(stateFrom: GS, stateTo: GS) {
        gi.levelUpdated(this as G, stateFrom, stateTo)
        if (isSolved) gi.gameSolved(this)
    }

    fun undo() {
        if (!canUndo) return
        stateIndex--
        levelUpdated(states[stateIndex + 1], currentState)
    }

    fun redo() {
        if (!canRedo) return
        stateIndex++
        levelUpdated(states[stateIndex - 1], currentState)
    }

    protected fun changeObject(move: GM, f: (GS, GM) -> Boolean): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state: GS = cloner.deepClone(currentState)
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

    open fun switchObject(move: GM) = changeObject(move) { state, move2 -> state.switchObject(move2) }
    open fun setObject(move: GM) = changeObject(move) { state, move2 -> state.setObject(move2) }

    init {
        cloner.dontClone(this.javaClass)
    }
}