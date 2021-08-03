package com.zwstudio.logicpuzzlesandroid.puzzles.hitori

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class HitoriGameActivity : GameGameActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState>() {
    private val document: HitoriDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = HitoriGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, HitoriHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        HitoriGame(level.layout, this, doc)
}
