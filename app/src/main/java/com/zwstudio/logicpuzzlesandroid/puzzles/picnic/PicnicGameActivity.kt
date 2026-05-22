package com.zwstudio.logicpuzzlesandroid.puzzles.picnic

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PicnicGameActivity : GameGameActivity<PicnicGame, PicnicDocument, PicnicGameMove, PicnicGameState>() {
    private val document: PicnicDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PicnicGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PicnicHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PicnicGame(level.layout, this, doc)
}