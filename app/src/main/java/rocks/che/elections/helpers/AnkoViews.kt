package rocks.che.elections.helpers

import android.content.Context
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewManager
import android.widget.TextView
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import nl.dionsegijn.konfetti.KonfettiView
import org.jetbrains.anko._GridLayout
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.textColor
import pl.droidsonroids.gif.GifImageView
import rocks.che.elections.R

class SquareGridLayout(ctx: Context): _GridLayout(ctx) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        if (width > height) {
            width = height
        } else {
            height = width
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }
}

inline fun ViewManager.squareGridLayout(init: SquareGridLayout.() -> Unit): SquareGridLayout {
    return ankoView({ SquareGridLayout(it) }, theme = 0, init = init)
}

inline fun ViewManager.sparkView(init: SparkView.() -> Unit): SparkView {
    return ankoView({ SparkView(it) }, theme = 0, init = init)
}

inline fun ViewManager.cardView(init: CardView.() -> Unit): CardView {
    return ankoView({ CardView(it) }, theme = 0, init = init)
}

inline fun ViewManager.gifImageView(init: GifImageView.() -> Unit): GifImageView {
    return ankoView({ GifImageView(it) }, theme = 0, init = init)
}

inline fun ViewManager.konfettiView(init: KonfettiView.() -> Unit): KonfettiView {
    return ankoView({ KonfettiView(it)}, theme = 0, init = init)
}

inline fun ViewManager.gameTextView(size: Int = 0, color: Int = 0, text: String?=null, init: TextView.() -> Unit) : TextView {
    return ankoView({
        val tv = TextView(it)
        tv.typeface = ResourcesCompat.getFont(it, R.font.mfred)
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        if (size > 0)   tv.textSize = size.toFloat()
        if (color != 0) tv.textColor = ContextCompat.getColor(it, color)
        if (text != null) tv.text = text
        tv.elevation = 100f
        tv
    }, theme = 0, init = init)
}

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