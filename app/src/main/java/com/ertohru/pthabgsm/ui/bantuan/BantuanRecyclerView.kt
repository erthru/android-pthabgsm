package com.ertohru.pthabgsm.ui.bantuan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import kotlinx.android.synthetic.main.list_bantuan.view.*

class BantuanRecyclerView(val context:Context, val data:ArrayList<HashMap<String, String>>?) : RecyclerView.Adapter<BantuanRecyclerView.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_bantuan,parent,false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.init(data,position,context)

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class Holder(val v: View) : RecyclerView.ViewHolder(v){

        var isExpand = false

        fun init(data: ArrayList<HashMap<String, String>>?,position: Int, context: Context){

            v.tvTitleLB.text = data?.get(position)?.get("title")
            v.tvContentLB.text = data?.get(position)?.get("content")

            v.layoutLB.setOnClickListener {

                if(isExpand){

                    isExpand = false
                    v.btnExpandLB.background = context.resources.getDrawable(R.drawable.ic_keyboard_arrow_down_grey_24dp)
                    v.layoutContentLB.visibility = View.GONE
                    v.tvContentLB.visibility = View.GONE

                }else{

                    isExpand = true
                    v.btnExpandLB.background = context.resources.getDrawable(R.drawable.ic_keyboard_arrow_up_grey_24dp)
                    layoutExpandsAnimate(v.layoutContentLB)
                    v.tvContentLB.visibility = View.VISIBLE

                }

            }

        }

        private fun layoutExpandsAnimate(linearLayout: LinearLayout){

            val animate = TranslateAnimation(0f,0f,30f,0f)
            animate.duration = 500
            animate.fillAfter = true
            linearLayout.startAnimation(animate)
            linearLayout.visibility = View.VISIBLE

        }


    }
}