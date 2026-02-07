package com.zwstudio.logicpuzzlesandroid.puzzles.castlepatrol

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CastlePatrolGameActivity : GameGameActivity<CastlePatrolGame, CastlePatrolDocument, CastlePatrolGameMove, CastlePatrolGameState>() {
    private val document: CastlePatrolDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CastlePatrolGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CastlePatrolHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CastlePatrolGame(level.layout, this, doc)
}