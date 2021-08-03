package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TheOddBrickGameActivity : GameGameActivity<TheOddBrickGame, TheOddBrickDocument, TheOddBrickGameMove, TheOddBrickGameState>() {
    private val document: TheOddBrickDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TheOddBrickGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TheOddBrickHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TheOddBrickGame(level.layout, this, doc)
}