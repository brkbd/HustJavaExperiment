package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.Posting;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import javax.sound.sampled.spi.AudioFileReader;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
/**
 * <pre>
 *  IndexSearcher是AbstractIndexSearcher的具体子类
 * </pre>
 */
public class IndexSearcher extends AbstractIndexSearcher {
    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        index.load(new File(indexFile));
        index.optimize();
//        System.out.println(index);
    }
    /**
     * 根据单个检索词进行搜索
     * @param queryTerm ：检索词
     * @param sorter ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postingList = index.search(queryTerm);
        if (postingList == null) return null;
        List<AbstractHit> hitList = new ArrayList<>();
        for (int i = 0; i < postingList.size(); i++) {
            AbstractPosting posting = postingList.get(i);
            Map<AbstractTerm, AbstractPosting> map = new TreeMap<>();
            map.put(queryTerm, posting);
            AbstractHit hit = new Hit(posting.getDocId(), index.docIdToDocPathMapping.get(posting.getDocId()), map);
            hit.setScore(-posting.getFreq());
            sorter.score(hit);
            hitList.add(hit);
        }
        sorter.sort(hitList);
        return hitList.toArray(new AbstractHit[0]);
    }
    /**
     *
     * 根据二个检索词进行搜索
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter ：    排序器
     * @param combine ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractPostingList postingList = index.search(queryTerm1);
        List<AbstractHit> hitList = new ArrayList<>();
        if (postingList == null) return null;
        Map<Integer, AbstractHit> hitMap = new TreeMap<>();
        for (int i = 0; i < postingList.size(); i++) {
            AbstractPosting posting = postingList.get(i);
            Map<AbstractTerm, AbstractPosting> map = new TreeMap<>();
            map.put(queryTerm1, posting);
            AbstractHit hit = new Hit(posting.getDocId(), index.docIdToDocPathMapping.get(posting.getDocId()), map);
            hit.setScore(-posting.getFreq());
            sorter.score(hit);
            hitMap.put(posting.getDocId(), hit);
        }
        postingList = index.search(queryTerm2);
        if (postingList == null) return null;
        if (combine == LogicalCombination.OR) {
            for (int i = 0; i < postingList.size(); i++) {
                AbstractPosting posting = postingList.get(i);
                AbstractHit hit;
                if ((hit = hitMap.get(posting.getDocId())) == null) {
                    Map<AbstractTerm, AbstractPosting> map = new TreeMap<>();
                    map.put(queryTerm2, posting);
                    hit = new Hit(posting.getDocId(), index.docIdToDocPathMapping.get(posting.getDocId()), map);
                    hit.setScore(-posting.getFreq());
                    sorter.score(hit);
                    hitMap.put(posting.getDocId(), hit);
                } else {
                    Map<AbstractTerm, AbstractPosting> map = hit.getTermPostingMapping();
                    map.put(queryTerm2, posting);
                    double score = hit.getScore();
                    hit.setScore(-posting.getFreq() + score);
                    sorter.score(hit);
                }
            }
            return hitMap.values().toArray(new AbstractHit[0]);
        } else if (combine == LogicalCombination.AND) {
            for (int i = 0; i < postingList.size(); i++) {
                AbstractPosting posting = postingList.get(i);
                AbstractHit hit;
                if ((hit = hitMap.get(posting.getDocId())) != null) {
                    Map<AbstractTerm, AbstractPosting> map = hit.getTermPostingMapping();
                    map.put(queryTerm2, posting);
                    double score = hit.getScore();
                    hit.setScore(-posting.getFreq() + score);
                    sorter.score(hit);
                    hitList.add(hit);
                }
            }
            return hitList.toArray(new AbstractHit[0]);
        }
        return new AbstractHit[0];
    }

    /**
     * 根据两个连着的检索词进行搜索
     * @param queryTerm1：第1个检索词
     * @param queryTerm2：第2个检索词
     * @param sorter：排序器
     * @return：命中结果数组
     */
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter) {
        AbstractPostingList postingList1 = index.search(queryTerm1);
        AbstractPostingList postingList2 = index.search(queryTerm2);
        if (postingList1 == null || postingList2 == null)
            return new AbstractHit[0];
        int i = 0, j = 0, len1 = postingList1.size(), len2 = postingList2.size();
        List<AbstractHit> hitList = new ArrayList<>();
        while (i < len1 && j < len2) {//双指针法
            AbstractPosting posting1 = postingList1.get(i);
            AbstractPosting posting2 = postingList2.get(j);
            if (posting1.getDocId() == posting2.getDocId()) {
                AbstractTerm term = new Term(queryTerm1.getContent() + " " + queryTerm2.getContent());
                Map<AbstractTerm, AbstractPosting> map = new TreeMap<>();
                List<Integer> pos1 = new ArrayList<>(posting1.getPositions());
                List<Integer> pos2 = new ArrayList<>(posting2.getPositions());
                pos2.replaceAll(integer -> integer - 1);
                List<Integer> pos = new ArrayList<>();
                for (Integer integer : pos1) {
                    if (pos2.contains(integer)) {
                        pos.add(integer);
                    }
                }
                if(pos.isEmpty()){
                    i++;
                    j++;
                    continue;
                }
                AbstractPosting posting = new Posting(posting1.getDocId(), pos.size(), pos);
                map.put(term, posting);
                AbstractHit hit = new Hit(posting1.getDocId(), index.docIdToDocPathMapping.get(posting.getDocId()), map);
                hit.setScore(-pos.size());
                sorter.score(hit);
                hitList.add(hit);
                i++;
                j++;
            } else if (posting1.getDocId() > posting2.getDocId()) {
                j++;
            } else {
                i++;
            }
        }
        return hitList.toArray(new AbstractHit[0]);
    }
}
