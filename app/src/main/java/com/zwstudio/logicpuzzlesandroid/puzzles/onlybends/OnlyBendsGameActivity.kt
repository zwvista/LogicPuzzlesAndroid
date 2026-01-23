package com.zwstudio.logicpuzzlesandroid.puzzles.onlybends

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class OnlyBendsGameActivity : GameGameActivity<OnlyBendsGame, OnlyBendsDocument, OnlyBendsGameMove, OnlyBendsGameState>() {
    private val document: OnlyBendsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = OnlyBendsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, OnlyBendsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        OnlyBendsGame(level.layout, this, doc)
}