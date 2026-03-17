package com.zwstudio.logicpuzzlesandroid.puzzles.desertdunes

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class DesertDunesObject {
    Empty, Forbidden, Hint, Marker, Dune
}

class DesertDunesGameMove(val p: Position, var obj: DesertDunesObject = DesertDunesObject.Empty)
