package common;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServletContext {
    public static int port;//监听的服务器端口
    public static int maxThread;//最大线程数
    public static String protocol;//协议名
    public static String webRoot;//指定资源所在的位置
    public static String notFoundPage;//404页面
    public static String homePage;//首页
    public static Map<String,String> typeMap = new HashMap<String, String>();

    static {
        init();
    }

    //读取xml文件并初始化
    private static void init() {
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read("config/web.xml");
            Element serverElement = document.getRootElement();
            Element connEle = serverElement.element("service").element("connector");
            port = Integer.valueOf(connEle.attributeValue("port"));
            maxThread = Integer.valueOf(connEle.attributeValue("maxThread"));
            protocol = connEle.attributeValue("protocol");
            webRoot = serverElement.element("service").elementText("webRoot");
            notFoundPage = serverElement.element("service").elementText("notFoundPage");
            homePage = serverElement.element("service").elementText("homePage");
            List<Element> typeList = serverElement.element("type-mappings").elements();
            for(Element e : typeList){
                String key = e.attributeValue("ext");
                String value = e.attributeValue("type");
                typeMap.put(key,value);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
