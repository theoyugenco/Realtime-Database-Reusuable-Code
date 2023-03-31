package com.example.realtimedatabasereusuablecodedoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CouponAdapter (val items: MutableList<ListItem>) : RecyclerView.Adapter<BaseViewHolder>() {
    var onItemClick: ((Any) -> Unit)? = null
    override fun getItemViewType(position: Int): Int {
        return items[position].getListItemType()
    }

    class ViewHolderA(itemView: View) : BaseViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.general_text)

        override fun bind(item: ListItem) {
            val itemA = item as GeneralCoupon
            textView.text = itemA.textA
        }
    }

    class ViewHolderB(itemView: View) : BaseViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.specific_text)

        override fun bind(item: ListItem) {
            val itemB = item as SpecificCoupon
            textView.text = itemB.textA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ListItem.Type.GeneralCoupon.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.general_coupon, parent, false)
                return ViewHolderA(view)
            }
            ListItem.Type.SpecificCoupon.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.specific_coupon, parent, false)
                return ViewHolderB(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.general_coupon, parent, false)
                return ViewHolderB(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(items[position])

        val coupon = items[position]

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(coupon)
        }
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