package com.zwstudio.logicpuzzlesandroid.puzzles.botanicalpark

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BotanicalParkGameActivity : GameGameActivity<BotanicalParkGame, BotanicalParkDocument, BotanicalParkGameMove, BotanicalParkGameState>() {
    private val document: BotanicalParkDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BotanicalParkGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BotanicalParkHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel): BotanicalParkGame {
        val plantsInEachArea = (level.settings["PlantsInEachArea"] ?: "1").toInt()
        return BotanicalParkGame(level.layout, plantsInEachArea, this, doc)
    }
}