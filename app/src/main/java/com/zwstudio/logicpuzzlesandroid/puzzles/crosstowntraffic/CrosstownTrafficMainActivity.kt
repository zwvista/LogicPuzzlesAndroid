package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CrosstownTrafficMainActivity : GameMainActivity<CrosstownTrafficGame, CrosstownTrafficDocument, CrosstownTrafficGameMove, CrosstownTrafficGameState>() {
    private val document: CrosstownTrafficDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CrosstownTrafficOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CrosstownTrafficGameActivity::class.java))
    }
}

class CrosstownTrafficOptionsActivity : GameOptionsActivity<CrosstownTrafficGame, CrosstownTrafficDocument, CrosstownTrafficGameMove, CrosstownTrafficGameState>() {
    private val document: CrosstownTrafficDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class CrosstownTrafficHelpActivity : GameHelpActivity<CrosstownTrafficGame, CrosstownTrafficDocument, CrosstownTrafficGameMove, CrosstownTrafficGameState>() {
    private val document: CrosstownTrafficDocument by inject()
    override val doc get() = document
}