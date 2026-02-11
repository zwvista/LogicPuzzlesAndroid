package com.zwstudio.logicpuzzlesandroid.puzzles.mirrors

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class MirrorsMainActivity : GameMainActivity<MirrorsGame, MirrorsDocument, MirrorsGameMove, MirrorsGameState>() {
    private val document: MirrorsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, MirrorsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, MirrorsGameActivity::class.java))
    }
}

class MirrorsOptionsActivity : GameOptionsActivity<MirrorsGame, MirrorsDocument, MirrorsGameMove, MirrorsGameState>() {
    private val document: MirrorsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class MirrorsHelpActivity : GameHelpActivity<MirrorsGame, MirrorsDocument, MirrorsGameMove, MirrorsGameState>() {
    private val document: MirrorsDocument by inject()
    override val doc get() = document
}