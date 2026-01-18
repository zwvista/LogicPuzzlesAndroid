package com.zwstudio.logicpuzzlesandroid.puzzles.joinme

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class JoinMeMainActivity : GameMainActivity<JoinMeGame, JoinMeDocument, JoinMeGameMove, JoinMeGameState>() {
    private val document: JoinMeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, JoinMeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, JoinMeGameActivity::class.java))
    }
}

class JoinMeOptionsActivity : GameOptionsActivity<JoinMeGame, JoinMeDocument, JoinMeGameMove, JoinMeGameState>() {
    private val document: JoinMeDocument by inject()
    override val doc get() = document
}

class JoinMeHelpActivity : GameHelpActivity<JoinMeGame, JoinMeDocument, JoinMeGameMove, JoinMeGameState>() {
    private val document: JoinMeDocument by inject()
    override val doc get() = document
}