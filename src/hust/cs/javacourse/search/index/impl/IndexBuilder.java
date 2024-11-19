package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * <pre>
 * IndexBuilder是AbstractIndexBuilder的具体实现子类
 * 完成索引构造的工作
 * </pre>
 */
public class IndexBuilder extends AbstractIndexBuilder {
    /**
     * 构造函数
     * @param docBuilder：构建索引必须解析文档构建Document对象，因此包含AbstractDocumentBuilder的子类对象
     */
    public IndexBuilder(AbstractDocumentBuilder docBuilder) {
        super(docBuilder);
    }
    /**
     * <pre>
     * 构建指定目录下的所有文本文件的倒排索引.
     *      需要遍历和解析目录下的每个文本文件, 得到对应的Document对象，再依次加入到索引，并将索引保存到文件.
     * @param rootDirectory ：指定目录
     * @return ：构建好的索引
     * </pre>
     */
    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        AbstractIndex index = new Index();
        File file = new File(rootDirectory);
        File[] files = file.listFiles();
        if(files==null) return null;
        for (File f : files) {
            AbstractDocument document = docBuilder. build(docId, f.getPath(), f);
            index.addDocument(document);
            docId++;
        }
        return index;
    }
}
