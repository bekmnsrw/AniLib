package com.bekmnsrw.feature.auth.api

object AuthConstant {

    const val CLIENT_ID = "UK0ylff0eHGzfGts-80Pq3UoffC-s3A1wGMmFUk93Ig" // Move to Build Config
    const val CLIENT_SECRET = "MFM5vmWv5fpf4Y4FczSsXFzkjEFzOcDHfk93A_Ung0M" // Move to Build Config

    const val REDIRECT_URI = "com.bekmnsrw.oauth:/urn:ietf:wg:oauth:2.0:oob" // Move to Build Config

    const val SCOPE = "user_rates+messages+comments+topics+friends"
    const val RESPONSE_TYPE = "code"
    const val GRANT_TYPE_AUTH_CODE = "authorization_code"
    const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
}
