package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;

import java.util.Map;
/**
 * <pre>
 * Hit是一个搜索命中结果的具体类. 是AbstractHit的具体子类，要实现Comparable接口.
 * 实现该接口是因为需要必须比较大小，用于命中结果的排序.
 * </pre>
 */
public class Hit extends AbstractHit implements Comparable<AbstractHit> {
    /**
     * 默认构造函数
     */
    public Hit() {
    }
    /**
     * 构造函数
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     */
    public Hit(int docId, String docPath) {
        super(docId, docPath);
    }
    /**
     * 构造函数
     * @param docId              ：文档id
     * @param docPath            ：文档绝对路径
     * @param termPostingMapping ：命中的三元组列表
     */
    public Hit(int docId, String docPath, Map<AbstractTerm, AbstractPosting> termPostingMapping) {
        super(docId, docPath, termPostingMapping);
    }
    /**
     * 获得文档id
     * @return ： 文档id
     */
    @Override
    public int getDocId() {
        return docId;
    }
    /**
     * 获得文档绝对路径
     * @return ：文档绝对路径
     */
    @Override
    public String getDocPath() {
        return docPath;
    }
    /**
     * 获得文档内容
     * @return ： 文档内容
     */
    @Override
    public String getContent() {
        return content;
    }
    /**
     * 设置文档内容
     * @param content ：文档内容
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * 获得文档得分
     * @return ： 文档得分
     */
    @Override
    public double getScore() {
        return score;
    }
    /**
     * 设置文档得分
     * @param score ：文档得分
     */
    @Override
    public void setScore(double score) {
        this.score = score;
    }
    /**
     * 获得命中的单词和对应的Posting键值对
     * @return ：命中的单词和对应的Posting键值对
     */
    @Override
    public Map<AbstractTerm, AbstractPosting> getTermPostingMapping() {
        return termPostingMapping;
    }
    /**
     * 比较二个命中结果的大小，根据score比较
     * @param o     ：要比较的名字结果
     * @return      ：二个命中结果得分的差值
     */
    @Override
    public int compareTo(AbstractHit o) {
        return (int) (score - o.getScore());
    }
    /**
     * 获得命中结果的字符串表示, 用于显示搜索结果.
     * @return : 命中结果的字符串表示
     */
    @Override
    public String toString() {
        return "Hit{\n" +
                "docId=" + docId +
                ", \ndocPath='" + docPath + '\'' +
                ", \ncontent='" + content + '\'' +
                ", \ntermPostingMapping=" + termPostingMapping +
                ", \nscore=" + score +
                "}\n";
    }
}
