package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CloudsAndClearsGameActivity : GameGameActivity<CloudsAndClearsGame, CloudsAndClearsDocument, CloudsAndClearsGameMove, CloudsAndClearsGameState>() {
    private val document: CloudsAndClearsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CloudsAndClearsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CloudsAndClearsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CloudsAndClearsGame(level.layout, this, doc)
}
