package com.zwstudio.logicpuzzlesandroid.puzzles.makethedifference

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class MaketheDifferenceGameActivity : GameGameActivity<MaketheDifferenceGame, MaketheDifferenceDocument, MaketheDifferenceGameMove, MaketheDifferenceGameState>() {
    private val document: MaketheDifferenceDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = MaketheDifferenceGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, MaketheDifferenceHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        MaketheDifferenceGame(level.layout, this, doc)
}