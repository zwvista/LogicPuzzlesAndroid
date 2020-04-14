package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.data.KakuroDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class KakuroOptionsActivity : GameOptionsActivity<KakuroGame?, KakuroDocument?, KakuroGameMove?, KakuroGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: KakuroDocument? = null
    override fun doc() = document!!
}