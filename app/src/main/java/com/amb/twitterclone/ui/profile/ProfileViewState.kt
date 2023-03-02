package com.amb.twitterclone.ui.profile

sealed class ProfileViewState {
    object Loading : ProfileViewState()
    object EmptyData : ProfileViewState()
    object GenericError : ProfileViewState()
    object UpdateError : ProfileViewState()
    object UpdateSuccess : ProfileViewState()

    data class ProfileData(val userName: String, val imageUrl: String) : ProfileViewState()
    data class UpdateImageSuccess(val url: String) : ProfileViewState()
}
