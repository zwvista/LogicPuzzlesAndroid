package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PipemaniaMainActivity : GameMainActivity<PipemaniaGame, PipemaniaDocument, PipemaniaGameMove, PipemaniaGameState>() {
    private val document: PipemaniaDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PipemaniaOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PipemaniaGameActivity::class.java))
    }
}

class PipemaniaOptionsActivity : GameOptionsActivity<PipemaniaGame, PipemaniaDocument, PipemaniaGameMove, PipemaniaGameState>() {
    private val document: PipemaniaDocument by inject()
    override val doc get() = document
}

class PipemaniaHelpActivity : GameHelpActivity<PipemaniaGame, PipemaniaDocument, PipemaniaGameMove, PipemaniaGameState>() {
    private val document: PipemaniaDocument by inject()
    override val doc get() = document
}