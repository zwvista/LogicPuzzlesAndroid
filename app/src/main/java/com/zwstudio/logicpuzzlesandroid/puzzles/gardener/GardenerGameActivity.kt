package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class GardenerGameActivity : GameGameActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState>() {
    private val document: GardenerDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = GardenerGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, GardenerHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        GardenerGame(level.layout, this, doc)
}