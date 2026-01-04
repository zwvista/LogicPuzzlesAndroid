package com.zwstudio.logicpuzzlesandroid.puzzles.lakesandmeadows

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LakesAndMeadowsGameActivity : GameGameActivity<LakesAndMeadowsGame, LakesAndMeadowsDocument, LakesAndMeadowsGameMove, LakesAndMeadowsGameState>() {
    private val document: LakesAndMeadowsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LakesAndMeadowsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LakesAndMeadowsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LakesAndMeadowsGame(level.layout, this, doc)
}
