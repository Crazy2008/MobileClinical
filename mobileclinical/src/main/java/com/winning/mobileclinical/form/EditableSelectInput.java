package com.winning.mobileclinical.form;

import java.util.ArrayList;
import java.util.HashMap;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.MBControl;
import com.winning.mobileclinical.model.OptionItem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EditableSelectInput extends LinearLayout implements CustomForm{

	private TextView label;
	private EditText text;
	private Button sltButton;
	private String msg ;
	
	private Context c;
	
	private ListAdapter adapter;
	
	private ListView mList;
	private PopupWindow mPopup;
	
	private int index = -1;
	private TextWatcher watcher;
	
	public EditableSelectInput(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context); 
	}
	
	public EditableSelectInput(Context context){
		super(context);
		this.init(context);
	}

	private void init(Context context){
		this.c = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.text = new EditText(c);
		this.text.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT,1));
		this.text.setSingleLine(true);
		this.text.setTextSize(18);
		this.text.setTextColor(Color.parseColor("#333333"));
		this.text.setPadding(15, 15,15,15);
		this.text.setGravity(Gravity.CENTER);
		this.sltButton = new Button(c);
		sltButton.setText("▼");
		sltButton.setBackgroundResource(R.drawable.custombutton);
		sltButton.setLayoutParams(new LayoutParams(35,LayoutParams.FILL_PARENT,0));
		sltButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showOptions();
			}
		});
		
		this.addView(this.text);
		this.addView(this.sltButton);
		
		
//		this.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				showOptions();
//				return false;
//			}
//		});
	}
	
	public void showOptions(){
		if(mPopup==null){
			createList();
			LinearLayout layout =new LinearLayout(this.c);
			layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			layout.setBackgroundResource(R.drawable.custombutton);
			layout.addView(mList);
			mPopup =  new PopupWindow(layout, this.getWidth(), LayoutParams.WRAP_CONTENT);
			mPopup.setBackgroundDrawable(new BitmapDrawable());
			mPopup.setFocusable(true);
		    mPopup.setOutsideTouchable(true);
		    mPopup.update();
		}
		if(mPopup.isShowing()){
			mPopup.dismiss();
		}else{
	        mPopup.showAsDropDown(this);
		}
	}
	
	public void setIndex(int index){
		HashMap<String, Object> item = (HashMap<String, Object>) adapter.getItem(index);
		this.index = index;
		this.text.setText((CharSequence) item.get("text"));
	}
	
	public void setText(String text){
		this.text.setText(text);
	}
	
	public String getText(){
		return this.text.getText().toString();
	}
	
	public void setTextColor(int color){
		this.text.setTextColor(color);
	}
	
	public void setBackgroundResource(int res){
		this.text.setBackgroundResource(res);
	}
	
	@Override
	public String getTextValue() {
		if(index<0){
			return getText().toString().trim();
		}
		if(index>adapter.getCount()-1){
			index = -1;
			return getText().toString().trim();
		}
		HashMap<String, Object> item = (HashMap<String, Object>) adapter.getItem(this.index);
		return (String) item.get("value");
	}
	
	@Override
	public void setTextValue(String text) {
		for(int i = 0 ;i<adapter.getCount();i++){
			HashMap<String, Object> item = (HashMap<String, Object>) adapter.getItem(i);
			if(item.get("value").equals(text)){
				setIndex(i);
				return;
			}
		}
		setText(text);
	}
	
	@Override
	public void clear() {
		this.text.setText("");
		this.index = -1;
	}
	
	
	public void setOptions(ArrayList<OptionItem> arrayList){
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < arrayList.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("text", arrayList.get(i).getText());
			map.put("value", arrayList.get(i).getValue());
			data.add(map);
		}
		this.adapter = new SimpleAdapter(this.c, data,R.layout.customselect_item, 
				 new String[]{"text"}, new int[]{R.id.select_text} );
//		this.setIndex(0);
	}
	
	private void createList(){
		mList = new ListView(c);
		mList.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		mList.setCacheColorHint(Color.parseColor("#00000000"));
		mList.setAdapter(adapter);
		mList.setSelector(R.drawable.menu_selector);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OnItemSelect(parent, view, position, id);
				
			}
		});
	}
	
	private void OnItemSelect(AdapterView<?> parent, View view,
			int position, long id){
		HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
		this.index = position;
//		this.text.addTextChangedListener(watcher);
		this.text.setText((CharSequence) item.get("text"));
		mPopup.dismiss();
	}
	
	public void setAdapter(ListAdapter adapter){
		this.adapter = adapter;
	}
	
	public TextView getLabel() {
		return label;
	}

	public void setLabel(String label,String labelmc) {
		if(labelmc==null||labelmc.trim().equals("")){
			this.label = new TextView(this.getContext());
			this.label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,0));
			this.label.setText(label);
			this.label.setTextSize(18);
			this.label.setTextColor(Color.parseColor("#555555"));
		}else{
			this.label = new EditText(this.getContext());
			this.label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,0));
			this.label.setBackgroundResource(R.drawable.custom_edit);
			this.label.setMinimumWidth(80);
			this.label.setText(label);
		}
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public MBControl getMb() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMb(MBControl mb) {
		// TODO Auto-generated method stub
		
	}

	public TextWatcher getWatcher() {
		return watcher;
	}

	public void setWatcher(TextWatcher watcher) {
		this.watcher = watcher;
	}
	
	
}
