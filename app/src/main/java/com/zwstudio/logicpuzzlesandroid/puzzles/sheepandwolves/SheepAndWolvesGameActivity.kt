package com.zwstudio.logicpuzzlesandroid.puzzles.sheepandwolves

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SheepAndWolvesGameActivity : GameGameActivity<SheepAndWolvesGame, SheepAndWolvesDocument, SheepAndWolvesGameMove, SheepAndWolvesGameState>() {
    private val document: SheepAndWolvesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SheepAndWolvesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SheepAndWolvesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SheepAndWolvesGame(level.layout, this, doc)
}