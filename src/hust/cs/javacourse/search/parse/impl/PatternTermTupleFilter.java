package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

public class PatternTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 过滤掉不符合该正则表达式的term
     */
    private static final String PATTERN_REGEX = Config.TERM_FILTER_PATTERN;

    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public PatternTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }
    /**
     * 获得下一个三元组
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tuple = input.next();
        while (tuple != null && !tuple.term.getContent().matches(PATTERN_REGEX)) {
            tuple = input.next();
        }
        return tuple;
    }
}
