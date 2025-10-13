package com.zwstudio.logicpuzzlesandroid.common.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface

enum class GameChangeType {
    None, InternalState, Level
}

open class GameState<GM> {
    var isSolved = false
    open fun setObject(move: GM): GameChangeType = GameChangeType.None
    open fun switchObject(move: GM): GameChangeType = GameChangeType.None
}

interface GameInterface<G : Game<G, GM, GS>, GM, GS : GameState<GM>> {
    fun moveAdded(game: G, move: GM)
    fun levelInitialized(game: G, state: GS)
    fun levelUpdated(game: G, stateFrom: GS, stateTo: GS)
    fun gameSolved(game: G)
    fun stateChanged(game: G, stateFrom: GS?, stateTo: GS)
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

    protected fun levelInitialized(state: GS) {
        states.add(state)
        gi.levelInitialized(this as G, state)
        gi.stateChanged(this, null, state)
        if (isSolved) gi.gameSolved(this)
    }

    protected fun levelUpdated(stateFrom: GS, stateTo: GS) {
        gi.levelUpdated(this as G, stateFrom, stateTo)
        gi.stateChanged(this, stateFrom, stateTo)
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

    protected fun changeObject(move: GM, f: (GS, GM) -> GameChangeType): Boolean {
    // Create a deep clone of the current state to work with
        var state: GS = cloner.deepClone(currentState)
    // Apply the state transformation function and handle the result
        when (f(state, move)) {
            GameChangeType.None -> return false  // No change made
            GameChangeType.InternalState -> {
                // swap state & currentState
                states[stateIndex] = state.also { state = currentState }
                gi.stateChanged(this as G, state, currentState)
                return false
            }
            GameChangeType.Level -> {
                if (canRedo) {
                    states.subList(stateIndex + 1, states.size).clear()
                    moves.subList(stateIndex, states.size).clear()
                }
                states.add(state)
                stateIndex++
                moves.add(move)
                moveAdded(move)
                levelUpdated(states[stateIndex - 1], state)
                return true
            }
        }
    }

    open fun switchObject(move: GM) = changeObject(move) { state, move2 -> state.switchObject(move2) }
    open fun setObject(move: GM) = changeObject(move) { state, move2 -> state.setObject(move2) }

    init {
        cloner.dontClone(this.javaClass)
    }
}