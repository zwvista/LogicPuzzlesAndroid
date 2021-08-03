package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SkyscrapersGameActivity : GameGameActivity<SkyscrapersGame, SkyscrapersDocument, SkyscrapersGameMove, SkyscrapersGameState>() {
    private val document: SkyscrapersDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SkyscrapersGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SkyscrapersHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SkyscrapersGame(level.layout, this, doc)
}