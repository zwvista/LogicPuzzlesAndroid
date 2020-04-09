package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MagnetsObject {
    Empty {
        override val isEmpty = true
        override val isPole = false
    },
    Marker {
        override val isEmpty = true
        override val isPole = false
    },
    Negative {
        override val isEmpty = false
        override val isPole = true
    },
    Positive {
        override val isEmpty = false
        override val isPole = true
    };

    abstract val isEmpty: Boolean
    abstract val isPole: Boolean
}

class MagnetsArea(val p: Position, var obj: MagnetsAreaType = MagnetsAreaType.Single)

enum class MagnetsAreaType {
    Single, Horizontal, Vertical
}

open class MagnetsGameMove(val p: Position, var obj: MagnetsObject = MagnetsObject.Empty)
