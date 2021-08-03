package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class KakuroMainActivity : GameMainActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    private val document: KakuroDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, KakuroOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, KakuroGameActivity::class.java))
    }
}

class KakuroOptionsActivity : GameOptionsActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    private val document: KakuroDocument by inject()
    override val doc get() = document
}

class KakuroHelpActivity : GameHelpActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    private val document: KakuroDocument by inject()
    override val doc get() = document
}