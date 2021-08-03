package com.zwstudio.logicpuzzlesandroid.puzzles.orchards

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class OrchardsGameActivity : GameGameActivity<OrchardsGame, OrchardsDocument, OrchardsGameMove, OrchardsGameState>() {
    private val document: OrchardsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = OrchardsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, OrchardsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        OrchardsGame(level.layout, this, doc)
}