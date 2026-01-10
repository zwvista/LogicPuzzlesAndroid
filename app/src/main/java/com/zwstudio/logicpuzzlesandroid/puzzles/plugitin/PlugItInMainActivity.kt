package com.zwstudio.logicpuzzlesandroid.puzzles.plugitin

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PlugItInMainActivity : GameMainActivity<PlugItInGame, PlugItInDocument, PlugItInGameMove, PlugItInGameState>() {
    private val document: PlugItInDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PlugItInOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PlugItInGameActivity::class.java))
    }
}

class PlugItInOptionsActivity : GameOptionsActivity<PlugItInGame, PlugItInDocument, PlugItInGameMove, PlugItInGameState>() {
    private val document: PlugItInDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class PlugItInHelpActivity : GameHelpActivity<PlugItInGame, PlugItInDocument, PlugItInGameMove, PlugItInGameState>() {
    private val document: PlugItInDocument by inject()
    override val doc get() = document
}