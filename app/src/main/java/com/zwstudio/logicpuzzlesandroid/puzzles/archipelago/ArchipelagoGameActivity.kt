package com.zwstudio.logicpuzzlesandroid.puzzles.archipelago

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ArchipelagoGameActivity : GameGameActivity<ArchipelagoGame, ArchipelagoDocument, ArchipelagoGameMove, ArchipelagoGameState>() {
    private val document: ArchipelagoDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ArchipelagoGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ArchipelagoHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        ArchipelagoGame(level.layout, this, doc)
}