package project.absurdnerds.simplify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import project.absurdnerds.simplify.Fragment.LoginMobileFragment
import project.absurdnerds.simplify.home.HomeActivity

class LoginActivity : AppCompatActivity(), FragmentChangeInterface {

    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var fragment = LoginMobileFragment()
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }

        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginFrame, fragment)
        fragmentTransaction.commit()

    }

    override fun changeFragment(fragment: Fragment) {
        if (supportFragmentManager.backStackEntryCount == 0) {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.loginFrame, fragment).commit()
        }
        else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginFrame, fragment).commit()
        }
    }


}