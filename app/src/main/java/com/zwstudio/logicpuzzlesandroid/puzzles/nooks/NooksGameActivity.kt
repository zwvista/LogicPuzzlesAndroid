package com.zwstudio.logicpuzzlesandroid.puzzles.nooks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class NooksGameActivity : GameGameActivity<NooksGame, NooksDocument, NooksGameMove, NooksGameState>() {
    private val document: NooksDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = NooksGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, NooksHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        NooksGame(level.layout, this, doc)
}