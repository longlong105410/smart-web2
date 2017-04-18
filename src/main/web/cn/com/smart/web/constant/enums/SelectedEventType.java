package cn.com.smart.web.constant.enums;

import cn.com.smart.constant.enumdef.EnumInterface;

/**
 * 选择事件触发类型
 * @author lmq
 *
 */
public enum SelectedEventType implements EnumInterface {
	OPEN_TO_TARGET(1,"open_to_target");
	private int index;
	private String value;
	
	private SelectedEventType(int index,String value) {
		this.index = index;
		this.value = value;
	}
	
	@Override
	public String getValue(int index) {
		String valueTmp = null;
		for (SelectedEventType selectedEventType : SelectedEventType.values()) {
			if(selectedEventType.getIndex() == index) {
				valueTmp = selectedEventType.getValue();
				break;
			}
		}
		return valueTmp;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
