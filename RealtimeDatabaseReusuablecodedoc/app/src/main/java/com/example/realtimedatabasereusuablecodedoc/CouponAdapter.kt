package com.example.realtimedatabasereusuablecodedoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
Kenneth Valero
An adapter that gathers all eligible coupons for an order.
 */
class CouponAdapter (val items: MutableList<ListItem>) : RecyclerView.Adapter<BaseViewHolder>() {
    var onItemClick: ((Any) -> Unit)? = null
    /*
    Retrieves the type of coupon of the current coupon
     */
    override fun getItemViewType(position: Int): Int {
        return items[position].getListItemType()
    }

    /*
    Sets up the on screen display for general coupons
     */
    class ViewHolderA(itemView: View) : BaseViewHolder(itemView) {
        private val description: TextView = itemView.findViewById(R.id.textLocation)
        private val expDate: TextView = itemView.findViewById(R.id.textAddress)

        override fun bind(item: ListItem) {
            val itemA = item as GeneralCoupon
            description.text = itemA.quantityNeeded.toString()
            expDate.text = itemA.expirationDate
        }
    }

    /*
    Sets up the on screen display for specific item coupons
     */
    class ViewHolderB(itemView: View) : BaseViewHolder(itemView) {
        private val description: TextView = itemView.findViewById(R.id.textLocation)
        private val expDate: TextView = itemView.findViewById(R.id.textAddress)

        override fun bind(item: ListItem) {
            val itemB = item as SpecificCoupon
            description.text = itemB.couponFor
            expDate.text = itemB.expirationDate
        }
    }

    /*
    Depending on which coupon type the current coupon is, will load either the general coupon
    view or the specific item coupon view
     */
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

    /*
    Utilizes the bind override to set cards on the recyclerview to the current coupon's
    parameters
     */
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(items[position])

        val coupon = items[position]

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(coupon)
        }
    }

    /*
    Gets the total number of coupons
     */
    override fun getItemCount(): Int {
        return items.size
    }
}

/*
Abstract class to allow different bind functions for the two different coupon types.
 */
abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: ListItem)
}