package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.query.impl.IndexSearcher;

import java.util.Arrays;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     * 搜索程序入口
     *
     * @param args ：命令行参数
     */
    public static void main(String[] args) {
        IndexSearcher indexSearcher = new IndexSearcher();
        indexSearcher.open("D:\\Java新实验一\\output\\index.txt");
//        AbstractTerm queryTerm = new Term();
//        queryTerm.setContent("activity");
//        AbstractHit[] results = indexSearcher.search(queryTerm, new SimpleSorter());
//        AbstractTerm queryTerm1=new Term("google");
//        AbstractTerm queryTerm2=new Term("activity");
//        AbstractHit[] results=indexSearcher.search(queryTerm1,queryTerm2,new SimpleSorter(), AbstractIndexSearcher.LogicalCombination.OR);
//        System.out.println(Arrays.toString(results));
        AbstractTerm queryTerm1=new Term("capital");
        AbstractTerm queryTerm2=new Term("destination");
        AbstractHit[] results=indexSearcher.search(queryTerm1,queryTerm2,new SimpleSorter());
        System.out.println(Arrays.toString(results));
    }
}
