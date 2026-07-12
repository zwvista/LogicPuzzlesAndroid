package com.zwstudio.logicpuzzlesandroid.puzzles.mirrorsextended

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class MirrorsExtendedGameActivity : GameGameActivity<MirrorsExtendedGame, MirrorsExtendedDocument, MirrorsExtendedGameMove, MirrorsExtendedGameState>() {
    private val document: MirrorsExtendedDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = MirrorsExtendedGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, MirrorsExtendedHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        MirrorsExtendedGame(level.layout, this, doc)
}