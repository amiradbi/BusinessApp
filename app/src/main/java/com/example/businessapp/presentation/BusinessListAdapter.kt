package com.example.businessapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.businessapp.R
import com.example.businessapp.domain.model.Business
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.business_item.view.*

class BusinessListAdapter(private val action: (lat: Double, lng: Double) -> Unit) :
    RecyclerView.Adapter<BusinessListAdapter.CustomAdapter>() {

    private val picasso = Picasso.get()

    private var list: List<Business> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.business_item, parent, false)
        return CustomAdapter(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CustomAdapter, position: Int) {
        holder.bind(list[position], action, picasso)
    }

    // Add a list of items
    fun updateList(list: List<Business>) {
        this.list = list
        notifyDataSetChanged()
    }

    class CustomAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(business: Business, action: (lat: Double, lng: Double) -> Unit, picasso: Picasso) {
            itemView.txtName.text = business.name
            itemView.txtAddress.text = business.address.line.toString().drop(1)
                .dropLast(1) // remove start and end brackets
            itemView.txtRate.text = itemView.context.getString(
                R.string.rate_text,
                business.rating.toString(),
                business.reviewCount.toString()
            )

            picasso.load(business.imageUrl)
                .placeholder(R.drawable.ic_store_empty)
                .error(R.drawable.ic_store_empty)
                .fit()
                .centerCrop()
                .into(itemView.businessImage)

            itemView.showOnMapBtn.setOnClickListener {
                action(
                    business.address.latitude,
                    business.address.longitude
                )
            }
        }
    }
}