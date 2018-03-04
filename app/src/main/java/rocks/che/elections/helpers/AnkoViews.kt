package rocks.che.elections.helpers

import android.content.Context
import android.graphics.RectF
import android.support.v7.widget.CardView
import android.view.ViewManager
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import org.jetbrains.anko._GridLayout
import org.jetbrains.anko.custom.ankoView

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
        );
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

class MyAdapter(val yData: FloatArray) : SparkAdapter() {
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
