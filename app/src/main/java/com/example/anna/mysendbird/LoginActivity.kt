package com.example.anna.mysendbird

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.sendbird.android.SendBird
import com.sendbird.android.SendBirdException
import com.sendbird.android.User
import kotlinx.android.synthetic.main.activity_login.*

public class LoginActivity : AppCompatActivity() {

    private var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        edit_user_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                userId = edit_user_id.text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })


        btn_connect.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {


                SendBird.connect(userId, object : SendBird.ConnectHandler{
                    override fun onConnected(p0: User?, p1: SendBirdException?) {
                        if(p1 != null){
                            p1.stackTrace
                            return
                        }
                        Log.d(MainActivity.TAG, "onConnected - userId -  ${p0?.userId}")
                        intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("user_id", userId)
                        startActivity(intent)
                    }
                })




            }
        })

    }
}