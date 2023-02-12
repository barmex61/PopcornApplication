package com.fatih.popcorn.other

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup

class CustomDialog(categoryArray:Array<String>,
                   categoryIndexPosition:Int,

                   context:Context) : AlertDialog.Builder(context) {

    override fun setTitle(title: CharSequence?): AlertDialog.Builder {
        return super.setTitle(title)
    }

    override fun setSingleChoiceItems(items: Array<out CharSequence>?, checkedItem: Int, listener: DialogInterface.OnClickListener?): AlertDialog.Builder {
        return super.setSingleChoiceItems(items, checkedItem, listener)
    }

    override fun setNegativeButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): AlertDialog.Builder {
        return super.setNegativeButton(text, listener)
    }

    override fun setPositiveButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): AlertDialog.Builder {
        return super.setPositiveButton(text, listener)
    }
}