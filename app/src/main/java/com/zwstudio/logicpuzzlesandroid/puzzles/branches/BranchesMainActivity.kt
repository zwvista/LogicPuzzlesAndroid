package com.zwstudio.logicpuzzlesandroid.puzzles.branches

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class BranchesMainActivity : GameMainActivity<BranchesGame, BranchesDocument, BranchesGameMove, BranchesGameState>() {
    private val document: BranchesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, BranchesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, BranchesGameActivity::class.java))
    }
}

class BranchesOptionsActivity : GameOptionsActivity<BranchesGame, BranchesDocument, BranchesGameMove, BranchesGameState>() {
    private val document: BranchesDocument by inject()
    override val doc get() = document
}

class BranchesHelpActivity : GameHelpActivity<BranchesGame, BranchesDocument, BranchesGameMove, BranchesGameState>() {
    private val document: BranchesDocument by inject()
    override val doc get() = document
}