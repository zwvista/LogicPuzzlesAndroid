package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CastleBaileyMainActivity : GameMainActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    private val document: CastleBaileyDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CastleBaileyOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CastleBaileyGameActivity::class.java))
    }
}

class CastleBaileyOptionsActivity : GameOptionsActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    private val document: CastleBaileyDocument by inject()
    override val doc get() = document
}

class CastleBaileyHelpActivity : GameHelpActivity<CastleBaileyGame, CastleBaileyDocument, CastleBaileyGameMove, CastleBaileyGameState>() {
    private val document: CastleBaileyDocument by inject()
    override val doc get() = document
}
