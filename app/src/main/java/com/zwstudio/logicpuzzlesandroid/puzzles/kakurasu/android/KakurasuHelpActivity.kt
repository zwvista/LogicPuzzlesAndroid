package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.data.KakurasuDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
class KakurasuHelpActivity : GameHelpActivity<KakurasuGame?, KakurasuDocument?, KakurasuGameMove?, KakurasuGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: KakurasuDocument? = null
    override fun doc() = document!!
}