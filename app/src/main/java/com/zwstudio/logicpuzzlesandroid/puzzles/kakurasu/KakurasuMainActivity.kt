package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class KakurasuMainActivity : GameMainActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState>() {
    @Bean
    protected lateinit var document: KakurasuDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        KakurasuOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        KakurasuGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class KakurasuOptionsActivity : GameOptionsActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState>() {
    @Bean
    protected lateinit var document: KakurasuDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class KakurasuHelpActivity : GameHelpActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState>() {
    @Bean
    protected lateinit var document: KakurasuDocument
    override val doc get() = document
}