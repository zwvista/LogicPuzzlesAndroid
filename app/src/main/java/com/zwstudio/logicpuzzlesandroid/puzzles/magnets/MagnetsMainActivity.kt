package com.zwstudio.logicpuzzlesandroid.puzzles.magnets

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

open class MagnetsMainActivity : GameMainActivity<MagnetsGame, MagnetsDocument, MagnetsGameMove, MagnetsGameState>() {
    private val document: MagnetsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, MagnetsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, MagnetsGameActivity::class.java))
    }
}

open class MagnetsOptionsActivity : GameOptionsActivity<MagnetsGame, MagnetsDocument, MagnetsGameMove, MagnetsGameState>() {
    private val document: MagnetsDocument by inject()
    override val doc get() = document
}

open class MagnetsHelpActivity : GameHelpActivity<MagnetsGame, MagnetsDocument, MagnetsGameMove, MagnetsGameState>() {
    private val document: MagnetsDocument by inject()
    override val doc get() = document
}