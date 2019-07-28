package rocks.che.elections

import android.content.Context
import android.graphics.Typeface
import androidx.core.widget.TextViewCompat
import android.view.Gravity
import android.widget.GridLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.*
import rocks.che.elections.logic.*

fun _LinearLayout.header(weight: Float) = verticalLayout {
    gravity = Gravity.CENTER
    gameTextView(20) {
        textResource = R.string.choose_candidate
    }
}.lparams(height = 0, width = matchParent, weight = weight)

fun _LinearLayout.candidateGrid(ctx: Context, weight: Float) = gridLayout {
    rowCount = 3
    columnCount = 2
    alignmentMode = GridLayout.ALIGN_MARGINS
    padding = dip(12)

    val candidates = loadCandidates(resources)
    for ((index, candidate) in candidates.withIndex()) {
        val row = index / columnCount
        val col = index % columnCount
        candidateCard(ctx, candidate, candidates).lparams{
            width = 0
            height = 0
            leftMargin = dip(8)
            rightMargin = dip(8)
            bottomMargin = dip(16)
            columnSpec = GridLayout.spec(col, 1f)
            rowSpec = GridLayout.spec(row, 1f)
        }
    }
}.lparams(width = matchParent, height = 0, weight = 0.9f)

fun _GridLayout.candidateCard(
    ctx: Context, candidate: Candidate, candidates: List<Candidate>) = cardView {
    verticalLayout {
        weightSum = 1f
        candidateInfo(ctx, candidate, 0.8f)
        candidateButton(ctx, candidate, 0.2f).onClick {
            if (!candidate.isLocked) {
                inActivityChange = true
                ctx.startActivity<HighlightsActivity>("gamestate" to Gamestate(candidate,
                    candidates as MutableList<Candidate>, loadQuestions(resources)))
            }
        }
    }.lparams(width = matchParent, height = matchParent)
    if (candidate.isLocked) {
        lockOverlay()
    }
}

fun _LinearLayout.candidateInfo(ctx: Context, candidate: Candidate, weight: Float) {
    linearLayout {
        gravity = Gravity.CENTER
        imageView(candidate.resource).lparams(width = dip(80))
        verticalLayout {
            gravity = Gravity.END
            for ((group, value) in candidate.opinions) {
                gameTextView(text = "${group.toMaybeRussian(ctx.configuration.locale.toString())}: $value")
            }
        }
    }.lparams(weight = weight, height = 0, width = matchParent)
}

fun _LinearLayout.candidateButton(ctx: Context, candidate: Candidate, weight: Float) = button {
    text = candidate.name
    typeface = Typeface.createFromAsset(ctx.assets, "mfred.ttf")
    textSize = 20f
    backgroundResource = if (candidate.isLocked) R.color.gray else R.color.blue
    TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
}.lparams(width = matchParent, height = 0, weight = weight)

fun _CardView.lockOverlay() {
    imageView {
        backgroundResource = R.color.black
        alpha = 0.5f
    }.lparams(width = matchParent, height = matchParent)
    imageView {
        imageResource = R.drawable.ic_locked
    }.lparams(width = dip(50), height = dip(50), gravity = Gravity.CENTER)
}

class ChooseCandidateView() : DefaultView<ChooseCandidateActivity> {
    private lateinit var ankoContext: AnkoContext<ChooseCandidateActivity>

    override fun createView(ui: AnkoContext<ChooseCandidateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            weightSum = 1f

            header(0.1f)
            candidateGrid(ctx, 0.9f)
        }
    }
}
