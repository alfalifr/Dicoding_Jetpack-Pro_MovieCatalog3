package sidev.app.course.dicoding.moviecatalog1.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.databinding.ItemMainListBinding

class ShowViewHolder(
    private val binding: ItemMainListBinding,
    private val onItemClick: OnItemClick?= null
): RecyclerView.ViewHolder(binding.root) {

    fun interface OnItemClick {
        fun onItemClick(pos: Int, data: Show)
    }

    fun bind(data: Show) = binding.apply {
        tvTitle.text = data.title
        tvRelease.text = data.getFormattedDate()
        tvPb.text = root.context.getString(R.string.percent, data.rating)
        pb.max = 100
        pb.progress = data.rating.times(10).toInt()
        Glide.with(iv)
            .load(data.imgUrl_300x450())
            .into(iv)

        root.setOnClickListener {
            onItemClick?.onItemClick(absoluteAdapterPosition, data)
        }
    }
}