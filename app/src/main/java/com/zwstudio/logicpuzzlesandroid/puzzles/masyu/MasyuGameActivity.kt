package com.zwstudio.logicpuzzlesandroid.puzzles.masyu

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class MasyuGameActivity : GameGameActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    private val document: MasyuDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = MasyuGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, MasyuHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        MasyuGame(level.layout, this, doc)
}