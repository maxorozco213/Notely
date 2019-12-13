package com.example.notely.ui.files

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.notely.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_upload.view.*

class UploadAdapter(items: List<String>, cntx: Context) : RecyclerView.Adapter<UploadAdapter.UploadViewHolder>() {

    private var list = items
    private var context = cntx

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UploadViewHolder, position: Int) {
        val url = list[position]
        Picasso.with(context)
            .load(url)
            .fit()
            .centerInside()
            .into(holder.uploadImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_upload, parent, false)
        return UploadViewHolder(v)
    }

    class UploadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val uploadImg: ImageView = view.img_view_upload
    }
}