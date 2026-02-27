package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FussyWaiterGameActivity : GameGameActivity<FussyWaiterGame, FussyWaiterDocument, FussyWaiterGameMove, FussyWaiterGameState>() {
    private val document: FussyWaiterDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FussyWaiterGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FussyWaiterHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FussyWaiterGame(level.layout, this, doc)
}