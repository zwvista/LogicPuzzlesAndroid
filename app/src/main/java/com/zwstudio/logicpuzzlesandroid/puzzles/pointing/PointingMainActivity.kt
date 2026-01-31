package com.zwstudio.logicpuzzlesandroid.puzzles.pointing

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PointingMainActivity : GameMainActivity<PointingGame, PointingDocument, PointingGameMove, PointingGameState>() {
    private val document: PointingDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PointingOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PointingGameActivity::class.java))
    }
}

class PointingOptionsActivity : GameOptionsActivity<PointingGame, PointingDocument, PointingGameMove, PointingGameState>() {
    private val document: PointingDocument by inject()
    override val doc get() = document
}

class PointingHelpActivity : GameHelpActivity<PointingGame, PointingDocument, PointingGameMove, PointingGameState>() {
    private val document: PointingDocument by inject()
    override val doc get() = document
}
