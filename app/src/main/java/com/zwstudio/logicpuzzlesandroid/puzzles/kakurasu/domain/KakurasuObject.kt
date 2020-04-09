package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class KakurasuObject {
    Empty, Forbidden, Marker, Cloud
}

class KakurasuGameMove(val p: Position, var obj: KakurasuObject = KakurasuObject.Empty)
