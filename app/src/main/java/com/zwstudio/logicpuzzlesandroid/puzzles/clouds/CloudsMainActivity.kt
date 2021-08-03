package com.zwstudio.logicpuzzlesandroid.puzzles.clouds

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CloudsMainActivity : GameMainActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState>() {
    private val document: CloudsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CloudsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CloudsGameActivity::class.java))
    }
}

class CloudsOptionsActivity : GameOptionsActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState>() {
    private val document: CloudsDocument by inject()
    override val doc get() = document
}

class CloudsHelpActivity : GameHelpActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState>() {
    private val document: CloudsDocument by inject()
    override val doc get() = document
}