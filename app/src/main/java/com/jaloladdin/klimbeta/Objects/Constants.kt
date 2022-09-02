package com.jaloladdin.klimbeta.Objects

object Constants {

    //Wallets name
    const val PAYEER = "PAYEER"
    const val QIWI = "QIWI"
    const val UZCARD = "UZCARD"
    const val HUMO = "HUMO"

    //Passwords
    const val PASSWORD_JALOLADDIN = "Jaloladdin9618Klim"
    const val PASSWORD_ADMIN = "Admin1234Klim"

    //Wallets ID
    const val PAYERR_ID = "62d81b29de1bbbe1b627498a"
    const val QIWI_ID = "62d81b3ade1bbbe1b627498b"
    const val UZCARD_ID = "62d81b05de1bbbe1b6274989"
    const val HUMO_ID = "62d81ae9de1bbbe1b6274988"

    //Shared Preferences
    const val POINTS = "points__key"
    const val IS_ORDERED = "is_ordered__key"
    const val TITLE = "title_key"
    const val MESSAGE = "message_key"

    //INTENTS
    const val INTENT_WALLET_NAME = "wallet_name_key"

    //LINKS
    const val VIDEO_INSTRUCTION_LINK = "https://youtu.be/XhoBpjsegdg"
    const val COMMUNITY_LINK = "https://t.me/klim_offical_channel"


    //Rest
    //GET
    const val GET_ALL_ORDERS = "https://wallet-my-app.herokuapp.com/api/withdrawal/all"
    const val GET_NOT_PAYED_ORDERS = "https://wallet-my-app.herokuapp.com/api/withdrawal/all/0"
    const val GET_PAYED_ORDERS = "https://wallet-my-app.herokuapp.com/api/withdrawal/all/1"

    //ADS
    const val BANNER_1 = "ca-app-pub-4772675325450856/9600419897"
    const val BANNER_2 = "ca-app-pub-4772675325450856/6128912892"
    const val BANNER_3 = "ca-app-pub-4772675325450856/7194582556"
    const val BANNER_4 = "ca-app-pub-4772675325450856/7581633359"

    const val INTER_AD = "ca-app-pub-4772675325450856/6268551682"
    const val APP_LAUNCH = "ca-app-pub-4772675325450856/9629174202"

    //POST
    const val POST_A_ORDER = "https://wallet-my-app.herokuapp.com/api/withdrawal/add"

    //PUT
    const val PUT_STATE = "https://wallet-my-app.herokuapp.com/api/withdrawal/update/"

    //Others
    const val NOT_SELECTED = "not_selected"

}