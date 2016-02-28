package qcjlibrary.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.utils.L;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import com.zhiyicx.zycx.util.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.adapter.UseMedicineNotifyAdapter;
import qcjlibrary.api.api.AlarmImpl;
import qcjlibrary.broadcast.AlarmBroadCastReciever;
import qcjlibrary.listview.base.swipelistview.SwipeMenu;
import qcjlibrary.listview.base.swipelistview.SwipeMenuListView;
import qcjlibrary.listview.base.swipelistview.SwipeMenuListView.OnMenuItemClickListener;
import qcjlibrary.model.ModelAlertData;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.base.Model;
import qcjlibrary.response.DataAnalyze;
import qcjlibrary.util.DateUtil;
import qcjlibrary.util.DisplayUtils;
import qcjlibrary.util.SharedPreferencesUtil;
import qcjlibrary.util.ToastUtils;

/**
 * author：tan time：下午5:7:01 类描述：用药提醒，闹钟界面
 */

public class UseMedicineNotifyActivity extends BaseActivity {
	private SwipeMenuListView mSwipeMenuListView;
	private UseMedicineNotifyAdapter mAdapter;
	private List<Model> mList;
	/** 用于区分返回的Msg数据的类型，true为删除返回，false为其他**/
	boolean isDel = false;
	private AlarmImpl impl;

	@Override
	public void onClick(View v) {

	}

	@Override
	public String setCenterTitle() {
		return "用药提醒";
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.swipe_menu_listview;
	}

