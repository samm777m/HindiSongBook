package com.yourapp.hindisongbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlphabetIndexHelper {

    class IndexAdapter(
        private val letters: List<String>,
        private val onLetterClick: (String) -> Unit
    ) : RecyclerView.Adapter<IndexAdapter.IndexViewHolder>() {

        class IndexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val letterTextView: TextView =
                itemView.findViewById(R.id.letterTextView)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): IndexViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_alpha_index, parent, false)

            return IndexViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: IndexViewHolder,
            position: Int
        ) {
            val letter = letters[position]

            holder.letterTextView.text = letter

            holder.itemView.setOnClickListener {
                onLetterClick(letter)
            }
        }

        override fun getItemCount(): Int {
            return letters.size
        }
    }
}
