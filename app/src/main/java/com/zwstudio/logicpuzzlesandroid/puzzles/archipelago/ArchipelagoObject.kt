package com.zwstudio.logicpuzzlesandroid.puzzles.archipelago

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ArchipelagoObject {
    Empty, Hint, Marker, Water
}

class ArchipelagoGameMove(val p: Position, var obj: ArchipelagoObject = ArchipelagoObject.Empty)
