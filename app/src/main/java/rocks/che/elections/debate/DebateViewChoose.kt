package rocks.che.elections.debate

import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.view.View
import android.widget.SeekBar
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.onTouch
import rocks.che.elections.R

enum class SourceSlider { OPPONENTS, GROUPS}

class DebateViewChoose : AnkoComponent<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>
    private lateinit var groupsBar: SeekBar
    private lateinit var opponentsBar: SeekBar

    override fun createView(ui: AnkoContext<DebateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            textView {
                textResource = R.string.debate
                typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                textSize = dip(20).toFloat()
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }.lparams(weight = 0.06f, height = 0)

            imageView {
                backgroundResource = R.color.blue
            }.lparams(weight = 0.012f, height = 0, width = dip(120))

            textView {
                textResource = R.string.debate_choose_description
                typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
                textSize = dip(12).toFloat()
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }.lparams(weight = 0.1f, height = 0)

            groupsBar = seekBar {
                progress = max/2
                onTouch { v, event ->
                    handleSliders(SourceSlider.GROUPS, ctx as DebateActivity)
                    false
                }
            }.lparams(weight = 0.1f, height = 0, width = matchParent)

            opponentsBar = seekBar {
                progress = max/2
                onTouch { v, event ->
                    handleSliders(SourceSlider.OPPONENTS, ctx as DebateActivity)
                    false
                }
            }.lparams(weight = 0.1f, height = 0, width = matchParent)


            themedButton(theme = R.style.button) {
                textResource = R.string.next
                onClick { (ctx as DebateActivity).nextStage() }
            }.lparams(weight = 0.14f, height = 0, width = dip(180))
        }
    }

    private fun handleSliders(slider: SourceSlider, ctx: DebateActivity) {
        if (slider == SourceSlider.GROUPS) {
            opponentsBar.progress = ((ctx.minutes-groupsBar.progress.toFloat()/groupsBar.max*ctx.maxMinutes)/ctx.maxMinutes*opponentsBar.max).toInt()
        } else {
            groupsBar.progress = ((ctx.minutes-opponentsBar.progress.toFloat()/opponentsBar.max*ctx.maxMinutes)/ctx.maxMinutes*groupsBar.max).toInt()
        }
    }
}
