package com.zwstudio.logicpuzzlesandroid.puzzles.desertdunes

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class DesertDunesGameActivity : GameGameActivity<DesertDunesGame, DesertDunesDocument, DesertDunesGameMove, DesertDunesGameState>() {
    private val document: DesertDunesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = DesertDunesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, DesertDunesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        DesertDunesGame(level.layout, this, doc)
}