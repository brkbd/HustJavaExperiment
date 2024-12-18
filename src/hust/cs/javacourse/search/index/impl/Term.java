package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * <pre>
 * Term是AbstractTerm对象的具体子类.
 *      Term对象表示文本文档里的一个单词.
 *      必须实现下面二个接口:
 *          Comparable：可比较大小（字典序）,为了加速检索过程，字典需要将单词进行排序.
 *          FileSerializable：可序列化到文件或从文件反序列化.
 *   </pre>
 */
public class Term extends AbstractTerm {
    /**
     * 构造函数
     *
     * @param content ：Term内容
     */
    public Term(String content) {
        super(content);
    }

    /**
     * 缺省构造函数
     */
    public Term() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Term) {
            return content.equals(((Term) obj).getContent());
        }
        return false;
    }
    /**
     * 返回Term的字符串表示
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return content;
    }
    /**
     * 返回Term内容
     *
     * @return Term内容
     */
    @Override
    public String getContent() {
        return content;
    }
    /**
     * 设置Term内容
     *
     * @param content：Term的内容
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * 比较二个Term大小（按字典序）
     *
     * @param o： 要比较的Term对象
     * @return ： 返回二个Term对象的字典序差值
     */
    @Override
    public int compareTo(AbstractTerm o) {
        return content.compareTo(o.getContent());
    }
    /**
     * 将term对象序列化写进输出流对象
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取输入流对象，进行反序列化得到term对象
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.content = (String) (in.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
