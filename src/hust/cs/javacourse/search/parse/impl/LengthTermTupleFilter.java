package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;
/**
 * <pre>
 * LengthTermTupleFilter是AbstractTermTupleFilter的一个具体子类,里面包含另一个
 * AbstractTermTupleStream对象作为输入，并根据长度对输入的AbstractTermTupleStream进行过滤,
 *
 * </pre>
 */
public class LengthTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * MAX_LENGTH是指单词长度允许的最大值
     */
    private static final int MAX_LENGTH = Config.TERM_FILTER_MAXLENGTH;
    /**
     * MIN_LENGTH是指单词长度允许的最小值
     */
    private static final int MIN_LENGTH = Config.TERM_FILTER_MINLENGTH;

    /**
     * 构造函数
     * @param input：Filter的输入，类型为AbstractTermTupleStream
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }
    /**
     * 获得下一个三元组
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tuple = input.next();
        if (tuple == null) {
            return null;
        }
        int length = tuple.term.getContent().length();
        while (length < MIN_LENGTH || length > MAX_LENGTH) {
            tuple = input.next();
            if (tuple == null){
                return null;
            }
            length = tuple.term.getContent().length();
        }
        return tuple;
    }
}
