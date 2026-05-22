package com.zwstudio.logicpuzzlesandroid.puzzles.banquet

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BanquetGameActivity : GameGameActivity<BanquetGame, BanquetDocument, BanquetGameMove, BanquetGameState>() {
    private val document: BanquetDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BanquetGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BanquetHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BanquetGame(level.layout, this, doc)
}