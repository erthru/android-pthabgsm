package com.ertohru.pthabgsm.utils

class StringUtils {

    companion object {

        fun dateMin(date:String) : String{

            return date.substring(8,10)+"/"+date.substring(5,7)+"/"+date.substring(0,2)

        }

    }

}