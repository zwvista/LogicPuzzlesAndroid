package com.zwstudio.logicpuzzlesandroid.puzzles.flowerbedshrubs

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FlowerbedShrubsGameActivity : GameGameActivity<FlowerbedShrubsGame, FlowerbedShrubsDocument, FlowerbedShrubsGameMove, FlowerbedShrubsGameState>() {
    private val document: FlowerbedShrubsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FlowerbedShrubsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FlowerbedShrubsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FlowerbedShrubsGame(level.layout, this, doc)
}