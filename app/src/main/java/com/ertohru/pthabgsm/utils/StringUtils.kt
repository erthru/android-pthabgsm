package com.ertohru.pthabgsm.utils

class StringUtils {

    companion object {

        fun dateMin(date:String) : String{

            return date.substring(8,10)+"/"+date.substring(5,7)+"/"+date.substring(0,2)

        }

        fun dateLengkap(date:String) : String{

            val month = HashMap<String, String>()
            month["01"]="Jan"
            month["02"]="Feb"
            month["03"]="Mar"
            month["04"]="Apr"
            month["05"]="Mei"
            month["06"]="Jun"
            month["07"]="Jul"
            month["08"]="Agu"
            month["09"]="Sep"
            month["10"]="Okt"
            month["11"]="Nov"
            month["12"]="Des"

            return date.substring(8,10)+" "+month.get(date.substring(5,7))+" "+date.substring(0,4)+" | "+date.substring(10)

        }

    }

}