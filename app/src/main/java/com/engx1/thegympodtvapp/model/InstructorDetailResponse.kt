package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class InstructorDetailResponse(
    @SerializedName("success")
    var success: String?,
    @SerializedName("data")
    var data : InstructorData?
)


data class InstructorData(
    @SerializedName("instructor")
    var instructor: InstructorDetail?,
    @SerializedName("social_media")
    var socialMedia: ArrayList<InstructorSocialMedia>?,
    @SerializedName("programme")
    var programme: ArrayList<InstructorProgramme>?
)


data class InstructorDetail(
    @SerializedName("name")
    var name: String?,
    @SerializedName("specialization")
    var specialization: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("photo")
    var photo: String?
)

data class InstructorSocialMedia(
    @SerializedName("name")
    var name: String?,
    @SerializedName("username")
    var username: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("icon")
    var icon: String?
)

data class InstructorProgramme(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main_category_id")
    var mainCategoryId: Int?,
    @SerializedName("main_category_name")
    var mainCategoryName: String?,
    @SerializedName("level_id")
    var levelId: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("total_mins")
    var totalMin: Int?,
    @SerializedName("images")
    var images: HomeProgrammeImage?,
    @SerializedName("asessment_video")
    var assessmentVideo: String?,
    @SerializedName("introduction_video")
    var introductionVideo: String?,
    @SerializedName("introduction_vimeo_id")
    var introductionVimeoId: String?,
    @SerializedName("descriptions")
    var descriptions: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("level")
    var level: String?,
    @SerializedName("intensity")
    var intensity: Int?,
    @SerializedName("instructor")
    var instructor: ArrayList<InstructorName>?,
    @SerializedName("exercises")
    var exercises: ArrayList<ProgrammeExercise>?
)

data class InstructorName(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?
)

data class ProgrammeExercise(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("total_mins")
    var totalMin: Int?,
    @SerializedName("exercise_type")
    var exerciseType: String?,
    @SerializedName("images")
    var images: HomeProgrammeImage?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("pivot")
    var pivot: InstructorPivot?
)


data class InstructorPivot(
    @SerializedName("sub_workout_id")
    var subWorkoutId: Int?,
    @SerializedName("exercise_id")
    var exerciseId: Int?,
    @SerializedName("workout_duration")
    var workoutDuration: Int?,
    @SerializedName("rest_duration")
    var restDuration: Int?,
    @SerializedName("feedback_text")
    var feedbackText: String?,
    @SerializedName("workout_sets")
    var workoutSets: Int?,
    @SerializedName("workout_reps")
    var workoutReps: Int?,
    @SerializedName("is_sets")
    var isSets: Boolean?
)