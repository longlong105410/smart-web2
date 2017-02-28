package cn.com.smart.constant.enumdef;

/**
 * 数据格式类型
 * @author lmq
 * @version 1.0
 * @since 1.0
 * 2015年8月22日
 */
public enum DataFormatType implements EnumInterface {
	
	NUM(0,"num"),              //数字（包括小数）
	DECIMAL(1,"decimal"),     //小数
	INTEGER(2,"integer"),    //整数
	IP(3,"ip"),             //IP
	EMAIL(4,"email"),      //email
	MOBILE_PHONE(5,"mobile_phone"),        //手机号码
	FIXED_TELPHONE(6,"fixed_telephone"),  //固定电话
	CHINESE(7,"chinese"),                //中文
	QQ(8,"qq"),                         //qq
	CARD_NO(10,"card_no"),             //身份证号码
	TEXT(11,"text");
	
	
	private String value;
	private int index;
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	private DataFormatType(int index,String value) {
		this.value = value;
		this.index = index;
	}
	
	@Override
	public String getValue(int index) {
		String valueTmp = null;
		for (DataFormatType dataFormate : DataFormatType.values()) {
			if(dataFormate.getIndex() == index) {
				valueTmp = dataFormate.getValue();
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
