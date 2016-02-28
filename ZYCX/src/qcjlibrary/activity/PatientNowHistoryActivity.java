package qcjlibrary.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelAddNowCase;
import qcjlibrary.model.ModelDiagnosis;
import qcjlibrary.model.ModelImage;
import qcjlibrary.model.ModelLab;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelPop;
import qcjlibrary.util.DateUtil;
import qcjlibrary.util.LoadingDialogUtl;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.util.localImageHelper.LocalImageManager;
import qcjlibrary.widget.popupview.PopCommonProgress;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.unit.SociaxUIUtils;

/**
 * author：qiuchunjia time：上午10:55:26 类描述：这个类是实现
 *
 */

public class PatientNowHistoryActivity extends BaseActivity {
	private RelativeLayout rl_check_time;
	private TextView tv_check_time_name;
	private RelativeLayout rl_check_time1;
	private TextView tv_check_end_time_name;
	private EditText et_name;
	private RelativeLayout rl_check_way;
	private TextView tv_check_way_name;
	private RelativeLayout rl_check_add;
	private RelativeLayout rl_lab_check_project;

	private TextView tv_lab_check_time_project;
	private RelativeLayout rl_lab_check_time1;
	private TextView tv_lab_check_end_time_name;
	private EditText et_lab_checkname;
	private RelativeLayout rl_lab_check_add;
	private RelativeLayout rl_vedio_check_project;
	private TextView tv_vedio_check_time_project;
	private RelativeLayout rl_vedio_check_time1;
	private TextView tv_vedio_check_end_time_name;
	private EditText et_vedio_checkname;
	private RelativeLayout rl_vedio_check_add;
	private TextView tv_lab_check_project;
	/*************** 添加图片的 **************************/
	private LocalImageManager mImageManager;
	private LinearLayout ll_ScrollView1;
	private LinearLayout ll_ScrollView2;
	private LinearLayout ll_ScrollView3;

	private PopCommonProgress mProgress;
	
	private TimePickerView pvTime;
	/** 不同时间选择框的类型**/
	private String timeType;

	@Override
	public String setCenterTitle() {
		return "现病史";
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_patient_now_history;
	}

	@Override
	public void initView() {
		titleSetRightTitle("保存");
		rl_check_time = (RelativeLayout) findViewById(R.id.rl_check_time);
		tv_check_time_name = (TextView) findViewById(R.id.tv_check_time_name);
		rl_check_time1 = (RelativeLayout) findViewById(R.id.rl_check_time1);
		tv_check_end_time_name = (TextView) findViewById(R.id.tv_check_end_time_name);
		et_name = (EditText) findViewById(R.id.et_name);
		rl_check_way = (RelativeLayout) findViewById(R.id.rl_check_way);
		tv_check_way_name = (TextView) findViewById(R.id.tv_check_way_name);
		rl_check_add = (RelativeLayout) findViewById(R.id.rl_check_add);
		rl_lab_check_project = (RelativeLayout) findViewById(R.id.rl_lab_check_project);
		tv_lab_check_time_project = (TextView) findViewById(R.id.tv_lab_check_time_project);
		rl_lab_check_time1 = (RelativeLayout) findViewById(R.id.rl_lab_check_time1);
		tv_lab_check_end_time_name = (TextView) findViewById(R.id.tv_lab_check_end_time_name);
		et_lab_checkname = (EditText) findViewById(R.id.et_lab_checkname);
		rl_lab_check_add = (RelativeLayout) findViewById(R.id.rl_lab_check_add);
		rl_lab_check_add = (RelativeLayout) findViewById(R.id.rl_lab_check_add);
		rl_vedio_check_project = (RelativeLayout) findViewById(R.id.rl_vedio_check_project);
		tv_vedio_check_time_project = (TextView) findViewById(R.id.tv_vedio_check_time_project);
		rl_vedio_check_time1 = (RelativeLayout) findViewById(R.id.rl_vedio_check_time1);
		tv_vedio_check_end_time_name = (TextView) findViewById(R.id.tv_vedio_check_end_time_name);
		et_vedio_checkname = (EditText) findViewById(R.id.et_vedio_checkname);
		rl_vedio_check_add = (RelativeLayout) findViewById(R.id.rl_vedio_check_add);
		tv_lab_check_project = (TextView) findViewById(R.id.tv_lab_check_project);
		ll_ScrollView1 = (LinearLayout) findViewById(R.id.ll_ScrollView1);
		ll_ScrollView2 = (LinearLayout) findViewById(R.id.ll_ScrollView2);
		ll_ScrollView3 = (LinearLayout) findViewById(R.id.ll_ScrollView3);
		
		pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		pvTime.setTime(new Date());
		pvTime.setCyclic(true);
		pvTime.setCancelable(true);
	}

