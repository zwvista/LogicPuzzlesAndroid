package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweensumscrapers

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class InbetweenSumscrapersGameActivity : GameGameActivity<InbetweenSumscrapersGame, InbetweenSumscrapersDocument, InbetweenSumscrapersGameMove, InbetweenSumscrapersGameState>() {
    private val document: InbetweenSumscrapersDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = InbetweenSumscrapersGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, InbetweenSumscrapersHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        InbetweenSumscrapersGame(level.layout, this, doc)
}