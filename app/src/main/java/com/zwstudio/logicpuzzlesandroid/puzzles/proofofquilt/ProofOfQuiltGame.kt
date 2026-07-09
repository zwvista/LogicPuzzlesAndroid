package com.zwstudio.logicpuzzlesandroid.puzzles.proofofquilt

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import kotlin.math.max
import kotlin.math.min

class ProofOfQuiltGame(layout: List<String>, gi: GameInterface<ProofOfQuiltGame, ProofOfQuiltGameMove, ProofOfQuiltGameState>, gdi: GameDocumentInterface) : CellsGame<ProofOfQuiltGame, ProofOfQuiltGameMove, ProofOfQuiltGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val pos2hint = mutableMapOf<Position, Int>()
    val patterns = mutableListOf<ProofOfQuiltPattern>()
    val allPositions = mutableSetOf<Position>()
    val objArray: Array<ProofOfQuiltObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ProofOfQuiltObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ProofOfQuiltObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { ProofOfQuiltObject.Empty }
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == ' ')
                    allPositions.add(p)
                else {
                    this[p] = ProofOfQuiltObject.Filled
                    if (ch != 'W')
                        pos2hint[p] = ch - '0'
                }
            }
        }
        //    (j,k) = 1,1
        //    A B
        //    C D
        //
        //    (j,k) = 1,2    (j,k) = 2,1
        //    A B .          . A B
        //    C . B          A . D
        //    . C D          C D .
        //
        //    (j,k) = 1,3    (j,k) = 2,2   (j,k) = 3,1
        //    A B . .        . A B .       . . A B
        //    C . B .        A . . B       . A . D
        //    . C . B        C . . D       A . D .
        //    . . C D        . C D .       C D . .
        //
        //    (j,k) = 1,4    (j,k) = 2,3   (j,k) = 3,2    (j,k) = 4,1
        //    A B . . .      . A B . .     . . A B .      . . . A B
        //    C . B . .      A . . B .     . A . . B      . . A . D
        //    . C . B .      C . . . B     A . . . D      . A . D .
        //    . . C . B      . C . . D     C . . D .      A . D . .
        //    . . . C D      . . C D .     . C D . .      C D . . .
        //
        // Find all tilted quilts
        // A tilted quilt has a circumscribed square
        for (i in 2..rows)
            for (j in 1..<i) {
                val k = i - j
                val pattern = mutableMapOf<Position, ProofOfQuiltObject>()
                for (dr in 0..<i) {
                    val m1: Int
                    val m2: Int
                    val o1: ProofOfQuiltObject
                    val o2: ProofOfQuiltObject
                    if (dr < min(j, k)) {
                        m1 = j - 1 - dr
                        m2 = j + dr
                        o1 = ProofOfQuiltObject.TriangleA
                        o2 = ProofOfQuiltObject.TriangleB
                    } else if (dr < j) {
                        m1 = j - 1 - dr
                        m2 = i - 1 - dr + k
                        o1 = ProofOfQuiltObject.TriangleA
                        o2 = ProofOfQuiltObject.TriangleD
                    } else if (dr < max(j, k)) {
                        m1 = dr - j
                        m2 = j + dr
                        o1 = ProofOfQuiltObject.TriangleC
                        o2 = ProofOfQuiltObject.TriangleB
                    } else {
                        m1 = dr - j
                        m2 = i - 1 - dr + k
                        o1 = ProofOfQuiltObject.TriangleC
                        o2 = ProofOfQuiltObject.TriangleD
                    }
                    for (dc in 0..<i) {
                        val p = Position(dr, dc)
                        if (dc == m1)
                            pattern[p] = o1
                        else if (dc > m1 && dc < m2)
                            pattern[p] = ProofOfQuiltObject.Empty
                        else if (dc == m2)
                            pattern[p] = o2
                    }
                }
                patterns.add(ProofOfQuiltPattern(i, pattern))
            }
        val state = ProofOfQuiltGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position): ProofOfQuiltObject = currentState[p]
    fun getObject(row: Int, col: Int): ProofOfQuiltObject = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
