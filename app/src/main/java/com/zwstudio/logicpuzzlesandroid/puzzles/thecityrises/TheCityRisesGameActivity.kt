package com.zwstudio.logicpuzzlesandroid.puzzles.thecityrises

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TheCityRisesGameActivity : GameGameActivity<TheCityRisesGame, TheCityRisesDocument, TheCityRisesGameMove, TheCityRisesGameState>() {
    private val document: TheCityRisesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TheCityRisesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TheCityRisesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TheCityRisesGame(level.layout, this, doc)
}