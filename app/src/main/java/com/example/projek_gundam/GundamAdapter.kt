package com.example.projek_gundam


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GundamAdapter(
    private val gundamList: MutableList<Gundam>,
    private val onClick: (Gundam) -> Unit
) : RecyclerView.Adapter<
        GundamAdapter.GundamViewHolder>() {

    fun updateData(newList: List<Gundam>) {

        gundamList.clear()
        gundamList.addAll(newList)

        notifyDataSetChanged()
    }

    class GundamViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val image: ImageView =
            view.findViewById(
                R.id.imgGundam
            )

        val name: TextView =
            view.findViewById(
                R.id.tvName
            )

        val pilot: TextView =
            view.findViewById(
                R.id.tvPilot
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GundamViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_gundam,
                    parent,
                    false
                )

        return GundamViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GundamViewHolder,
        position: Int
    ) {

        val gundam =
            gundamList[position]

        holder.name.text =
            gundam.name

        holder.pilot.text =
            firstPilot(gundam.pilots)

        Glide.with(
            holder.itemView.context
        )
            .load(gundam.imgUrl)
            .placeholder(
                android.R.drawable.ic_menu_gallery
            )
            .error(
                android.R.drawable.ic_menu_report_image
            )
            .centerCrop()
            .into(holder.image)

        holder.itemView.setOnClickListener {

            onClick(gundam)
        }
    }

    override fun getItemCount(): Int {
        return gundamList.size
    }

    private fun firstPilot(
        pilots: String
    ): String {

        if (pilots.isBlank()) {
            return "Pilot data unavailable"
        }

        return pilots
            .substringBefore(",")
            .trim()
    }
}