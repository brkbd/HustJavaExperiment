package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.util.Config;

import java.io.File;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     * 索引构建程序入口
     *
     * @param args : 命令行参数
     */
    public static void main(String[] args) {
//        Term term=new Term("We have a dream.");
//        System.out.println(term);
        AbstractDocumentBuilder documentBuilder = new DocumentBuilder();
        AbstractIndexBuilder indexBuilder = new IndexBuilder(documentBuilder);
        AbstractIndex index = indexBuilder.buildIndex("D:\\Java新实验一\\功能测试数据集");
        System.out.println(index);
        index.save(new File("D:\\Java新实验一\\output\\index.txt"));
//        AbstractIndex loadIndex = new Index();
//        loadIndex.load(new File("D:\\Java新实验一\\output\\index.txt"));
//        System.out.println("loadIndex:\n"+loadIndex);
    }
}
