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
        private val description: TextView = itemView.findViewById(R.id.textLocation)
        private val expDate: TextView = itemView.findViewById(R.id.textAddress)

        override fun bind(item: ListItem) {
            val itemA = item as GeneralCoupon
            description.text = itemA.quantityNeeded.toString()
            expDate.text = itemA.expirationDate
        }
    }

    class ViewHolderB(itemView: View) : BaseViewHolder(itemView) {
        private val description: TextView = itemView.findViewById(R.id.textLocation)
        private val expDate: TextView = itemView.findViewById(R.id.textAddress)

        override fun bind(item: ListItem) {
            val itemB = item as SpecificCoupon
            description.text = itemB.couponFor
            expDate.text = itemB.expirationDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ListItem.Type.GeneralCoupon.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.checkout_coupon_recycler, parent, false)
                return ViewHolderA(view)
            }
            ListItem.Type.SpecificCoupon.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.checkout_coupon_recycler, parent, false)
                return ViewHolderB(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.checkout_coupon_recycler, parent, false)
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

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: ListItem)
}