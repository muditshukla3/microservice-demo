package com.microservice.demo.common.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionsUtil {

    private CollectionsUtil() {
    }

    private static class CollectionUtilHolder{
        static final CollectionsUtil INSTANCE = new CollectionsUtil();
    }

    public <T> List<T> getListFromIterable(Iterable<T> iterable){
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
