package com.zwstudio.logicpuzzlesandroid.puzzles.thegreylabyrinth

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TheGreyLabyrinthObject {
    Empty, Treasure, Up, Right, Down, Left, Forbidden, Marker, Wall
}

class TheGreyLabyrinthGameMove(val p: Position, var obj: TheGreyLabyrinthObject = TheGreyLabyrinthObject.Empty)
