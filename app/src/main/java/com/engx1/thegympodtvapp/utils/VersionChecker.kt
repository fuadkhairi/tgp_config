package com.engx1.thegympodtvapp.utils

import com.engx1.thegympodtvapp.BuildConfig
import org.apache.maven.artifact.versioning.DefaultArtifactVersion

class VersionChecker {
    fun getVersionStatus(compareTo: String) : Int {
        val minVer = DefaultArtifactVersion(compareTo)
        val currentVer = DefaultArtifactVersion(BuildConfig.VERSION_NAME)
        return currentVer.compareTo(minVer)
    }
}