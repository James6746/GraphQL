package com.example.graphql

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.graphql.network.GraphQl
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            getUserList()
            deleteUserById("797885ed-fc0f-4231-9abd-71ad5206d916")
            getUserById("8d8ac7a6-e1c5-494b-a5a6-d5b1325409ad")
            insertUser("Alisher", "Navoiy", "@alishernavoiy.tweet")
            updateUser("8d8ac7a6-e1c5-494b-a5a6-d5b1325409ad", "Ironman", "Raketa", "ironman@tweet.com")
        }
    }

    private fun getUserList() {
        lifecycleScope.launch launchWhenResumed@{
            val response = try {
                GraphQl.get().query(UsersListQuery(10)).execute()
            } catch (e: ApolloException) {
                Log.d("MainActivity", e.toString())
                return@launchWhenResumed
            }
            val users = response.data?.users
            Log.d("@@@GetUsers", users!!.toString())
        }
    }

    private fun deleteUserById(id: String) {
        lifecycleScope.launch launchWhenResumed@{
            val response = try {
                GraphQl.get().mutation(DeleteUserMutation(id)).execute()
            } catch (e: ApolloException) {
                Log.d("MainActivity", e.toString())
                return@launchWhenResumed
            }
            Log.d("@@@DeleteUser", response.data?.delete_users!!.toString())
        }
    }

    private fun getUserById(id: String) {
        lifecycleScope.launch launchWhenResumed@{
            val response = try {
                GraphQl.get().query(UserByIdQuery(id)).execute()
            } catch (e: ApolloException) {
                Log.d("MainActivity", e.toString())
                return@launchWhenResumed
            }
            val user = response.data?.users_by_pk
            Log.d("@@@GetUserByID", user.toString())
        }
    }

    private fun insertUser(name: String, rocket: String, twitter: String) {
        lifecycleScope.launch launchWhenResumed@{
            val response = try {
                GraphQl.get().mutation(InsertUserMutation(name, rocket, twitter)).execute()
            } catch (e: ApolloException) {
                Log.d("MainActivity", e.toString())
                return@launchWhenResumed
            }
            Log.d("@@@InsertUser", response.data?.insert_users.toString())
        }
    }

    private fun updateUser(id: String, name: String, rocket: String, twitter: String) {
        lifecycleScope.launch launchWhenResumed@{
            val response = try {
                GraphQl.get().mutation(
                    UpdateUserMutation(
                        id,
                        name,
                        rocket,
                        Optional.presentIfNotNull(twitter)
                    )
                ).execute()
            } catch (e: ApolloException) {
                Log.d("MainActivity", e.toString())
                return@launchWhenResumed
            }
            val user = response.data?.update_users
            Log.d("@@@InsertUser", user.toString())
        }
    }

}