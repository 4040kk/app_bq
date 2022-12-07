package com.example.text

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.leancloud.LCUser
import com.example.supernotes.utils.sendmail
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlin.random.Random


open class RegisterActivity0 : AppCompatActivity(), View.OnClickListener {
     val content=this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val buttonSendMessage: Button =findViewById(R.id.sendmessage)
        buttonSendMessage.setOnClickListener(this)
        val buttonCreat: Button =findViewById(R.id.register_success)
        buttonCreat.setOnClickListener(this)
        val textItems: TextView =findViewById(R.id.item1)
        textItems.setOnClickListener(this)
        val Wait:ProgressBar =findViewById(R.id.progressBar2)
        Wait.visibility=View.INVISIBLE;
    }

    var emailcode=0
    override fun onClick(v: View?) {
        val email: EditText = findViewById(R.id.RegisterAccount)
        when (v?.id) {
            R.id.sendmessage -> {
                val inputEmail = email.text.toString()
                emailcode = Random.nextInt(100000, 999999)
                if (isEmail(inputEmail)) {
                    Thread{
                        sendmail.SendEmai.sendMail(inputEmail,emailcode.toString())}.start()
                    Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "请输入正确的邮箱地址", Toast.LENGTH_SHORT).show()
                }
            }
        }
        when (v?.id) {
            R.id.item1 -> {
                val intent = Intent(this, items::class.java)
                startActivity(intent)
            }
        }
        when (v?.id) {
            R.id.register_success -> {
                val passWord: EditText =findViewById(R.id.RegisterPassword)
                val inputPassword=passWord.text.toString()
                val code: EditText =findViewById(R.id.RegisterCode)
                val inputEmailCode=code.text.toString()
                val inputEmail = email.text.toString()
                val item: CheckBox =findViewById(R.id.item)
                val user = LCUser()
                val Wait:ProgressBar =findViewById(R.id.progressBar2)
                val content=this;
                emailcode = Random.nextInt(100000, 999999)

                if(isEmailCode(inputEmailCode,emailcode.toString()) && registerPassWord(inputPassword)&&item.isChecked) {
                    if (isEmail(inputEmail)){
                        user.apply {
                            username= inputEmail;
                            password=inputPassword;
                            Wait.visibility=View.VISIBLE;
                            signUpInBackground().subscribe(object :Observer<LCUser>{
                                override fun onSubscribe(d: Disposable) {}

                                override fun onNext(t: LCUser) {
                                    //加邮箱验证码后执行以下代码:////////////
                                    Toast.makeText(content,"注册成功",Toast.LENGTH_SHORT).show();
                                    LCUser.logIn(inputEmail,inputPassword).subscribe(object :Observer<LCUser>{
                                        override fun onComplete() { }

                                        override fun onError(e: Throwable) {
                                            Toast.makeText(content,"${e.message}",Toast.LENGTH_SHORT).show();
                                            Wait.visibility=View.INVISIBLE;
                                        }

                                        override fun onNext(t: LCUser) {
                                            Wait.visibility=View.INVISIBLE;
                                            val intent = Intent(content, MainActivity::class.java)
                                            startActivity(intent)
                                            finish();
                                        }

                                        override fun onSubscribe(d: Disposable) {}


                                    })
                                    //////////////////////////////
                                }

                                override fun onError(e: Throwable) {
                                    Toast.makeText(content,"${e.message}",Toast.LENGTH_SHORT).show();
                                    Wait.visibility=View.INVISIBLE;
                                }

                                override fun onComplete() {
                                }


                            })
                        }
                    }else {
                        Toast.makeText(this, "请输入正确的邮箱地址", Toast.LENGTH_SHORT).show()
                    }

                    ///////////////////////////////////////////////////////////////////////////////////

                   // Toast.makeText(this,"注册成功", Toast.LENGTH_SHORT).show()
                   /* val dbHelper=MyDatabaseHelper(this,"Users.db",1)
                    dbHelper.writableDatabase
                    val db=dbHelper.writableDatabase
                    val user= ContentValues().apply {
                        put("account","$inputEmail")
                        Log.d("asd","+++++++++++++++++++++++")
                        put("password","$inputPassword")
                    }

                    db.insert("User",null,user)
                    val intent = Intent(this, LoginActivity1::class.java)
                    startActivity(intent)*/
                }
                else if (item.isChecked==false){
                    Toast.makeText(this,"请同意用户协议", Toast.LENGTH_SHORT).show()
                }
                else if(isEmailCode(inputEmailCode, emailcode.toString()) && (registerPassWord(inputPassword)==false)){
                    Toast.makeText(this,"密码不符合规则", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"验证码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendmessage(message:String) {

    }
}

fun  isEmail(emali:String):Boolean{
    return if(emali.endsWith("@qq.com")||emali.endsWith("@163.com")){ true }
    else{ false }
}

fun isEmailCode(inputEmaiCode:String,emaiCode:String):Boolean {
    return if(inputEmaiCode==emaiCode){
        true
    }
    else{
        false
    }
}

fun registerPassWord(inputPassWord:String):Boolean{
    if(inputPassWord.length<6){
        return false
    }
    else{
        return true
    }
}

class MyDatabaseHelper(var context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    public var createUser="create table User("+
            "account text,"+
            "password text)"

    public var createTodoList="create table TodoList("+
            "title text,"+
            "todoText text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createUser)
        db?.execSQL(createTodoList)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists User")
        onCreate(db)
        db?.execSQL("drop table if exists TodoList")
        onCreate(db)
    }



}