package ua.sg.academy.havrulenko.android;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.databinding.CardUserBinding;
import ua.sg.academy.havrulenko.android.sqlite.Account;

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
        holder.binding.tvUserEmail.setText(account.getEmail());
        holder.binding.imageViewRemove.setVisibility(account.isAdmin() ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    class UsersListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CardUserBinding binding;

        UsersListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.imageViewRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int pos = getLayoutPosition();
            Account account = accountList.get(pos);
            new AlertDialog.Builder(binding.getRoot().getContext())
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
