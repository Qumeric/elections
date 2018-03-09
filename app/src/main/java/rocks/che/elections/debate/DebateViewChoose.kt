package rocks.che.elections.debate

import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.SeekBar
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.onTouch
import rocks.che.elections.R
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.bus

enum class SourceSlider { OPPONENTS, GROUPS}

class DebateViewChoose: DefaultView<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>
    private lateinit var groupsBar: SeekBar
    private lateinit var opponentsBar: SeekBar

    override fun createView(ui: AnkoContext<DebateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            gameTextView(20) {
                textResource = R.string.debate
            }.lparams(weight = 0.05f, height = 0)

            imageView {
                backgroundResource = R.color.blue
            }.lparams(weight = 0.005f, height = 0, width = dip(120))

            space().lparams(weight = 0.03f, height = 0)

            gameTextView(12) {
                textResource = R.string.debate_choose_description
            }.lparams(weight = 0.1f, height = 0)

            space().lparams(weight = 0.03f, height = 0)

            gameTextView(10) {
                textResource = R.string.group_slider_annotation
            }.lparams(weight = 0.05f, height = 0)
            groupsBar = seekBar {
                splitTrack = false
                thumb = ContextCompat.getDrawable(ctx, R.drawable.seek)
                progressDrawable = ContextCompat.getDrawable(ctx, R.drawable.seek_style)

                progress = max/2
                onTouch { _, _ ->
                    handleSliders(SourceSlider.GROUPS, ui.owner)
                    false
                }
            }.lparams(weight = 0.04f, height = 0, width = matchParent)

            space().lparams(weight = 0.03f, height = 0)

            gameTextView(10) {
                textResource = R.string.opponent_slider_annotation
            }.lparams(weight = 0.05f, height = 0) { topMargin = dip(40)}
            opponentsBar = seekBar {
                splitTrack = false
                thumb = ContextCompat.getDrawable(ctx, R.drawable.seek)
                progressDrawable = ContextCompat.getDrawable(ctx, R.drawable.seek_style)

                progress = max/2
                onTouch { _, _ ->
                    handleSliders(SourceSlider.OPPONENTS, ui.owner)
                    false
                }
            }.lparams(weight = 0.04f, height = 0, width = matchParent)

            space().lparams(weight = 0.07f, height = 0)

            themedButton(theme = R.style.button) {
                textResource = R.string.next
                onClick {
                    bus.post(DebateTimeDistributionUpdate(groupsBar.progress, opponentsBar.progress))
                    ui.owner.nextStage()
                }
            }.lparams(weight = 0.07f, height = 0, width = dip(180))
        }
    }

    private fun handleSliders(slider: SourceSlider, ctx: DebateActivity) {
        if (slider == SourceSlider.GROUPS) {
            opponentsBar.progress = ((ctx.maxMinutes-groupsBar.progress.toFloat()/groupsBar.max*ctx.maxMinutes)/ctx.maxMinutes*opponentsBar.max).toInt()
        } else {
            groupsBar.progress = ((ctx.maxMinutes-opponentsBar.progress.toFloat()/opponentsBar.max*ctx.maxMinutes)/ctx.maxMinutes*groupsBar.max).toInt()
        }
    }
}
