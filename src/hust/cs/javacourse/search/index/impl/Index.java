package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * <pre>
 * Index是内存中的倒排索引对象的具体类，是AbstractIndex的具体子类.
 *      一个倒排索引对象包含了一个文档集合的倒排索引.
 *      内存中的倒排索引结构为HashMap，key为Term对象，value为对应的PostingList对象.
 *      另外在AbstractIndex里还定义了从docId和docPath之间的映射关系.
 *      必须实现下面接口:
 *          FileSerializable：可序列化到文件或从文件反序列化.
 * </pre>
 */
public class Index extends AbstractIndex {
    /**
     * 返回索引的字符串表示
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        return "docIdToDocPathMapping: " + docIdToDocPathMapping + "\ntermToPostingListMapping:" + termToPostingListMapping;
    }
    /**
     * 添加文档到索引，更新索引内部的HashMap
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        if (docIdToDocPathMapping.get(document.getDocId()) == null) {
            docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
            int docId = document.getDocId();
            List<AbstractTermTuple> tuples = document.getTuples();
            for (AbstractTermTuple tuple : tuples) {
                if (getDictionary().contains(tuple.term)) {
                    AbstractPostingList postingList = search(tuple.term);
                    int docIndex = postingList.indexOf(docId);
                    List<Integer> positions = new ArrayList<>();
                    if (docIndex == -1) {
                        positions.add(tuple.curPos);
                        postingList.add(new Posting(docId, 1, positions));
                    } else {
                        AbstractPosting posting = postingList.get(docIndex);
                        posting.setFreq(posting.getFreq() + 1);
                        positions = posting.getPositions();
                        positions.add(tuple.curPos);
                    }
                } else {
                    AbstractPostingList postingList = new PostingList();
                    List<Integer> positions = new ArrayList<>();
                    positions.add(tuple.curPos);
                    postingList.add(new Posting(docId, 1, positions));
                    termToPostingListMapping.put(tuple.term, postingList);
                }
            }
        }
    }
    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try {
            readObject(new ObjectInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try {
            writeObject(new ObjectOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 返回指定单词的PostingList
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return termToPostingListMapping.get(term);
    }
    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return termToPostingListMapping.keySet();
    }
    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for (Map.Entry<AbstractTerm, AbstractPostingList> entry :
                termToPostingListMapping.entrySet()) {
            AbstractPostingList postingList = entry.getValue();
            postingList.sort();
            for (int i = 0; i < postingList.size(); i++) {
                postingList.get(i).sort();
            }
        }
    }
    /**
     * 根据docId获得对应文档的完全路径名
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
    }

    /**
     * 将index对象序列化写进输出流对象
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(docIdToDocPathMapping);
            out.writeObject(termToPostingListMapping);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取输入流对象，进行反序列化得到index对象
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docIdToDocPathMapping = (Map<Integer, String>) (in.readObject());
            this.termToPostingListMapping = (Map<AbstractTerm, AbstractPostingList>) (in.readObject());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
