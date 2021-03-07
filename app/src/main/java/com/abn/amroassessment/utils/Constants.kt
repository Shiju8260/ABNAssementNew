package com.abn.amroassessment.utils

class Constants {

    companion object {

        //API PARAMS
        const val PARAM_CLIENTID="client_id"
        const val PARAM_CLIENTSECRET="client_secret"
        const val PARAM_V="v"
        const val PARAM_NEAR="near"
        const val PARAM_INTENT="intent"
        const val PARAM_RADIUS="radius"
        const val PARAM_LIMIT="limit"

        //API or empty DB response messages
        const val TXT_NOT_AVAILABLE="Not Available"
        const val TXT_DATA_NOT_AVAILABLE="Sorry! Data Not Available"

        //Database name
        const val VENUE_DATABASE="venue_database"

        //API Paths
        const val API_VENUE_SEARCH="venues/search?"
        const val API_VENUE_DETAILS="venues/{venueId}?"
        const val API_VENUE_ID_PATH_NAME="venueId"

    }

}