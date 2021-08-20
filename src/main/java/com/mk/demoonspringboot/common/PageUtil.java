package com.mk.demoonspringboot.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PageUtil {

    /**
     * 多数据源分页
     * @param pageNum
     * @param pageSize
     * @param allList
     * @param <T>
     * @return
     */
    public static <T> List<T> pageMultiList(int pageNum, int pageSize, List<T>... allList) {
        List<T> page = new ArrayList<>(pageSize);
        int startIdx = (pageNum - 1) * pageSize;
        Iterator<List<T>> iterator = Arrays.stream(allList).iterator();
        int pos = 0;
        while(pos <= startIdx && page.size() < pageSize && iterator.hasNext()) {
            List<T> subList = iterator.next();
            int d = startIdx - pos;
            pos += Math.min(d, subList.size());
            if(pos == startIdx && d < subList.size()) {
                page.addAll(subList.subList(d, Math.min(pageSize-page.size()+ d, subList.size())));
            }
        }
        return page;
    }

}
