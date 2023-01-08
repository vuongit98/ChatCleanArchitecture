package com.anonymous.getapifromflowcoroutines.presentation.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anonymous.getapifromflowcoroutines.R
import com.anonymous.getapifromflowcoroutines.data.Model.Friend
import com.anonymous.getapifromflowcoroutines.databinding.ItemAccountBinding

class AccountAdapter (val clickItem: (Friend) -> Unit) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    inner class AccountViewHolder (val viewBinding : ItemAccountBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(accout : Friend, clickItem:(Friend)-> Unit) {
            if (accout.status == 2) {
                return
            }
            if (accout.account?.gender == 1 ) {
                viewBinding.imgPerson.setBackgroundResource(R.drawable.male)
            }else {
                viewBinding.imgPerson.setImageResource(R.drawable.female)
            }
            viewBinding.txtName.setText(accout.account?.email)
            viewBinding.txtPhone.setText(accout.account?.phone)
            viewBinding.txtAddress.setText(accout.account?.address)
            if (accout.status == 0) {
                viewBinding.btnAddFriend.setOnClickListener {
                    viewBinding.btnAddFriend.setText("Requested")
                    clickItem.invoke(accout)
                    viewBinding.btnAddFriend.isEnabled = false

                }
            }
            else if(accout.status == 1) {
                viewBinding.btnAddFriend.setText("Requested")
                viewBinding.btnAddFriend.isEnabled = false
            }
            else if(accout.status == 3) {
                viewBinding.btnAddFriend.setText("Accepted")
                viewBinding.btnAddFriend.setOnClickListener {
                    clickItem.invoke(accout)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = ItemAccountBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = differ.currentList.get(position)
        holder.bind(account,clickItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    val listAccount = object : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem.account?.uid == newItem.account?.uid
        }

        override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem.account?.uid == newItem.account?.uid && oldItem.account?.email == newItem.account?.email
        }

    }
    var differ = AsyncListDiffer(this,listAccount)
}