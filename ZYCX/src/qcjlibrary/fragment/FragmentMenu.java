package qcjlibrary.fragment;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.LoginActivity;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.activity.GuideActivity;
import com.zhiyicx.zycx.activity.HomeActivity;
import com.zhiyicx.zycx.activity.WebActivity;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import com.zhiyicx.zycx.sociax.unit.Anim;
import com.zhiyicx.zycx.util.PreferenceUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import qcjlibrary.activity.MeAplicationActivity;
import qcjlibrary.activity.MeCenterActivity;
import qcjlibrary.activity.MePerioActivity;
import qcjlibrary.activity.MsgNotifyPraiseActivity;
import qcjlibrary.activity.RequestMyAskActivity;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelNotiyState;
import qcjlibrary.model.ModelUser;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.UIUtils;
import qcjlibrary.widget.RoundImageView;

/**
 * author：qiuchunjia time：下午5:27:29 类描述：这个类是实现
 *
 */

public class FragmentMenu extends BaseFragment {
	private LinearLayout rl_user;
	private RoundImageView riv_user_icon;
	private TextView tv_username;
	private ImageView menu_iv_edit;
	private ImageView iv_menu_msg;
	private RelativeLayout rl_home;
	private RelativeLayout rl_question;
	private RelativeLayout rl_app;
	private RelativeLayout rl_cycle;
	private RelativeLayout rl_periodical;
	private RelativeLayout rl_msgnotify;
	private Button btn_quit;

	private ModelUser mUser;
	private String status;

	@Override
	public void initIntentData() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_slide_menu;
	}

	@Override
	public void initView() {
		rl_user = (LinearLayout) findViewById(R.id.rl_user);
		riv_user_icon = (RoundImageView) findViewById(R.id.riv_user_icon);
		tv_username = (TextView) findViewById(R.id.tv_username);
		menu_iv_edit = (ImageView) findViewById(R.id.menu_iv_edit);
		iv_menu_msg = (ImageView) findViewById(R.id.iv_menu_msg);
		rl_home = (RelativeLayout) findViewById(R.id.rl_home);
		rl_question = (RelativeLayout) findViewById(R.id.rl_question);
		rl_app = (RelativeLayout) findViewById(R.id.rl_app);
		rl_cycle = (RelativeLayout) findViewById(R.id.rl_cycle);
		rl_periodical = (RelativeLayout) findViewById(R.id.rl_periodical);
		rl_msgnotify = (RelativeLayout) findViewById(R.id.rl_msgnotify);
		btn_quit = (Button) findViewById(R.id.btn_quit);

	}

	@Override
	public void initData() {
		RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) btn_quit
				.getLayoutParams();
		layoutParams.bottomMargin = (int) (UIUtils.getWindowHeight(getActivity()) * 0.1) - 2;
		btn_quit.setLayoutParams(layoutParams);
		if (!isLogin()) {
			btn_quit.setText("登录");
		}
		/*
		 * mUser = mApp.getUser(); if (TextUtils.isEmpty(mUser.getAvatar())) {
		 * sendRequest(mApp.getUserImpl().index(), ModelUser.class,
		 * REQUEST_GET); } else { addDataToIcon(mUser); }
		 */
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelUser) {
			mUser = (ModelUser) object;
			addDataToIcon(mUser);
			mApp.saveUser(mUser);
		}
		if(object instanceof ModelNotiyState){
			ModelNotiyState state = (ModelNotiyState) object;
			status = state.getStatus();
			if(status != null && status.equals("1")){
				iv_menu_msg.setVisibility(View.VISIBLE);
			} else{
				iv_menu_msg.setVisibility(View.GONE);
			}
		}
		return object;
	}

	private void addDataToIcon(ModelUser mUser2) {
		if (mUser2 != null) {
			mApp.displayImage(mUser.getAvatar(), riv_user_icon);
			tv_username.setText(mUser2.getUname());
		}
	}

	@Override
	public void initListener() {
		riv_user_icon.setOnClickListener(this);
		rl_user.setOnClickListener(this);
		rl_home.setOnClickListener(this);
		rl_question.setOnClickListener(this);
		rl_app.setOnClickListener(this);
		rl_cycle.setOnClickListener(this);
		rl_periodical.setOnClickListener(this);
		rl_msgnotify.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.riv_user_icon:

			break;
		case R.id.rl_user:
			if (isLogin()) {
				mApp.startActivity_qcj(getActivity(), MeCenterActivity.class, mActivity.sendDataToBundle(mUser, null));
			} else {
				mApp.startActivity_qcj(mActivity, LoginActivity.class, null);
			}

			break;
		case R.id.rl_home:
			mApp.startActivity_qcj(mActivity, HomeActivity.class, mActivity.sendDataToBundle(new Model(), null));
			mActivity.finish();
			break;
		case R.id.rl_question:
			if (isLogin()) {
				mApp.startActivity_qcj(mActivity, RequestMyAskActivity.class, null);
			} else {
				mApp.startActivity_qcj(mActivity, LoginActivity.class, null);
			}
			break;
		case R.id.rl_app:
			mApp.startActivity_qcj(mActivity, MeAplicationActivity.class,
					mActivity.sendDataToBundle(new Model(), null));
			break;
		case R.id.rl_cycle:
			mApp.startActivity_qcj(mActivity, WebActivity.class, mActivity.sendDataToBundle(new Model(), null));
			break;
		case R.id.rl_periodical:
			mApp.startActivity_qcj(getActivity(), MePerioActivity.class, mActivity.sendDataToBundle(new Model(), null));
			break;
		case R.id.rl_msgnotify:
			if(isLogin()){
				mApp.startActivity_qcj(getActivity(), MsgNotifyPraiseActivity.class, mActivity.sendDataToBundle(new Model(), null));
			} else {
				mApp.startActivity_qcj(mActivity, LoginActivity.class, null);
			}
			break;
		case R.id.btn_quit:
			if (isLogin()) {
				quitLogin();
			} else {
				mApp.startActivity_qcj(mActivity, LoginActivity.class, null);
			}
			break;
		}
	}

	/**
	 * 退出登录
	 */
	private void quitLogin() {
		final Activity obj = getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(obj);
		builder.setMessage("退出登录?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Thinksns app = (Thinksns) obj.getApplicationContext();
				app.getUserSql().clear();
				/******************
				 * 清除token author qcj 2015-1-12
				 **************************/
				PreferenceUtil preferenceUtil = PreferenceUtil.getInstance(getActivity());
				preferenceUtil.saveString("oauth_token_secret", "");
				preferenceUtil.saveString("oauth_token", "");
				/******************
				 * 清除token author qcj 2015-1-12
				 **************************/
				// Thinksns.exitApp();
				Intent intent = new Intent(obj, GuideActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				obj.startActivity(intent);
				Anim.in(obj);
				obj.finish();
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		mUser = mApp.getUser();
		if (!isLogin()) {
			return;
		}
		btn_quit.setText("退出登录");
//		if (TextUtils.isEmpty(mUser.getAvatar())) {
//			sendRequest(mApp.getUserImpl().index(), ModelUser.class, REQUEST_GET);
//		} else {
//			addDataToIcon(mUser);
//		}
		if(isLogin()){
			sendRequest(mApp.getUserImpl().index(), ModelUser.class, REQUEST_GET);
			sendRequest(mApp.getNotifyImpl().isRead(), ModelNotiyState.class, REQUEST_GET);
		}
	}
}
