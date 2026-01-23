package com.zwstudio.logicpuzzlesandroid.puzzles.onlystraights

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class OnlyStraightsGameActivity : GameGameActivity<OnlyStraightsGame, OnlyStraightsDocument, OnlyStraightsGameMove, OnlyStraightsGameState>() {
    private val document: OnlyStraightsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = OnlyStraightsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, OnlyStraightsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        OnlyStraightsGame(level.layout, this, doc)
}