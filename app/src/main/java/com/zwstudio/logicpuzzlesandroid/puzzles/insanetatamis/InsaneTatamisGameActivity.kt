package com.zwstudio.logicpuzzlesandroid.puzzles.insanetatamis

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class InsaneTatamisGameActivity : GameGameActivity<InsaneTatamisGame, InsaneTatamisDocument, InsaneTatamisGameMove, InsaneTatamisGameState>() {
    private val document: InsaneTatamisDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = InsaneTatamisGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, InsaneTatamisHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        InsaneTatamisGame(level.layout, this, doc)
}