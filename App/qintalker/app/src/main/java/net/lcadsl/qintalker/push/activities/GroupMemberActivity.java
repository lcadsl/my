package net.lcadsl.qintalker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.lcadsl.qintalker.common.app.PresenterToolbarActivity;
import net.lcadsl.qintalker.common.widget.PortraitView;
import net.lcadsl.qintalker.common.widget.recycler.RecyclerAdapter;
import net.lcadsl.qintalker.factory.model.db.view.MemberUserModel;
import net.lcadsl.qintalker.factory.presenter.group.GroupMembersContract;
import net.lcadsl.qintalker.factory.presenter.group.GroupMembersPresenter;
import net.lcadsl.qintalker.push.R;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupMemberActivity extends PresenterToolbarActivity<GroupMembersContract.Presenter>
        implements GroupMembersContract.View {
    private static final String KEY_GROUP_ID="KEY_GROUP_ID";

    private static final String KEY_GROUP_AdMIN="KEY_GROUP_ADMIN";


    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private String mGroupId;
    private boolean mIsAdmin;

    private RecyclerAdapter<MemberUserModel> mAdapter;

    public static void show(Context context,String groupId){
        show(context,groupId,false);
    }

    public static void showAdmin(Context context,String groupId){
        show(context,groupId,true);
    }

    private static void show(Context context,String groupId,boolean isAdmin){
        if (TextUtils.isEmpty(groupId))
            return;
        Intent intent=new Intent(context,GroupMemberActivity.class);
        intent.putExtra(KEY_GROUP_ID,groupId);
        intent.putExtra(KEY_GROUP_AdMIN,isAdmin);
        context.startActivity(intent);
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_member;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mGroupId =bundle.getString(KEY_GROUP_ID);
        mIsAdmin=bundle.getBoolean(KEY_GROUP_AdMIN);
        return !TextUtils.isEmpty(mGroupId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        setTitle(R.string.title_member_list);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<MemberUserModel>() {
            @Override
            protected int getItemViewType(int position, MemberUserModel memberUserModel) {
                return R.layout.cell_group_create_contact;
            }

            @Override
            protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
                return new GroupMemberActivity.ViewHolder(root);
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter.refresh();
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }

    @Override
    protected GroupMembersContract.Presenter initPresenter() {
        return new GroupMembersPresenter(this);
    }

    @Override
    public String getmGroupId() {
        return mGroupId;
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;
        @BindView(R.id.txt_name)
        TextView mName;


        ViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cb_select).setVisibility(View.GONE);
        }


        @Override
        protected void onBind(MemberUserModel model) {
            mPortrait.setup(Glide.with(GroupMemberActivity.this), model.portrait);
            mName.setText(model.name);
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            PersonalActivity.show(GroupMemberActivity.this, mData.userId);
        }
    }
}
