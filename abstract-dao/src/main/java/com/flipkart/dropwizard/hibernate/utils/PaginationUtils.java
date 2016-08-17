package com.flipkart.dropwizard.hibernate.utils;

import org.hibernate.Criteria;

/**
 * Created by rishabh.sood on 17/08/16.
 */
public class PaginationUtils {
    public static void paginate(Criteria criteria, int pageNumber, int pageSize) {
        int startIndex =  (pageNumber - 1) * pageSize;
        criteria.setFirstResult(startIndex);
        criteria.setMaxResults(pageSize);
    }

    public static void validateParams(Integer pageNumber, Integer pageSize, Integer maxPageSize){
        if(pageNumber == null) {
            throw new IllegalArgumentException("Page Number is a mandatory parameter");
        }
        if(pageSize == null) {
            throw new IllegalArgumentException("Page Size is a mandatory parameter");
        }
        if(pageSize > maxPageSize) {
            throw new IllegalArgumentException("Page Size must be less than " + maxPageSize);
        }
    }

}
