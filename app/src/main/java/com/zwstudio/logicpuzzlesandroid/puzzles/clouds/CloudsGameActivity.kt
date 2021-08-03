package com.zwstudio.logicpuzzlesandroid.puzzles.clouds

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CloudsGameActivity : GameGameActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState>() {
    private val document: CloudsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CloudsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CloudsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CloudsGame(level.layout, this, doc)
}