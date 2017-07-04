package ua.sg.academy.havrulenko.android;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.databinding.CardUserBinding;
import ua.sg.academy.havrulenko.android.model.Account;
import ua.sg.academy.havrulenko.android.ui.AccountDetailActivity;

import static ua.sg.academy.havrulenko.android.ui.MainActivity.KEY_SESSION_EMAIL;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UsersListViewHolder> {

    private final List<Account> accountList;

    public UsersListAdapter(@NonNull List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public UsersListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardUserBinding binding = CardUserBinding.inflate(inflater, parent, false);
        return new UsersListViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(UsersListViewHolder holder, int position) {
        Account account = accountList.get(position);
        String firstName = account.getFirstName() == null ? "" : account.getFirstName();
        String lastName = account.getLastName() == null ? "" : account.getLastName();

        holder.mBinding.tvUserEmail.setText(account.getEmail());
        holder.mBinding.tvUserName.setText(firstName + " " + lastName);
        holder.mBinding.imageViewRemove.setVisibility(account.isAdmin() ? View.GONE : View.VISIBLE);

        if (account.getImage() != null) {
            File imgFile=new File(MyApplication.getImgPathUsers(), account.getImage());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.mBinding.imageViewUserPicture.setImageBitmap(myBitmap);
            }
        } else {
            holder.mBinding.imageViewUserPicture.setImageResource(R.drawable.ic_account_circle);
        }
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    class UsersListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CardUserBinding mBinding;

        UsersListViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.imageViewRemove.setOnClickListener(this);
            mBinding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), AccountDetailActivity.class);
                intent.putExtra(KEY_SESSION_EMAIL, accountList.get(getLayoutPosition()).getEmail());
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public void onClick(View v) {
            final int pos = getLayoutPosition();
            Account account = accountList.get(pos);
            new AlertDialog.Builder(mBinding.getRoot().getContext())
                    .setTitle(R.string.title_account_deleting)
                    .setMessage(v.getContext().getString(R.string.really_delete, account.getEmail()))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                        accountList.remove(pos);
                        notifyItemRemoved(pos);
                        SqLiteStorage.getInstance().delete(account);
                    }).create()
                    .show();
        }
    }

}
