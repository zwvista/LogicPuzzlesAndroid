package com.zwstudio.logicpuzzlesandroid.puzzles.culturedbranches

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CulturedBranchesMainActivity : GameMainActivity<CulturedBranchesGame, CulturedBranchesDocument, CulturedBranchesGameMove, CulturedBranchesGameState>() {
    private val document: CulturedBranchesDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CulturedBranchesOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CulturedBranchesGameActivity::class.java))
    }
}

class CulturedBranchesOptionsActivity : GameOptionsActivity<CulturedBranchesGame, CulturedBranchesDocument, CulturedBranchesGameMove, CulturedBranchesGameState>() {
    private val document: CulturedBranchesDocument by inject()
    override val doc get() = document
}

class CulturedBranchesHelpActivity : GameHelpActivity<CulturedBranchesGame, CulturedBranchesDocument, CulturedBranchesGameMove, CulturedBranchesGameState>() {
    private val document: CulturedBranchesDocument by inject()
    override val doc get() = document
}