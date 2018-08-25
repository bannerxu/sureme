package top.xuguoliang.common.utils;

class MyString implements Comparable<MyString> {
	public String s;// 包装String

	public MyString(String s) {
		this.s = s;
	}

	@Override
	public int compareTo(MyString o) {
		if (o == null || o.s == null) {
			return 1;
		}
		return s.compareTo(o.s);
	}


}

