package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.data.FutoshikiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class FutoshikiMainActivity : GameMainActivity<FutoshikiGame?, FutoshikiDocument?, FutoshikiGameMove?, FutoshikiGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FutoshikiDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        FutoshikiOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        FutoshikiGameActivity_.intent(this).start()
    }
}