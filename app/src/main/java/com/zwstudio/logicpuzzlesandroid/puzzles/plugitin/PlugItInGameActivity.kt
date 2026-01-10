package com.zwstudio.logicpuzzlesandroid.puzzles.plugitin

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PlugItInGameActivity : GameGameActivity<PlugItInGame, PlugItInDocument, PlugItInGameMove, PlugItInGameState>() {
    private val document: PlugItInDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PlugItInGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PlugItInHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PlugItInGame(level.layout, this, doc)
}