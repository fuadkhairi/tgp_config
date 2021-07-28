package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class VimeoConfigResponse(
    @SerializedName("cdn_url")
    val cdnUrl: String,
    @SerializedName("vimeo_api_url")
    val vimeoApiUrl: String,
    @SerializedName("request")
    val request: VimeoRequest
)

data class VimeoRequest(
    @SerializedName("files")
    val files: VimeoFiles
)

data class VimeoFiles(
    @SerializedName("hls")
    val hls: HlsConfig
)

data class HlsConfig(
    @SerializedName("cdns")
    val cdns: CdnsConfig
)

data class CdnsConfig(
    @SerializedName("akfire_interconnect_quic")
    val akfireInterconnectQuic: AkfireInterconnectQuic
)

data class AkfireInterconnectQuic(
    @SerializedName("url")
    val url: String
)
