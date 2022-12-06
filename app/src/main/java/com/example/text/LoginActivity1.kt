package com.example.text

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity1 : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val buttonLogin: Button = findViewById(R.id.login)
        buttonLogin.setOnClickListener(this)
        val buttonRegister: Button = findViewById(R.id.add_user)
        buttonRegister.setOnClickListener(this)
        val accountEdit: EditText = findViewById(R.id.Account)
        val passwordEdit: EditText = findViewById(R.id.password)
        val prefs=getPreferences(MODE_PRIVATE)
        val isRemember=prefs.getBoolean("remember_user",false)
        val find: CheckBox =findViewById(R.id.rememberUser)
        if(isRemember){
            val account=prefs.getString("account","")
            val password=prefs.getString("password","")
            Log.d("dsadasd","+++++++++++++++")
            accountEdit.setText(account)
            passwordEdit.setText(password)
            find.isChecked=true
        }
    }

    override fun onClick(v: View?) {
        val account: EditText = findViewById(R.id.Account)
        val password: EditText = findViewById(R.id.password)
        when (v?.id) {
            R.id.login -> {
                val inputAccount = account.text.toString()
                val inputPassword = password.text.toString()
                val rememberUser: CheckBox =findViewById(R.id.rememberUser)
                val prefs=getPreferences(MODE_PRIVATE)
                val editor=prefs.edit()
                if (isUser(inputAccount,inputPassword)) {
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    if(rememberUser.isChecked){
                        editor.putBoolean("remember_user",true)
                        editor.putString("account",inputAccount)
                        editor.putString("password",inputPassword)
                        Log.d("login","-------记住了----------")
                    }
                    else{
                        editor.clear()
                        Log.d("login","---------清除----------")
                    }
                    editor.apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
        when (v?.id) {
            R.id.add_user -> {
                val intent = Intent(this, RegisterActivity0::class.java)
                startActivity(intent)
            }
        }

    }
    @SuppressLint("Range")
    fun isUser(inputAccount:String,inputPassword:String):Boolean{
        val dbHelper= MyDatabaseHelper(this,"Users.db",1)
        dbHelper.writableDatabase
        val db=dbHelper.writableDatabase
        var i=0
        val cursor=db.query("User",null,null,null,null,null,null)
        if(cursor.moveToFirst()){
            do {
                val account=cursor.getString(cursor.getColumnIndex("account"))
                val password=cursor.getString(cursor.getColumnIndex("password"))
                Log.d("mmmmmmm","------------------$account")
                Log.d("mmmmmmmmm","-----------------$password")
                if(account==inputAccount&&password==inputPassword){
                    i=1
                }
                Log.d("mmmmmmmmm","--------------------$i")
            }while (cursor.moveToNext())
        }
        return if(i==1){
            true
        }
        else{
            false
        }
    }


}