package org.nbone.util.json;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;

import org.apache.commons.lang.StringUtils;
import org.nbone.test.UserTest;

public class JSONXOperUtils {

	private static XMLSerializer xmlSerializer = new XMLSerializer();

	/**
	 * Java 对象转化成Xml
	 * 
	 * @param pojo
	 *            Object
	 * @param xmlSer
	 *            XML标签配置信息
	 * @return XMlString
	 */
	public static String pojo2XML(Object pojo, XMLSerializer xmlSer) {
		JSON json = null;
		if (pojo instanceof Collection || pojo instanceof Object[]) {
			json = JSONArray.fromObject(pojo);
		} else {
			json = JSONObject.fromObject(pojo);
		}
		if (xmlSer == null) {
			xmlSer = new XMLSerializer();
		}
		String xml = xmlSer.write(json);
		return xml;
	}

	/**
	 * @param pojo
	 * @return
	 * @see #pojo2XML(Object, XMLSerializer)
	 */
	public static String pojo2XML(Object pojo) {
		return pojo2XML(pojo, new XMLSerializer());
	}

	/**
	 * JSON 转化成 XML
	 * 
	 * @param p_json
	 *            JSON String
	 * @param xmlSer
	 *            XMl标签配置信息
	 * @return XML
	 */
	public static String json2XML(String p_json, XMLSerializer xmlSer) {
		JSON json = null;
		String xml = null;
		if (StringUtils.isNotEmpty(p_json)) {
			if (p_json.startsWith("{") && p_json.endsWith("}")) {

				json = JSONObject.fromObject(p_json);

			} else if (p_json.startsWith("[") && p_json.endsWith("]")) {
				json = JSONArray.fromObject(p_json);

			} else {
				throw new JSONException(
						"A JSONObject text must begin with '{' or [");
			}
			if (xmlSer == null) {
				xmlSer = new XMLSerializer();
			}
			xml = xmlSer.write(json);

		}
		return xml;
	}

	/**
	 * @param json
	 * @return
	 * @see #json2XML(String, XMLSerializer)
	 */
	public static String json2XML(String json) {

		return json2XML(json, new XMLSerializer());
	}

	/**
	 * xml转化成JSON
	 * 
	 * @param xml
	 *            xml字符串
	 * @param isRootArray
	 *            根节点是否为数组,isRootArray=true时设置根节点为数组;<br>
	 *            否则自动判断,可能为Array(当根节点的子元素>1时 root为Array);<br>
	 *            也有可能为Object(当根节点的子元素=1时 root为Object).
	 * 
	 * @return
	 */
	public static String xml2JSON(String xml, boolean isRootArray) {
		Element root;
		Document doc;
		// 设置root为数组
		if (isRootArray) {
			xml = setXmlRootAttrToArray(xml);
		}
		XMLSerializer xmlSer = new XMLSerializer();
		return xmlSer.read(xml).toString();
	}

	/**
	 * @param xml
	 * @return
	 * @see #xml2JSON(String, boolean)
	 */
	public static String xml2JSON(String xml) {

		return xml2JSON(xml, true);
	}
     
	
	/**
	 * 将简单xml中信息转化Map<br>
	 * 
	 * @param xml
	 * @return
	 */
	public static Map xml2Map(String xml) {
		//案例：
		//<object><id type="string">77777</id><userName type="string">chenyc</userName></object>
		XMLSerializer xmlSer = new XMLSerializer();
		JSON json = xmlSer.read(xml);
		
		return (JSONObject)json;
	}
	/**
	 * 将xml中列表信息转化对象数组
	 * 
	 * @param xml
	 * @return
	 */
	public static Object[] xml2ArrayForMap(String xml) {

		// 设置root为数组
		xml = setXmlRootAttrToArray(xml);
		XMLSerializer xmlSer = new XMLSerializer();
		JSON json = xmlSer.read(xml);
		if(json.isArray()){
			return ((JSONArray) json).toArray();
		}
		return new Object[]{json};
	}

	/**
	 * 将xml中列表信息转化对象数组
	 * 
	 * @param xml
	 * @param beanClass
	 * @return
	 */
	public static Object[] xml2ArrayForObject(String xml, Class beanClass) {

		// 设置root为数组
		xml = setXmlRootAttrToArray(xml);
		XMLSerializer xmlSer = new XMLSerializer();
		JSON jsonArr = xmlSer.read(xml);
		Object[] objArr = new Object[jsonArr.size()];

		for (int i = 0; i < jsonArr.size(); i++) {
			objArr[i] = JSONObject.toBean(
					((JSONArray) jsonArr).getJSONObject(i), beanClass);
		}

		return objArr;
	}

	/**
	 * 设置xml的根节点元素为Array结构 例如<datas class="array"></datas>
	 * 
	 * @param xml
	 * @return
	 */
	private static String setXmlRootAttrToArray(String xml) {
		Element root;
		Document doc;
		try {
			doc = (new Builder()).build(new StringReader(xml));
			root = doc.getRootElement();
			Attribute attr = root.getAttribute("class");
			root.addAttribute(new Attribute("class", "array"));
			xml = root.toXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;

	}

	// test main
	public static void main(String[] args) {
		List list = new ArrayList();
		Map map1 = new HashMap();
		map1.put("id", "77777");
		map1.put("userName", "chenyc");
		list.add(map1);
		list.add(map1);
        //数组
		String xmls = pojo2XML(list, new JSONXConfig("datas", "node", "node"));
		System.out.println("xmls---:" + xmls);
		//单个对象
		String xml = pojo2XML(map1, new JSONXConfig("datas", "node1", "object1"));
		System.out.println("xml---:" + xml);
        
		String jsonString = xml2JSON(xmls, true);
		System.out.println("jsonString==" + jsonString);

		
		Object[] obj = xml2ArrayForMap(xmls);
		Map  map = xml2Map(xml);
		Object[] obj1 = xml2ArrayForObject(xmls, UserTest.class);
		
		System.out.println("obj=="+obj[0]);
		System.out.println("map=="+map);

	}

}
