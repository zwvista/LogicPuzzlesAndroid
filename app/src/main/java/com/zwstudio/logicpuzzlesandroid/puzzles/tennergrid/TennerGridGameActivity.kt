package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TennerGridGameActivity : GameGameActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState>() {
    private val document: TennerGridDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TennerGridGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TennerGridHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TennerGridGame(level.layout, this, doc)
}