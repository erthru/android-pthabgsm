package com.ertohru.pthabgsm.utils

import android.R.attr.digits
import android.util.Log


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

        fun rupiah(rp:String) : String{

            var result = ""
            var counter = 0
            for (i in rp.length - 1 downTo 0) {
                val ch = rp.get(i)
                result = ch + result
                counter++
                if (counter % 3 === 0) {
                    result = ",$result"
                }
            }

            Log.d("RUPIAH_FIRST_CHAR",result.substring(0,1))

            if(result.substring(0,1) == ",") {
                return result.replaceFirst(",", "")
            }else{
                return result
            }


        }

    }

}