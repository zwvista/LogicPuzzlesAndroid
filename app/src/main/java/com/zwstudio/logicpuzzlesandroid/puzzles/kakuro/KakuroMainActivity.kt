package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class KakuroMainActivity : GameMainActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    @Bean
    protected lateinit var document: KakuroDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        KakuroOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        KakuroGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class KakuroOptionsActivity : GameOptionsActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    @Bean
    protected lateinit var document: KakuroDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class KakuroHelpActivity : GameHelpActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState>() {
    @Bean
    protected lateinit var document: KakuroDocument
    override val doc get() = document
}