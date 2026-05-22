package com.zwstudio.logicpuzzlesandroid.puzzles.picnic

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PicnicMainActivity : GameMainActivity<PicnicGame, PicnicDocument, PicnicGameMove, PicnicGameState>() {
    private val document: PicnicDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PicnicOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PicnicGameActivity::class.java))
    }
}

class PicnicOptionsActivity : GameOptionsActivity<PicnicGame, PicnicDocument, PicnicGameMove, PicnicGameState>() {
    private val document: PicnicDocument by inject()
    override val doc get() = document
}

class PicnicHelpActivity : GameHelpActivity<PicnicGame, PicnicDocument, PicnicGameMove, PicnicGameState>() {
    private val document: PicnicDocument by inject()
    override val doc get() = document
}