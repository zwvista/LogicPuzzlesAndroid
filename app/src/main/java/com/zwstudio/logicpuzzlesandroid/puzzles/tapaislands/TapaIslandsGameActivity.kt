package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TapaIslandsGameActivity : GameGameActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState>() {
    private val document: TapaIslandsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TapaIslandsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TapaIslandsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TapaIslandsGame(level.layout, this, doc)
}