	@Override
	public void initView() {
		titleSetRightImage(R.drawable.tianjia);
		mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.mSwipeMenuListView);
		mSwipeMenuListView.setDividerHeight(DisplayUtils.dp2px(this, 10));

	}

	@Override
	public void initData() {
		Title title = getTitleClass();
		title.iv_title_right1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mApp.startActivity_qcj(UseMedicineNotifyActivity.this, MedicineEditNotifyActivity.class, null);
			}
		});
		mList = new ArrayList<Model>();
		impl = new AlarmImpl();
		//sendRequest(impl.index(), ModelAlertData.class, REQUEST_GET);
	}

	@Override
	public void initListener() {
		mSwipeMenuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mSwipeMenuListView.stepToNextActivity(parent, view, position, MedicineEditNotifyActivity.class);

			}
		});

		/**
		 * 监听滑动删除,调用适配器中delete方法，删除服务器中数据，同时取消闹钟事件。
		 */
		mSwipeMenuListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				isDel = true;
				sendRequest(impl.delete((ModelAlertData) mList.get(position)), 
						ModelMsg.class, REQUEST_POST);
				cancelAlarm((ModelAlertData) mList.get(position));
				return false;
			}
		});
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		// TODO 自动生成的方法存根
		Object object;
		/**
		 * 根据返回的对象类型使用不同的解析数据的方法 如果是删除后数据，则将isDel设置为false
		 */
		if (isDel) {
			object = DataAnalyze.parseDataByGson(str, class1);
		} else {
			object = DataAnalyze.parseData(str, class1);
		}
		if (object instanceof List<?>) {
			if (mList != null) {
				mList.clear();
				List<ModelAlertData> newList = (List<ModelAlertData>) object;
				if(newList != null && newList.size() > 0){
					mList.addAll(newList);
				}
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				} else {
					mAdapter = new UseMedicineNotifyAdapter(this, mList);
					mSwipeMenuListView.setAdapter(mAdapter);
				}
				for (int i = 0; i < mList.size(); i++) {
					setAlarm(i);
				}
			}
		}
		if (object instanceof ModelMsg) {
			
			ModelMsg msg = (ModelMsg) object;
			if(isDel){
				if(msg.getCode() == 0){
					ToastUtils.showLongToast(this, "删除成功");
					if(mList.size() == 1){
						mList.remove(0);
						mAdapter.notifyDataSetChanged();
						return object;
					}
					sendRequest(impl.index(), ModelAlertData.class, REQUEST_GET);
				} else{
					ToastUtils.showLongToast(this, "删除失败");
				}
				isDel = false;
			} else{
				if(msg.getCode() == 0){
					//缺省页
					ToastUtils.showLongToast(this, "暂无服药提醒数据");
				}
			}
		}
		return object;
	}

	private void setAlarm(int position) {
		Log.d("Cathy", "position:"+position);
		// 循环为每个闹钟设置广播
		ModelAlertData mData = (ModelAlertData) mList.get(position);
		String timeList = mData.getMed_time();
		String startTime = mData.getStime();
		String repeatDaily = mData.getPeriod();
		int period = Integer.parseInt(repeatDaily);
		// 根据重复天数计算出间隔的毫秒数
		long intervalMillis = DateUtil.setIntervalMillis(period);
		if (timeList == null) {
			return;
		}
		String[] timeArr = timeList.split(",");
		for (int i = 0; i < timeArr.length; i++) {
			/** 区分不同闹钟的ID 如231：第23号闹钟的第一个提醒时间 **/
			int id = Integer.parseInt(mData.getId()+""+i);
			//提醒
			if (mData.getIs_remind() == 0) {
				if (timeArr[i] != null) {
					long currentMillis = System.currentTimeMillis();
					Calendar mCalendar = Calendar.getInstance();
					//将当前时间设置到Calendar中，如果设置的时间为过去时间，那么直接改成当前时间
					mCalendar.setTimeInMillis(currentMillis);
					Date current = new Date();
					Date set = DateUtil.stampToDate(DateUtil.dateToStr2(startTime));
					/**
					 * 如果设置的时间为未来时间，那么将年月日设置为未来时间
					 * */
					if (DateUtil.compareDate(set, current)) {
						mCalendar.set(Calendar.YEAR, set.getYear());
						mCalendar.set(Calendar.MONTH, set.getMonth());
						mCalendar.set(Calendar.DAY_OF_MONTH, set.getDay());
					}
					int hour = 8;
					int min = 0;
					try{
						hour = Integer.parseInt(timeArr[i].split(":")[0]);
						min = Integer.parseInt(timeArr[i].split(":")[1]);
					} catch(Exception e){
						hour = 8;
						min = 0;
					}
					mCalendar.set(Calendar.HOUR_OF_DAY, hour);
					mCalendar.set(Calendar.MINUTE, min);
					mCalendar.set(Calendar.SECOND, 0);
					/**
					 * 如果当前时间晚于第一次提醒时间，那么则提醒时间顺推到下次提醒的时间
					 * 如：设置的为 2016-1-12 10:00:00 当前为 2016-1-12 11:00:00 间隔时间为一天
					 * 	     则将第一次提醒时间设置为2016-1-13 10:00:00
					 * */
					if(mCalendar.getTimeInMillis() < currentMillis){
						 mCalendar.add(Calendar.DAY_OF_MONTH, period);
					}
//					Log.d("Cathy", "开始时间：" + DateUtil.changeLong2Str(mCalendar.getTimeInMillis()));
					/**
					 * 三种获取PendingIntent对象的方法 getActivity(Context, int, Intent,
					 * int) 启动一个activity getBroadcast(Context, int, Intent, int)
					 * 发送一个广播 getService(Context, int, Intent, int) 开启一个服务
					 */
					Intent mIntent = new Intent(this, AlarmBroadCastReciever.class);
					/** 闹钟管理类 **/
					AlarmManager mManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					mIntent.setAction("alarm.alert.short");
					PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, id, mIntent, 
							PendingIntent.FLAG_CANCEL_CURRENT);
					mManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),
							intervalMillis, mPendingIntent);
					//SharedPreferencesUtil.saveData(this, mData.getId() + ":" + i, id);
				}
			} else{
				//不提醒则取消
				AlarmManager mManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				Intent mIntent = new Intent(this, AlarmBroadCastReciever.class);
				mIntent.setAction("alarm.alert.short");
				L.d("Cathy", "cancel alert");
				/** 区分不同闹钟的ID **/
//				SharedPreferencesUtil.saveData(this, mData.getId() + ":" + i, id);
				PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, id, mIntent, 
						PendingIntent.FLAG_CANCEL_CURRENT);
				mManager.cancel(mPendingIntent);
			}
		}
	}
	
	private void cancelAlarm(ModelAlertData mData){
		int count = mData.getMed_num();
		for (int i = 0; i < count; i++) {
			AlarmManager mManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Intent mIntent = new Intent(this, AlarmBroadCastReciever.class);
			mIntent.setAction("alarm.alert.short");
			int id = Integer.parseInt(mData.getId()+""+i);
			PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, id, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			mManager.cancel(mPendingIntent);
		}
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		sendRequest(impl.index(), ModelAlertData.class, REQUEST_GET);
	}
	
}
