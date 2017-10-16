package com.winning.mobileclinical.model.cis;

/**
 * 
 * @Description: js�����������ʾ���ڿؼ��Ĳ����bean
 * @author wyt
 * @date 2016-9-13 ����1:28:03
 */
public class PickerInfo {
	/**
	 * �Ҵ���7������ istimeselector, israngeselector, title, min, max, begin, end
	 * istimeselector : string "1" ��ʾ ��ȷ��ʱ�� "0"��ʾֻ��ȷ��������
	 * israngeselector: string"1" ��ʾѡ��ʱ��� "0" ��ʾѡ��ʱ��� title : string ����
	 * min: string �������С���� ��ʽ"2016010100:01", ������ַ����ʾû�����ޡ� max:
	 * string ������������ ��ʽ "2016010100:01",������ַ����ʾû�����ޡ� begin:
	 * string ��ʼ���ڳ�ʼֵ ��ʽ "2016010100:01" end : string�������ڳ�ʼֵ ��ʽ
	 * "2016010100:01"
	 */

	public String istimeselector;
	public String israngeselector;
	public String title;
	public String min;
	public String max;
	public String begin;
	public String end;

	/**
	 * 
	 * @param istimeselector
	 *            string "1" ��ʾ ��ȷ��ʱ�� "0"��ʾֻ��ȷ��������
	 * @param israngeselector
	 *            string"1" ��ʾѡ��ʱ��� "0" ��ʾѡ��ʱ���
	 * @param title
	 *            ����
	 * @param min
	 *            �������С���� ��ʽ"2016010100:01", ������ַ����ʾû������
	 * @param max
	 *            ������������ ��ʽ "2016010100:01",������ַ����ʾû�����ޡ�
	 * @param begin
	 *            ��ʼ���ڳ�ʼֵ ��ʽ "2016010100:01"
	 * @param end
	 *            �������ڳ�ʼֵ ��ʽ "2016010100:01"
	 */
	public PickerInfo(String istimeselector, String israngeselector, String title, String min, String max, String begin, String end) {
		super();
		this.istimeselector = istimeselector;
		this.israngeselector = israngeselector;
		this.title = title;
		this.min = min;
		this.max = max;
		this.begin = begin;
		this.end = end;
	}

}
