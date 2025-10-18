package com.zwstudio.logicpuzzlesandroid.puzzles.zenlandscaper

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ZenLandscaperGameActivity : GameGameActivity<ZenLandscaperGame, ZenLandscaperDocument, ZenLandscaperGameMove, ZenLandscaperGameState>() {
    private val document: ZenLandscaperDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ZenLandscaperGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ZenLandscaperHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        ZenLandscaperGame(level.layout, this, doc)
}