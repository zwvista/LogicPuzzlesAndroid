package com.zwstudio.logicpuzzlesandroid.puzzles.branches

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BranchesGameActivity : GameGameActivity<BranchesGame, BranchesDocument, BranchesGameMove, BranchesGameState>() {
    private val document: BranchesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BranchesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BranchesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BranchesGame(level.layout, this, doc)
}