package qcjlibrary.activity;

import java.util.List;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelRequestAnswerComom;
import qcjlibrary.model.ModelRequestDetailCommon;
import qcjlibrary.model.ModelRequestDetailExpert;
import qcjlibrary.model.ModelRequestFlag;
import qcjlibrary.model.ModelRequestItem;
import qcjlibrary.model.ModelRequestMyAsk;
import qcjlibrary.model.ModelRequestRelate;
import qcjlibrary.model.ModelShareContent;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.DefaultLayoutUtil;
import qcjlibrary.util.SpanUtil;
import qcjlibrary.widget.RoundImageView;
import qcjlibrary.widget.popupview.PopShareContent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.LoginActivity;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.config.MyConfig;
import com.zhiyicx.zycx.sociax.unit.SociaxUIUtils;

/**
 * author：qiuchunjia time：下午4:31:08 类描述：这个类是实现
 *
 */

public class RequestDetailExpertActivity extends BaseActivity {
	private TextView expert_tv_title;
	private TextView expert_tv_content;
	private TextView tv_flag_value;
	private TextView tv_flag_value2;
	private TextView tv_flag_value3;

	private RoundImageView riv_icon;
	private TextView tv_username;
	private TextView tv_date;
	private LinearLayout ll_relate;

	private LinearLayout ll_expert_repaly;// 专家回复列表
	private EditText et_content;// 发送内容
	private TextView tv_send;// 专家回复列表

	private TextView tv_expert_user;
	private TextView tv_expert_date;
	private TextView tv_expertcontent;
	private TextView find_more;

	// 数据model赛
	private ModelRequestItem mRequestItem;
	// 返回的数据
	private ModelRequestDetailExpert mDetailExpert;
	
	/** 网络异常时的缺省图**/
	private View defaultView;
	private boolean isFirst = true;
	private RelativeLayout rl_request_02;
	private LinearLayout ll_request_expert;
	private FrameLayout frame_request_expert;

	@Override
	public String setCenterTitle() {
		return "问题详情";
	}

	@Override
	public void initIntent() {
		//mRequestItem = (ModelRequestItem) getDataFromIntent(getIntent(), null);
		Model model = (Model) getDataFromIntent(getIntent(), null);
		if(model instanceof ModelRequestItem){
			mRequestItem = (ModelRequestItem) getDataFromIntent(getIntent(), null);
		} else if(model instanceof ModelRequestMyAsk){
			ModelRequestMyAsk mAsk = (ModelRequestMyAsk) model;
			mRequestItem = new ModelRequestItem();
			mRequestItem.setQuestion_id(mAsk.getQuestion_id());
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_request_detail_expert;
	}

	@Override
	public void initView() {
		titleSetRightImage(R.drawable.fenxiang);
		expert_tv_title = (TextView) findViewById(R.id.expert_tv_title);
		expert_tv_content = (TextView) findViewById(R.id.expert_tv_content);
		tv_flag_value = (TextView) findViewById(R.id.tv_flag_value);
		tv_flag_value2 = (TextView) findViewById(R.id.tv_flag_value2);
		riv_icon = (RoundImageView) findViewById(R.id.riv_icon);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_date = (TextView) findViewById(R.id.tv_date);
		ll_relate = (LinearLayout) findViewById(R.id.ll_relate);
		ll_request_expert = (LinearLayout) findViewById(R.id.ll_request_expert);
		rl_request_02 = (RelativeLayout) findViewById(R.id.rl_request_02);
		ll_expert_repaly = (LinearLayout) findViewById(R.id.ll_expert_repaly);
		ll_expert_repaly.setVisibility(View.GONE);
		et_content = (EditText) findViewById(R.id.et_content);
		tv_send = (TextView) findViewById(R.id.tv_send);
		tv_expert_user = (TextView) findViewById(R.id.tv_expert_user);
		tv_expert_date = (TextView) findViewById(R.id.tv_expert_date);
		tv_expertcontent = (TextView) findViewById(R.id.tv_expertcontent);
		find_more = (TextView) findViewById(R.id.find_more);
		tv_flag_value3 = (TextView) findViewById(R.id.tv_flag_value3);
		frame_request_expert = (FrameLayout) findViewById(R.id.frame_request_expert);
	}

	@Override
	public void initData() {
		Title title = getTitleClass();
		title.iv_title_right1.setOnClickListener(this);
		SociaxUIUtils.hideSoftKeyboard(this, et_content);
		sendRequest(mApp.getRequestImpl().answer(mRequestItem), ModelRequestDetailExpert.class, REQUEST_GET);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelRequestDetailExpert) {
			mDetailExpert = (ModelRequestDetailExpert) object;
			addDataToHeadAndFlag(mDetailExpert.getQuestion(), mDetailExpert.getTopic_list());
			addDataToExpertAnswer(mDetailExpert.getAnswer(), mDetailExpert.getCommentlist());
			addDataToRelate(mDetailExpert.getOther_question());
		}
		if (judgeTheMsg(object)) {
			et_content.setText("");
			sendRequest(mApp.getRequestImpl().answer(mRequestItem), ModelRequestDetailExpert.class, REQUEST_GET);
		}
		return object;
	}

