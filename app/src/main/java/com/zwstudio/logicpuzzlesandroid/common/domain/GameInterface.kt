package com.zwstudio.logicpuzzlesandroid.common.domain

interface GameInterface<G : Game<G, GM, GS>, GM, GS : GameState> {
    fun moveAdded(game: G, move: GM)
    fun levelInitilized(game: G, state: GS)
    fun levelUpdated(game: G, stateFrom: GS, stateTo: GS)
    fun gameSolved(game: G)
}