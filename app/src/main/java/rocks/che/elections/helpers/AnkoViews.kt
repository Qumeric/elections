package rocks.che.elections.helpers

import android.content.Context
import android.graphics.RectF
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewManager
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
        val size = min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))

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

interface DefaultView<T> : AnkoComponent<T>, AnkoLogger
