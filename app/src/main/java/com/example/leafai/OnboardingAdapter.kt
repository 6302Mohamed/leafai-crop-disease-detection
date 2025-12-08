package com.example.leafai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView

class OnboardingAdapter(
    private val items: List<OnboardingItem>,
    private val answers: MutableList<String?>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val animationView: LottieAnimationView = view.findViewById(R.id.lottieAnimation)
        val questionText: TextView = view.findViewById(R.id.questionText)
        val spinner: Spinner = view.findViewById(R.id.questionSpinner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.onboarding_item, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = items[position]

        holder.animationView.setAnimation(item.lottieFile)
        holder.animationView.playAnimation()

        holder.questionText.text = item.question

        val adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, item.options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner.adapter = adapter

        // Use holder.adapterPosition to be safe
        val adapterPos = holder.adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: position

        holder.spinner.setSelection(
            answers.getOrNull(adapterPos)?.let { item.options.indexOf(it) } ?: 0
        )
        holder.spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val pos = holder.adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: adapterPos
                if (pos >= 0 && pos < answers.size) {
                    answers[pos] = item.options[pos]
                }
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                val pos = holder.adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: adapterPos
                if (pos >= 0 && pos < answers.size) {
                    answers[pos] = null
                }
            }
        })
    }


    override fun getItemCount(): Int = items.size
}
