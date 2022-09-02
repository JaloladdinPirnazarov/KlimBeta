package com.jaloladdin.klimbeta.DataClasses

data class OrderItems(
    var order_id: String = "",
    var order_state: Int = -1,
    var wallet_name: String = "",
    var wallet_currency: String = "",
    var wallet_path: String = "",
    var sum: Int = -1,
    var comment: String = "",
    var order_created_date: String = ""
)
