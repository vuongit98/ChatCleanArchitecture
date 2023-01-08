package com.anonymous.getapifromflowcoroutines.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anonymous.getapifromflowcoroutines.R
import com.anonymous.getapifromflowcoroutines.databinding.FragmentAddFriendBinding
import com.anonymous.getapifromflowcoroutines.presentation.ui.Adapter.AccountAdapter
import com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel.FriendViewModel
import com.anonymous.getapifromflowcoroutines.presentation.ui.activity.HomeActivity
import com.anonymous.getapifromflowcoroutines.presentation.ui.activity.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AddFriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var viewBinding : FragmentAddFriendBinding
    val adapterAccount : AccountAdapter by lazy {
        AccountAdapter({
//            Toast.makeText(activity, "${it}", Toast.LENGTH_SHORT).show()
            if (it.status == 0) {
                viewModelFriend.addFriends(LoginActivity.accountLogin!!, it.account!!)
            }
            else if(it.status == 3) {
                viewModelFriend.acceptFriends(LoginActivity.accountLogin!!, it.account!!)
            }
        })
    }
    val viewModelFriend : FriendViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentAddFriendBinding.inflate(layoutInflater)
        viewBinding.recyclerFriends.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            adapter = adapterAccount
        }
        viewBinding.loading.visibility = View.VISIBLE
        Log.d("AddFriend", "onCreateView: ")
        viewModelFriend.getListUsers(uid = HomeActivity.uid!!)
        lifecycle.coroutineScope.launchWhenCreated {
            viewModelFriend._friendFlow.collectLatest {
                if (it.isLoading) {
                    viewBinding.loading.visibility = View.VISIBLE
                    Toast.makeText(activity, "Loading...", Toast.LENGTH_SHORT).show()
                }
                if (it.error.isNotBlank()){
                    Toast.makeText(activity, "${it.error.toString()}", Toast.LENGTH_SHORT).show()
                }
                if (it.data.size > 0) {
                    // Log.d("list friends:", "${it}")
                    // Toast.makeText(activity, "${it.size} Success...", Toast.LENGTH_SHORT).show()

                    adapterAccount.differ.submitList(it.data)
                    adapterAccount.notifyDataSetChanged()

                }
                viewBinding.loading.visibility = View.GONE
            }
            viewModelFriend._acceptFriend.collectLatest {
                if (it.isLoading == true) {
                    Toast.makeText(activity, "Loading....", Toast.LENGTH_SHORT).show()
                }
                else if (it.error.isNotBlank() == false) {
                    Toast.makeText(activity, "Accepted successfully....", Toast.LENGTH_SHORT).show()
                    //startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                   // finish()
                }
                else {
                    Toast.makeText(activity, "${it.error}", Toast.LENGTH_SHORT).show()

                }
            }
        }

        return viewBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFriendFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFriendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}