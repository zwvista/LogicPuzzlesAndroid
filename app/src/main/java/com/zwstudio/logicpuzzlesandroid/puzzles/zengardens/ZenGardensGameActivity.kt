package com.zwstudio.logicpuzzlesandroid.puzzles.zengardens

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ZenGardensGameActivity : GameGameActivity<ZenGardensGame, ZenGardensDocument, ZenGardensGameMove, ZenGardensGameState>() {
    private val document: ZenGardensDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ZenGardensGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ZenGardensHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        ZenGardensGame(level.layout, this, doc)
}