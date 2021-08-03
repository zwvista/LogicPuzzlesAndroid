package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class KakuroGameActivity : GameGameActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    private val document: KakuroDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = KakuroGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, KakuroHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        KakuroGame(level.layout, this, doc)
}