package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TermTupleScanner extends AbstractTermTupleScanner {
    /**
     * 缓存读入一行生成的三元组列表
     */
    private ArrayList<AbstractTermTuple> tuples = new ArrayList<>();
    /**
     * 分词器
     */
    private StringSplitter splitter = new StringSplitter();
    /**
     * 记录当前遍历到的文档位置
     */
    private int curPos = 0;
    /**
     * 构造函数
     * @param input：指定输入流对象，应该关联到一个文本文件
     */
    public TermTupleScanner(BufferedReader input) {
        super(input);
        splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
    }
    /**
     * 获得下一个三元组
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        if (tuples.isEmpty()) {
            String str;
            try {
                str = input.readLine();
                while (str != null && str.isEmpty()) {
//                    System.out.println("line: " + str);
                    str = input.readLine();
                }
//                System.out.println("line: " + str);
                if (str == null) {
//                    System.out.println("--------------------------------------------");
                    return null;
                }
                List<String> terms = splitter.splitByRegex(str);
                for (String term : terms) {
                    TermTuple termTuple = new TermTuple();
                    termTuple.term = new Term(term.toLowerCase());
                    termTuple.curPos = curPos;
                    curPos++;
                    tuples.add(termTuple);
                }
                AbstractTermTuple tuple = tuples.get(0);
                tuples.remove(0);
                return tuple;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AbstractTermTuple tuple = tuples.get(0);
            tuples.remove(0);
            return tuple;
        }
        return null;
    }
}
