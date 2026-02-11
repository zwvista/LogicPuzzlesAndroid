package com.zwstudio.logicpuzzlesandroid.puzzles.yalooniq

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class YalooniqGameActivity : GameGameActivity<YalooniqGame, YalooniqDocument, YalooniqGameMove, YalooniqGameState>() {
    private val document: YalooniqDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = YalooniqGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, YalooniqHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        YalooniqGame(level.layout, this, doc)
}