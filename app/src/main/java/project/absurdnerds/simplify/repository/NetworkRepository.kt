package project.absurdnerds.simplify.repository

import project.absurdnerds.simplify.api.ApiInterface
import project.absurdnerds.simplify.api.SafeApiRequest
import project.absurdnerds.simplify.data.request.ProfilePutRequest

/**
 * Created by Dheeraj Kotwani on 13-11-2020.
 */

class NetworkRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun putUser (putPhoneRequest: ProfilePutRequest) = apiRequest { api.putUser(putPhoneRequest) }

}