package com.example.text

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.leancloud.LCUser
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class LoginActivity1 : AppCompatActivity(),View.OnClickListener {
    val content=this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val buttonLogin: Button = findViewById(R.id.login)
        buttonLogin.setOnClickListener(this)
        val buttonRegister: Button = findViewById(R.id.add_user)
        val loginIng:ProgressBar =findViewById(R.id.progressBar)
        loginIng.isEnabled=false;
        loginIng.visibility=View.INVISIBLE
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
        val loginIng:ProgressBar =findViewById(R.id.progressBar)
        when (v?.id) {
            R.id.login -> {
                val inputAccount = account.text.toString()
                val inputPassword = password.text.toString()
                val rememberUser: CheckBox =findViewById(R.id.rememberUser)
                val prefs=getPreferences(MODE_PRIVATE)
                val editor=prefs.edit()
                val author: LCUser? = null
                loginIng.visibility=View.VISIBLE;
                LCUser.logIn(inputAccount,inputPassword).subscribe(object : Observer<LCUser> {
                    override fun onComplete() {}

                    override fun onError(e: Throwable) {
                        Toast.makeText(content,"????????????",Toast.LENGTH_SHORT).show();
                        loginIng.visibility=View.INVISIBLE;
                    }

                    override fun onNext(t: LCUser) {
                        loginIng.visibility=View.INVISIBLE;
                        if(rememberUser.isChecked){
                            editor.putBoolean("remember_user",true)
                            editor.putString("account",inputAccount)
                            editor.putString("password",inputPassword)
                            Log.d("login","-------?????????----------")
                        }
                        else{
                            editor.clear()
                            Log.d("login","---------??????----------")
                        }
                        editor.apply()
                        val intent = Intent(content, MainActivity::class.java)
                        setResult(1, intent)
                        finish()
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                })
                loginIng.visibility=View.INVISIBLE;

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