package com.zwstudio.logicpuzzlesandroid.puzzles.liarliar

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LiarLiarMainActivity : GameMainActivity<LiarLiarGame, LiarLiarDocument, LiarLiarGameMove, LiarLiarGameState>() {
    private val document: LiarLiarDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LiarLiarOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LiarLiarGameActivity::class.java))
    }
}

class LiarLiarOptionsActivity : GameOptionsActivity<LiarLiarGame, LiarLiarDocument, LiarLiarGameMove, LiarLiarGameState>() {
    private val document: LiarLiarDocument by inject()
    override val doc get() = document
}

class LiarLiarHelpActivity : GameHelpActivity<LiarLiarGame, LiarLiarDocument, LiarLiarGameMove, LiarLiarGameState>() {
    private val document: LiarLiarDocument by inject()
    override val doc get() = document
}