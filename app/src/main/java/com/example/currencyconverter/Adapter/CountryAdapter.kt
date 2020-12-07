package com.example.currencyconverter.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.currencyconverter.R
import com.haipq.android.flagkit.FlagImageView

class CountryAdapter(var names: Array<String>, val context: Context): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: FromViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            vh = FromViewHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as FromViewHolder
        }

        val params = view.layoutParams
        params.height = 100
        view.layoutParams = params
        vh.name.text = names[position]
        vh.flag.countryCode = names[position].substring(0, 2).toUpperCase()
        return view
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return names.size
    }

    private class FromViewHolder(row: View?) {
        val name: TextView = row!!.findViewById(R.id.textField)
        val flag: FlagImageView = row!!.findViewById(R.id.flagView)
    }

}