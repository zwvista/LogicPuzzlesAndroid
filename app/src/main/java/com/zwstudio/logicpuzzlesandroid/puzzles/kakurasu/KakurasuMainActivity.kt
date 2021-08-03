package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class KakurasuMainActivity : GameMainActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState>() {
    private val document: KakurasuDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, KakurasuOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, KakurasuGameActivity::class.java))
    }
}

class KakurasuOptionsActivity : GameOptionsActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState>() {
    private val document: KakurasuDocument by inject()
    override val doc get() = document
}

class KakurasuHelpActivity : GameHelpActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState>() {
    private val document: KakurasuDocument by inject()
    override val doc get() = document
}