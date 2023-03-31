/*
package com.example.realtimedatabasereusuablecodedoc

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CouponAdapter (val items: List<ListItem>) : RecyclerView.Adapter<BaseViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return items[position].getListItemType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ListItem.Type.TypeA.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_a, parent, false)
                return ViewHolderA(view)
            }
            ListItem.Type.TypeB.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_b, parent, false)
                return ViewHolderB(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_a, parent, false)
                return ViewHolderB(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

interface ListItem {
    enum class Type(value: Int) {GeneralCoupon(0), SpecificCoupon(1) }
    fun getListItemType(): Int
}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: ListItem)
}
 */