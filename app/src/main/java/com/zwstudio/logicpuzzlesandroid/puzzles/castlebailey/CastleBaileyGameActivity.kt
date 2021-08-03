package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CastleBaileyGameActivity : GameGameActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    private val document: CastleBaileyDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CastleBaileyGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CastleBaileyHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CastleBaileyGame(level.layout, this, doc)
}