	@Override
	public void initData() {
		addImageToHsv(null, ADDPHOTO, ll_ScrollView1, Config.TYPE_CHECK_PHOTO);
		addImageToHsv(null, ADDPHOTO, ll_ScrollView2, Config.TYPE_LAB_PHOTO);
		addImageToHsv(null, ADDPHOTO, ll_ScrollView3, Config.TYPE_VIDEO_PHOTO);
		Title title = getTitleClass();
		title.tv_title_right.setOnClickListener(this);
		mImageManager = LocalImageManager.from(this);
		mProgress = new PopCommonProgress(this, null, this);
	}

	@Override
	public void initListener() {
		rl_check_time.setOnClickListener(this);
		rl_check_time1.setOnClickListener(this);
		rl_check_way.setOnClickListener(this);
		rl_lab_check_project.setOnClickListener(this);
		rl_lab_check_time1.setOnClickListener(this);
		rl_vedio_check_project.setOnClickListener(this);
		rl_vedio_check_time1.setOnClickListener(this);
		pvTime.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(Date date) {
				if(timeType.equals(Config.TYPE_CHECK_START_TIME)){
					diagnosis_stime = DateUtil.DateToStamp(date);
					tv_check_time_name.setText(DateUtil.strTodate(diagnosis_stime));
				} else if(timeType.equals(Config.TYPE_CHECK_END_TIME)){
					diagnosis_etime = DateUtil.DateToStamp(date);
					tv_check_end_time_name.setText(DateUtil.strTodate(diagnosis_etime));
				} else if(timeType.equals(Config.TYPE_LAB_CHECK_TIME)){
					lab_exam_time = DateUtil.DateToStamp(date);
					tv_lab_check_end_time_name.setText(DateUtil.strTodate(lab_exam_time));
				} else if(timeType.equals(Config.TYPE_VIDEO_CHECK_TIME)){
					image_exam_time = DateUtil.DateToStamp(date);
					tv_vedio_check_end_time_name.setText(DateUtil.strTodate(image_exam_time));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_right:
			setEditContent();
			if (checkTheContent()) {
				ModelAddNowCase addNowCase = addDataToModel();
				sendRequest(mApp.getMedRecordImpl().savePresent(addNowCase), ModelMsg.class, REQUEST_POST);
			}

			break;
		case R.id.rl_check_time:
			hide();
			timeType = Config.TYPE_CHECK_START_TIME;
			pvTime.show();
//			PopDatePicker datePicker = new PopDatePicker(this, null, this);
//			datePicker.setType(Config.TYPE_CHECK_START_TIME);
//			datePicker.showPop(rl_check_time, Gravity.BOTTOM, 0, 0);
			break;

		case R.id.rl_check_time1:
			hide();
			timeType = Config.TYPE_CHECK_END_TIME;
			pvTime.show();
//			PopDatePicker datePicker1 = new PopDatePicker(this, null, this);
//			datePicker1.setType(Config.TYPE_CHECK_END_TIME);
//			datePicker1.showPop(rl_check_time1, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.rl_check_way:
			mApp.startActivityForResult_qcj(this, ChooseSurgeryWayActivity.class, null);
			break;
		case R.id.rl_lab_check_project:
			mApp.startActivityForResult_qcj(this, ChooseLabWayActivity.class, null);
			break;
		case R.id.rl_lab_check_time1:
			hide();
			timeType = Config.TYPE_LAB_CHECK_TIME;
			pvTime.show();
//			PopDatePicker labdatePicker = new PopDatePicker(this, null, this);
//			labdatePicker.setType(Config.TYPE_LAB_CHECK_TIME);
//			labdatePicker.showPop(rl_lab_check_time1, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.rl_vedio_check_project:
			mApp.startActivityForResult_qcj(this, ChooseTreatWayActivity.class, null);
			break;
		case R.id.rl_vedio_check_time1:
			hide();
			timeType = Config.TYPE_VIDEO_CHECK_TIME;
			pvTime.show();
//			PopDatePicker videodatePicker = new PopDatePicker(this, null, this);
//			videodatePicker.setType(Config.TYPE_VIDEO_CHECK_TIME);
//			videodatePicker.showPop(rl_vedio_check_time1, Gravity.BOTTOM, 0, 0);
			break;
		}

	}
	
	private void hide(){
		SociaxUIUtils.hideSoftKeyboard(this, et_name);
		SociaxUIUtils.hideSoftKeyboard(this, et_lab_checkname);
		SociaxUIUtils.hideSoftKeyboard(this, et_vedio_checkname);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (judgeTheMsg(object)) {
			onBackPressed();
			ToastUtils.showToast("上传成功，等待审核");
		} else{
		}
		return object;
	}

	@Override
	public Object onPopResult(Object object) {
		if (object instanceof ModelPop) {
			ModelPop modelPop = (ModelPop) object;
			if (modelPop.getType().equals(Config.TYPE_CHECK_START_TIME)) {
				tv_check_time_name.setText(modelPop.getDataStr());
				diagnosis_stime = DateUtil.dateToStr(modelPop.getDataStr());
			} else if (modelPop.getType().equals(Config.TYPE_CHECK_END_TIME)) {
				tv_check_end_time_name.setText(modelPop.getDataStr());
				diagnosis_etime = DateUtil.dateToStr(modelPop.getDataStr());
			} else if (modelPop.getType().equals(Config.TYPE_LAB_CHECK_TIME)) {
				tv_lab_check_end_time_name.setText(modelPop.getDataStr());
				lab_exam_time = DateUtil.dateToStr(modelPop.getDataStr());
			} else if (modelPop.getType().equals(Config.TYPE_VIDEO_CHECK_TIME)) {
				tv_vedio_check_end_time_name.setText(modelPop.getDataStr());
				image_exam_time = DateUtil.dateToStr(modelPop.getDataStr());
			}
		}
		return super.onPopResult(object);
	}

	private boolean isFirst = true;

	@Override
	public void onResponseProgress(long bytesWritten, long totalSize) {
		super.onResponseProgress(bytesWritten, totalSize);
//		if (isFirst) {
//			isFirst = false;
//			mProgress.showPop(rl_check_time, Gravity.CENTER, 0, 0);
//			mProgress.setProgress(bytesWritten, totalSize);
//		} else {
//			mProgress.setProgress(bytesWritten, totalSize);
//		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Object object = getReturnResultSeri(resultCode, data, Config.TYPE_CHECK_WAY);
		if (object instanceof String) {
			tv_check_way_name.setText(object.toString());
			diagnosis_way = object.toString();
		}
		Object object1 = getReturnResultSeri(resultCode, data, Config.TYPE_LAB_PROJECT);
		if (object1 instanceof String) {
			tv_lab_check_time_project.setText(object1.toString());
			lab_exam_program = object1.toString();
		}
		Object object2 = getReturnResultSeri(resultCode, data, Config.TYPE_VIDEO_PROJECT);
		if (object2 instanceof String) {
			tv_vedio_check_time_project.setText(object2.toString());
			image_exam_program = object2.toString();
		}
		Object photolist1 = getReturnResultSeri(resultCode, data, Config.TYPE_CHECK_PHOTO);
		if (photolist1 instanceof List<?>) {
			List<String> lists = (List<String>) photolist1;
			Log.i("photolist", lists.toString());
			addDataAndDisplay(ll_ScrollView1, mPhotoList1, lists, Config.TYPE_CHECK_PHOTO);
		}
		Object photolist2 = getReturnResultSeri(resultCode, data, Config.TYPE_LAB_PHOTO);
		if (photolist2 instanceof List<?>) {
			List<String> lists = (List<String>) photolist2;
			Log.i("photolist", lists.toString());
			addDataAndDisplay(ll_ScrollView2, mPhotoList2, lists, Config.TYPE_LAB_PHOTO);
		}
		Object photolist3 = getReturnResultSeri(resultCode, data, Config.TYPE_VIDEO_PHOTO);
		if (photolist3 instanceof List<?>) {
			List<String> lists = (List<String>) photolist3;
			Log.i("photolist", lists.toString());
			addDataAndDisplay(ll_ScrollView3, mPhotoList3, lists, Config.TYPE_VIDEO_PHOTO);
		}
	}

	/**
	 * 
	 * @param parent
	 * @param totaldata
	 * @param data
	 */
	public void addDataAndDisplay(LinearLayout parent, List<String> totaldata, List<String> data, String returntype) {
		if (parent != null && totaldata != null && data != null) {
			for (String str : data) {
				if (parent.getChildCount() >= 6) {
					ToastUtils.showToast("最多只能选六张！");
					return;
				}
				addImageToHsv(str, PHOTO, parent, returntype);
				totaldata.add(str);
			}
		}

	}

	/************************************** 需要上传的参数集合 ***********************/
	private String diagnosis_stime; // 诊断起始时间
	private String diagnosis_etime;// 诊断截止时间
	private String diagnosis_hospital; // 检查医院
	private String diagnosis_way; // 诊断方式
	private String lab_exam_program;// 实验室检查项目
	private String lab_exam_time; // 实验室检查时间
	private String lab_exam_hospital; // 实验室检查医院
	private String image_exam_program;// 影像学检查项目
	private String image_exam_time;// 影像学检查时间
	private String image_exam_hospital; // 影像学检查医院
	// 图片文件上传

	/**
	 * 设置输入框的内容
	 */
	private void setEditContent() {
		diagnosis_hospital = et_name.getText().toString();
		lab_exam_hospital = et_lab_checkname.getText().toString();
		image_exam_hospital = et_vedio_checkname.getText().toString();
	}

	/**
	 * 检验数据是否存在空，用于需要上传的时候的判断
	 * 
	 * @return
	 */
	
	private boolean checkFirst(){
		if(TextUtils.isEmpty(diagnosis_stime) 
				&& TextUtils.isEmpty(diagnosis_etime) 
				&& TextUtils.isEmpty(diagnosis_hospital) 
				&& TextUtils.isEmpty(diagnosis_way)){
			return false;
		}
		if (TextUtils.isEmpty(diagnosis_stime)) {
			ToastUtils.showToast("请选择诊断起始时间");
			return false;
		}
		if (TextUtils.isEmpty(diagnosis_etime)) {
			ToastUtils.showToast("请选择诊断结束时间");
			return false;
		}
		if (TextUtils.isEmpty(diagnosis_hospital)) {
			ToastUtils.showToast("请输入检查医院");
			return false;
		}
		if (TextUtils.isEmpty(diagnosis_way)) {
			ToastUtils.showToast("请选择诊断方式");
			return false;
		}
		return true;
	}
	
	private boolean checkSecond(){
		if(TextUtils.isEmpty(lab_exam_program) 
				&& TextUtils.isEmpty(lab_exam_time) 
				&& TextUtils.isEmpty(lab_exam_hospital)){
			return false;
		}
		if (TextUtils.isEmpty(lab_exam_program)) {
			ToastUtils.showToast("请选择实验室检查项目");
			return false;
		}
		if (TextUtils.isEmpty(lab_exam_time)) {
			ToastUtils.showToast("请选择实验室检查时间");
			return false;
		}
		if (TextUtils.isEmpty(lab_exam_hospital)) {
			ToastUtils.showToast("请输入实验室检查医院");
			return false;
		}
		return true;
	}
	
	private boolean checkThired(){
		if(TextUtils.isEmpty(image_exam_program) 
				&& TextUtils.isEmpty(image_exam_time) 
				&& TextUtils.isEmpty(image_exam_hospital)){
			return false;
		}
		if (TextUtils.isEmpty(image_exam_program)) {
			ToastUtils.showToast("请选择影像学检查项目");
			return false;
		}
		if (TextUtils.isEmpty(image_exam_time)) {
			ToastUtils.showToast("请选择影像学检查时间");
			return false;
		}
		if (TextUtils.isEmpty(image_exam_hospital)) {
			ToastUtils.showToast("请输入影像学检查医院");
			return false;
		}
		return true;
	}

	private boolean checkTheContent() {
		
		if(checkFirst() || checkSecond() || checkThired()){
			return true;
		} 
		return false;
	}

	/**
	 * 添加数据到model用于请求发送
	 * 
	 * @return
	 */
	private ModelAddNowCase addDataToModel() {
		ModelAddNowCase addCase = new ModelAddNowCase();
		ModelDiagnosis diagnosis = new ModelDiagnosis();
		ModelLab lab = new ModelLab();
		ModelImage image = new ModelImage();
		diagnosis.setDiagnosis_stime(diagnosis_stime);
		diagnosis.setDiagnosis_etime(diagnosis_etime);
//		Log.d("Cathy", "diagnosis_stime = "+diagnosis_stime);
//		Log.d("Cathy", "diagnosis_etime = "+diagnosis_etime);
		diagnosis.setDiagnosis_hospital(diagnosis_hospital);
		diagnosis.setDiagnosis_way(diagnosis_way);
		lab.setLab_exam_program(lab_exam_program);
		lab.setLab_exam_time(lab_exam_time);
		lab.setLab_exam_hospital(lab_exam_hospital);
		image.setImage_exam_program(image_exam_program);
		image.setImage_exam_time(image_exam_time);
		image.setImage_exam_hospital(image_exam_hospital);
		addCase.setDiagnosis(diagnosis);
		addCase.setLab_exam(lab);
		addCase.setImage_exam(image);
		addCase.setDiagnosisList(mPhotoList1);
		addCase.setLab_examList(mPhotoList2);
		addCase.setImage_examList(mPhotoList3);
		// 图片待添加
		return addCase;
	}

	/*************************** 添加图片 ***********************************************/
	/**
	 * 添加图片到下面布局中
	 */
	private final int ADDPHOTO = 0;
	private final int PHOTO = 1;
	private ArrayList<String> mPhotoList1 = new ArrayList<String>();
	private ArrayList<String> mPhotoList2 = new ArrayList<String>();
	private ArrayList<String> mPhotoList3 = new ArrayList<String>();

	private void addImageToHsv(String path, int type, final LinearLayout parent, final String returnType) {
		View itemView = mInflater.inflate(R.layout.hsv_img_item, null);
		ImageView big_image = (ImageView) itemView.findViewById(R.id.big_image);
		ImageView delete_image = (ImageView) itemView.findViewById(R.id.delete_image);
		if (type == PHOTO) {
			if (path != null) {
				mImageManager.displayImage(big_image, path, R.drawable.default_image_small, 100, 100);
				delete_image.setTag(itemView);
				parent.addView(itemView);
				changeThePosition(parent, itemView);
				delete_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						View view = (View) v.getTag();
						parent.removeView(view);
					}
				});
			}
		} else if (type == ADDPHOTO) {
			big_image.setBackgroundResource(R.drawable.add);
			itemView.setTag("tag");
			delete_image.setVisibility(View.GONE);
			parent.addView(itemView);
			big_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mApp.startActivityForResult_qcj(PatientNowHistoryActivity.this, LocalImagListActivity.class,
							sendDataToBundle(returnType, null));
				}
			});
		}
	}

	/**
	 * 交换位置
	 *
	 * @param parent
	 *            父布局
	 * @param itemView
	 *
	 */
	private void changeThePosition(LinearLayout parent, View itemView) {
		int sum = parent.getChildCount();
		for (int i = 0; i < sum; i++) {
			View view = parent.getChildAt(i);
			String str = (String) view.getTag();
			if (str != null && str == "tag") {
				parent.removeView(view);
				parent.addView(view);
			}
		}
	}

}
