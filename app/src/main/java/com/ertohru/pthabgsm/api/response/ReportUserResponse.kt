package com.ertohru.pthabgsm.api.response

import com.ertohru.pthabgsm.api.model.ReportUserReportUserResponse

data class ReportUserResponse(

    var error:Boolean?,
    var status:String?,
    var report_user:ReportUserReportUserResponse

)