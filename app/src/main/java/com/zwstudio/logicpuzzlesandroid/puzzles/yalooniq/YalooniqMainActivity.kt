package com.zwstudio.logicpuzzlesandroid.puzzles.yalooniq

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class YalooniqMainActivity : GameMainActivity<YalooniqGame, YalooniqDocument, YalooniqGameMove, YalooniqGameState>() {
    private val document: YalooniqDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, YalooniqOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, YalooniqGameActivity::class.java))
    }
}

class YalooniqOptionsActivity : GameOptionsActivity<YalooniqGame, YalooniqDocument, YalooniqGameMove, YalooniqGameState>() {
    private val document: YalooniqDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class YalooniqHelpActivity : GameHelpActivity<YalooniqGame, YalooniqDocument, YalooniqGameMove, YalooniqGameState>() {
    private val document: YalooniqDocument by inject()
    override val doc get() = document
}