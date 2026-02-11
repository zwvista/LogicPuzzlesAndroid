package com.zwstudio.logicpuzzlesandroid.puzzles.mondrianloop

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class MondrianLoopGameActivity : GameGameActivity<MondrianLoopGame, MondrianLoopDocument, MondrianLoopGameMove, MondrianLoopGameState>() {
    private val document: MondrianLoopDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = MondrianLoopGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, MondrianLoopHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        MondrianLoopGame(level.layout, this, doc)
}