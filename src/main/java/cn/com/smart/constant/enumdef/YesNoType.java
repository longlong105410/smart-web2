package cn.com.smart.constant.enumdef;

/**
 * 是否类型
 * @author lmq
 *
 */
@Deprecated
public enum YesNoType implements EnumInterface {
	NO(0,"0"),YES(1,"1");
	
	private String value;
	private int index;
	private YesNoType(int index,String value) {
		this.value = value;
		this.index = index;
	}
	
	@Override
	public String getValue(int index) {
		String valueTmp = null;
		for (YesNoType yesNoType : YesNoType.values()) {
			if(yesNoType.getIndex() == index) {
				valueTmp = yesNoType.getValue();
				break;
			}
		}
		return valueTmp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
