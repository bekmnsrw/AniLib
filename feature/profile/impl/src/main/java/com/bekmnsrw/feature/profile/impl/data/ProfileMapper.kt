package com.bekmnsrw.feature.profile.impl.data

import com.bekmnsrw.feature.profile.api.model.Image
import com.bekmnsrw.feature.profile.api.model.WhoAmI
import com.bekmnsrw.feature.profile.impl.data.datasource.remote.response.ImageResponse
import com.bekmnsrw.feature.profile.impl.data.datasource.remote.response.WhoAmIResponse

internal fun WhoAmIResponse.toWhoAmI(): WhoAmI = WhoAmI(
    avatar = avatar,
    birthOn = birthOn,
    fullYears = fullYears,
    id = id,
    image = image.toImage(),
    lastOnlineAt = lastOnlineAt,
    locale = locale,
    name = name,
    nickname = nickname,
    sex = sex,
    url = url,
    website = website
)

internal fun ImageResponse.toImage(): Image = Image(
    x148 = x148,
    x16 = x16,
    x160 = x160,
    x32 = x32,
    x48 = x48,
    x64 = x64,
    x80 = x80
)
