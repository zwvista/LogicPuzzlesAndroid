package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class NorthPoleFishingGameActivity : GameGameActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    private val document: NorthPoleFishingDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = NorthPoleFishingGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, NorthPoleFishingHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        NorthPoleFishingGame(level.layout, this, doc)
}
