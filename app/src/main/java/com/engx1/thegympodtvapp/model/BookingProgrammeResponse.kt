package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class BookingProgrammeResponse(
    @SerializedName("success")
    var success: String,
    @SerializedName("data")
    var data: BookingProgramme
)

data class BookingProgramme(
    @SerializedName("ads")
    var ads: ArrayList<String>,
    @SerializedName("data")
    var programme: Programme
)


data class Programme(
    @SerializedName("current_page")
    var currentPage: Int,
    @SerializedName("data")
    var userHomeProgramme: ArrayList<UserHomeProgramme>
)


data class UserHomeProgramme(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("total_mins")
    var totalMin: Int,
    @SerializedName("images")
    var images: HomeProgrammeImage,
    @SerializedName("descriptions")
    var descriptions: String,
    @SerializedName("assessment_video")
    var assessmentVideo: String,
    @SerializedName("introduction_video")
    var introductionVideo: String,
    @SerializedName("level")
    var level: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("exercises")
    var exercise: ArrayList<Exercise>
)


data class HomeProgrammeImage(
    @SerializedName("listing_image")
    var listingImage: String,
    @SerializedName("thumbs")
    var thumbs: ArrayList<String>
)

data class Exercise(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("total_mins")
    var totalMin: Int,
    @SerializedName("settings")
    var settings: Setting,
    @SerializedName("exercise_type")
    var exerciseType: String,
    @SerializedName("exercise_type_content")
    var exerciseTypeContent: String,
    @SerializedName("images")
    var images: HomeProgrammeImage,
    @SerializedName("status")
    var status: Int,
    @SerializedName("pivot")
    var pivot: Pivot,
)

data class Pivot(
    @SerializedName("programme_id")
    var programmeId: Int,
    @SerializedName("exercise_id")
    var exerciseId: Int,
    @SerializedName("category")
    var category: String,
    @SerializedName("workout_duration")
    var workoutDuration: Int,
    @SerializedName("rest_duration")
    var restDuration: Int,
    @SerializedName("feedback_text")
    var feedbackText: Int,
    @SerializedName("workout_sets")
    var workoutSets: Int,
    @SerializedName("workout_reps")
    var workoutReps: Int,
    @SerializedName("is_sets")
    var isSets: Boolean,
    @SerializedName("other_setting")
    var otherSetting: String,
)

data class Setting(
    @SerializedName("video_mode")
    var videoMode: String,
)
