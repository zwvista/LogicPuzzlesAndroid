package com.zwstudio.logicpuzzlesandroid.puzzles.zenlandscaper

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ZenLandscaperMainActivity : GameMainActivity<ZenLandscaperGame, ZenLandscaperDocument, ZenLandscaperGameMove, ZenLandscaperGameState>() {
    private val document: ZenLandscaperDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ZenLandscaperOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ZenLandscaperGameActivity::class.java))
    }
}

class ZenLandscaperOptionsActivity : GameOptionsActivity<ZenLandscaperGame, ZenLandscaperDocument, ZenLandscaperGameMove, ZenLandscaperGameState>() {
    private val document: ZenLandscaperDocument by inject()
    override val doc get() = document
}

class ZenLandscaperHelpActivity : GameHelpActivity<ZenLandscaperGame, ZenLandscaperDocument, ZenLandscaperGameMove, ZenLandscaperGameState>() {
    private val document: ZenLandscaperDocument by inject()
    override val doc get() = document
}