	/**
	 * 添加数据到相关模块
	 * 
	 * @param other_question
	 */
	private void addDataToRelate(List<ModelRequestRelate> other_question) {
		if (other_question != null) {
			if (ll_relate.getChildCount() > 0) {
				ll_relate.removeAllViews();
			}
			for (int i = 0; i < other_question.size(); i++) {
				final ModelRequestRelate relate = other_question.get(i);
				View view = mInflater.inflate(R.layout.item_request_detail_relate, null);
				/*********** 初始化布局问答布局 ********************/
				TextView tv_relate_title = (TextView) view.findViewById(R.id.tv_relate_title);
				TextView tv_watch_num = (TextView) view.findViewById(R.id.tv_watch_num);
				/*********** 初始化布局问答布局 end ********************/
				tv_relate_title.setText(relate.getTitle());
				tv_watch_num.setText("浏览人数：" + relate.getView_count());
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ModelRequestItem item = new ModelRequestItem();
						item.setQuestion_id(relate.getQid());
						if (relate.getIs_expert().equals("0")) {
							mApp.startActivity_qcj(RequestDetailExpertActivity.this, RequestDetailCommonActivity.class,
									sendDataToBundle(item, null));
						} else if (relate.getIs_expert().equals("1")) {
							mApp.startActivity_qcj(RequestDetailExpertActivity.this, RequestDetailExpertActivity.class,
									sendDataToBundle(item, null));
						}
					}
				});
				ll_relate.addView(view);
			}
		}
	}

	/**
	 * 添加数据到问答模块
	 * 
	 * @param answer
	 */
	private void addDataToExpertAnswer(ModelRequestAnswerComom data, List<ModelRequestAnswerComom> answers) {
		if (data != null) {
			tv_expert_date.setText(data.getTime());
			if(data.getAnswer_content() != null && !data.getAnswer_content().equals(" ") && !data.getAnswer_content().equals("")){
				tv_expertcontent.setText(data.getAnswer_content());
			}
			find_more.setVisibility(View.VISIBLE);
			find_more.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ll_expert_repaly.setVisibility(View.VISIBLE);
					find_more.setVisibility(View.GONE);
				}
			});
		}
		if (answers != null && answers.size() > 0) {
			if (ll_expert_repaly.getChildCount() > 0) {
				ll_expert_repaly.removeAllViews();
			}
			for (int i = 0; i < answers.size(); i++) {
				final ModelRequestAnswerComom answerComom = answers.get(i);
				answerComom.setQid(mRequestItem.getQuestion_id());
				addDataToComment(answerComom);
			}
		} else {
			find_more.setVisibility(View.GONE);
		}

	}

	private void addDataToComment(final ModelRequestAnswerComom answerComom) {
		if (answerComom != null) {
			View view = mInflater.inflate(R.layout.item_request_expert_repaly, null);
			/*********** 初始化布局问答布局 ********************/
			TextView tv_user = (TextView) view.findViewById(R.id.tv_user);
			TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
			TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
			/*********** 初始化布局问答布局 end ********************/
			if (answerComom.getFrom().equals("expert")) {
				tv_user.setText("专家建议");
				tv_user.setTextColor(getResources().getColor(R.color.text_yellow));
			} else if (answerComom.getFrom().equals("user")) {
				tv_user.setText("追加提问");
				tv_user.setTextColor(getResources().getColor(R.color.text_green));
			}
			tv_content.setText(answerComom.getContent());
			tv_date.setText(answerComom.getTime());
			ll_expert_repaly.addView(view);
		}
	}

	/**
	 * 添加数据到头部和表情部分
	 * 
	 * @param question
	 * @param topic_list
	 */
	private void addDataToHeadAndFlag(ModelRequestItem question, List<ModelRequestFlag> topic_list) {
		if (question != null) {
			expert_tv_title.setText("");
			Drawable drawable = getResources().getDrawable(R.drawable.q);
			expert_tv_title.append(SpanUtil.setImageSpan("xx", 0, 0, drawable));
			expert_tv_title.append("  " + question.getQuestion_content());
			expert_tv_content.setText(question.getQuestion_detail());
			mApp.displayImage(question.getUser_face(), riv_icon);
			tv_username.setText(question.getUser_name());
			tv_date.setText(question.getTime());
		}
		if (topic_list != null) {
			for (int i = 0; i < topic_list.size(); i++) {
				final ModelRequestFlag flag = topic_list.get(i);
				if (i == 0) {
					tv_flag_value.setVisibility(View.VISIBLE);
					tv_flag_value.setText(flag.getTitle());
					tv_flag_value.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mApp.startActivity_qcj(RequestDetailExpertActivity.this, RequestFlagActivity.class,
									sendDataToBundle(flag, null));
						}
					});
				}
				if (i == 1) {
					tv_flag_value2.setVisibility(View.VISIBLE);
					tv_flag_value2.setText(flag.getTitle());
					tv_flag_value2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mApp.startActivity_qcj(RequestDetailExpertActivity.this, RequestFlagActivity.class,
									sendDataToBundle(flag, null));
						}
					});
				}
				if (i == 2) {
					tv_flag_value3.setVisibility(View.VISIBLE);
					tv_flag_value3.setText(flag.getTitle());
					tv_flag_value3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mApp.startActivity_qcj(RequestDetailExpertActivity.this, RequestFlagActivity.class,
									sendDataToBundle(flag, null));
						}
					});
				}
			}
		}
	}

	@Override
	public void initListener() {
		tv_flag_value2.setOnClickListener(this);
		tv_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_answer:
			if (isLogin()) {
				mApp.startActivityForResult_qcj(this, RequestDetailReponceSingleActivity.class,
						sendDataToBundle(mRequestItem, null));
			} else {
				mApp.startActivity_qcj(this, LoginActivity.class, null);
			}
			break;

		case R.id.tv_flag_value2:
			mApp.startActivity_qcj(this, RequestFlagActivity.class, sendDataToBundle(new Model(), null));
			break;
		case R.id.iv_title_right1:
			ModelShareContent shareContent = new ModelShareContent();
			shareContent.setType(Config.SHARE_TEXT);
			shareContent.setTitle("问答分享：");
			String url = mRequestItem.getUrl();
			if(mRequestItem.getUrl() == null || mRequestItem.getUrl().equals(" ")){
				url = MyConfig.HOST + "index.php?app=ask&mod=Question&act=view&id="+mRequestItem.getQuestion_id();
			}
			shareContent.setUrl(url);
			Log.d("Cathy", "url = "+url);
			PopShareContent popShareContent = new PopShareContent(this, shareContent, this);
			popShareContent.showPop(tv_send, Gravity.BOTTOM, 0, 0);
			// mApp.startActivity_qcj(this, RequestDetailResponceActivity.class,
			// sendDataToBundle(new Model(), null));
			break;
		case R.id.tv_send:
			if (isLogin()) {
				String content = et_content.getText().toString();
				if (!TextUtils.isEmpty(content)) {
					ModelRequestAnswerComom answerComom = new ModelRequestAnswerComom();
					answerComom.setContent(content);
					// 当没有追加内容的时候 就拿专家的id，当有追加内容的时候就拿 追加当中id最大的专家id
					if (mDetailExpert != null) {
						addDataToModel(mDetailExpert.getCommentlist(), mDetailExpert.getAnswer(), answerComom);
					}
				}
			} else {
				mApp.startActivity_qcj(this, LoginActivity.class, null);
			}
			break;
		}

	}

	/**
	 * 添加数据到数据源
	 * 
	 * @param commentlist
	 * @param answer
	 * @param answerComom
	 */
	private void addDataToModel(List<ModelRequestAnswerComom> commentlist, ModelRequestAnswerComom answer,
			ModelRequestAnswerComom answerComom) {
		if (commentlist != null) {
			for (int i = commentlist.size() - 1; i >= 0; i--) {
				ModelRequestAnswerComom comom = commentlist.get(i);
				if (comom.getFrom().equals("expert")) {
					answerComom.setComment_id(comom.getComment_id());
				}
			}

		}
		if (answerComom.getComment_id() != null && !answerComom.equals("")) {
			sendRequest(mApp.getRequestImpl().addComment(answerComom), ModelMsg.class, REQUEST_GET);
		} else {
			answerComom.setAnswer_id(answer.getAnswer_id());
			sendRequest(mApp.getRequestImpl().answerComment(answerComom), ModelMsg.class, REQUEST_GET);
		}

	}
	
	@Override
	public View onRequestFailed() {
		// TODO 自动生成的方法存根
		defaultView =  super.onRequestFailed();
		TextView tv_reload = (TextView) defaultView.findViewById(R.id.tv_reload);
		tv_reload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendRequest(mApp.getRequestImpl().answer(mRequestItem), 
						ModelRequestDetailExpert.class, REQUEST_GET);
			}
		});
		DefaultLayoutUtil.showDefault(frame_request_expert, defaultView);
		return defaultView;
	}
	
	@Override
	public View onRequestSuccess() {
		// TODO 自动生成的方法存根
		defaultView = super.onRequestSuccess();
		DefaultLayoutUtil.hideDefault(frame_request_expert, defaultView);
		return defaultView;
	}

}
