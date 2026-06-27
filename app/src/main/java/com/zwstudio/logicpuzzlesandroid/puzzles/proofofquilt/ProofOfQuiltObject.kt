package com.zwstudio.logicpuzzlesandroid.puzzles.proofofquilt

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class ProofOfQuiltPosition(
    val p: Position,
    val n: Int
) {
    override fun hashCode() = p.hashCode() * 100 + n
    override fun toString() = String.format("(%d,%d),%d", p.row, p.col, n)
}

class ProofOfQuiltGameMove(val p: Position, var obj: Char = ' ')
