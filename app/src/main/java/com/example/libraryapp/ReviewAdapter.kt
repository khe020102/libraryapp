package com.example.libraryapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val context: Context, private val mList: ArrayList<ReviewData>) : RecyclerView.Adapter<ReviewAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reviewRate: TextView = view.findViewById(R.id.reviewRate)
        val reviewUserid: TextView = view.findViewById(R.id.reviewUserid)
        val reviewTitle: TextView = view.findViewById(R.id.reviewTitle)
        val reviewReview: TextView = view.findViewById(R.id.reviewReview)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.reviewlist, viewGroup, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: CustomViewHolder, position: Int) {
        val reviewData = mList[position]

        viewholder.reviewRate.text = reviewData.review_rate
        viewholder.reviewUserid.text = reviewData.review_userID
        viewholder.reviewTitle.text = reviewData.review_title
        viewholder.reviewReview.text = reviewData.review_review
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
