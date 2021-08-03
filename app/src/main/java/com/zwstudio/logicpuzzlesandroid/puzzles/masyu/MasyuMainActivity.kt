package com.zwstudio.logicpuzzlesandroid.puzzles.masyu

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class MasyuMainActivity : GameMainActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    private val document: MasyuDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, MasyuOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, MasyuGameActivity::class.java))
    }
}

class MasyuOptionsActivity : GameOptionsActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    private val document: MasyuDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class MasyuHelpActivity : GameHelpActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    private val document: MasyuDocument by inject()
    override val doc get() = document
}