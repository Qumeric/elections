package rocks.che.elections.helpers

import android.content.Context
import android.content.res.Resources
import android.graphics.RectF
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import nl.dionsegijn.konfetti.KonfettiView
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko._GridLayout
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.textColor
import pl.droidsonroids.gif.GifImageView
import rocks.che.elections.R
import kotlin.math.min

class SquareGridLayout(ctx: Context) : _GridLayout(ctx) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(MeasureSpec.getSize(widthMeasureSpec),  MeasureSpec.getSize(heightMeasureSpec))

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        )
    }
}

inline fun ViewManager.squareGridLayout(init: SquareGridLayout.() -> Unit): SquareGridLayout =
    ankoView({ SquareGridLayout(it) }, theme = 0, init = init)

inline fun ViewManager.sparkView(init: SparkView.() -> Unit): SparkView =
    ankoView({ SparkView(it) }, theme = 0, init = init)

inline fun ViewManager.cardView(init: CardView.() -> Unit): CardView =
    ankoView({ CardView(it) }, theme = R.style.CardView, init = init)

inline fun ViewManager.gifImageView(init: GifImageView.() -> Unit): GifImageView =
    ankoView({ GifImageView(it) }, theme = 0, init = init)

inline fun ViewManager.konfettiView(init: KonfettiView.() -> Unit): KonfettiView =
    ankoView({ KonfettiView(it) }, theme = 0, init = init)

inline fun ViewManager.gameTextView(size: Int? = null, color: Int? = null, text: String? = null, autoResize: Boolean = true,
                                    init: AppCompatTextView.() -> Unit): AppCompatTextView = ankoView(
    {
        val tv = AppCompatTextView(it)
        tv.typeface = Typeface.createFromAsset(it.assets, "mfred.ttf")
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        if (color != null) tv.textColor = ContextCompat.getColor(it, color)
        if (text != null) tv.text = text
        if (size != null) tv.textSize = size * 2.5f // FIXME shouldn't have a constant
        else if (autoResize) TextViewCompat.setAutoSizeTextTypeWithDefaults(tv, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        tv.elevation = 100f
        tv
    }, theme = 0, init = init)


class MyAdapter(private val yData: FloatArray) : SparkAdapter() {
    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any? {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }

    override fun getDataBounds(): RectF {
        val bounds = super.getDataBounds()
        bounds.bottom = 100f
        bounds.top = 0f
        return bounds
    }
}

interface DefaultView<in T> : AnkoComponent<T>, AnkoLogger

val groupToResource = mapOf(
    "media" to R.drawable.ic_media,
    "business" to R.drawable.ic_business,
    "foreign" to R.drawable.ic_foreign,
    "workers" to R.drawable.ic_workers,
    "military" to R.drawable.ic_military
)

val candidateResourceNameToResource = mapOf(
    "candidate_navalny" to R.drawable.candidate_navalny,
    "candidate_sobchak" to R.drawable.candidate_sobchak,
    "candidate_zhirinovsky" to R.drawable.candidate_zhirinovsky,
    "candidate_putin" to R.drawable.candidate_putin,
    "candidate_grudinin" to R.drawable.candidate_grudinin,
    "candidate_yavlinsky" to R.drawable.candidate_yavlinsky
)

const val h1TextSize = 20
const val h2TextSize = 16
const val ordinaryTextSize = 10

fun scaleView(v: View, startScale: Float = 0f, endScale: Float = 1f, duration: Long = 2500,
              pivotX: Float = 0.5f, pivotY: Float = 0.5f) {
    val anim = ScaleAnimation(
        startScale, endScale, // X axis scaling
        startScale, endScale, // Y axis scaling
        Animation.RELATIVE_TO_SELF, pivotX,
        Animation.RELATIVE_TO_SELF, pivotY)
    anim.fillAfter = true // Needed to keep the result of the animation
    anim.duration = duration
    v.startAnimation(anim)
}

fun isRussian(resources: Resources) = isRussian(resources.configuration.locale.toString())

fun isRussian(locale: String): Boolean {
    val part = locale.substring(0, 2)
    // FIXME check collisions
    return (part == "ru" || part == "uk" || part == "be")
}

fun String.toMaybeRussian(locale: String): String {
    if (isRussian(locale)) {
        return when (this.toLowerCase()) {
            "military" -> "военные"
            "foreign" -> "мир"
            "media" -> "пресса"
            "business" -> "бизнес"
            "workers" -> "трудящиеся"
            else -> this
        }
    }
    return this
}
