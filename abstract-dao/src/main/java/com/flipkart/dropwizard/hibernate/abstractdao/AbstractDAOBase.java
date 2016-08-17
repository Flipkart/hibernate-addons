package com.flipkart.dropwizard.hibernate.abstractdao;

import com.flipkart.dropwizard.hibernate.utils.PaginationUtils;
import com.google.common.collect.Multimap;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by rishabh.sood on 17/08/16.
 */
public abstract class AbstractDAOBase<E> extends io.dropwizard.hibernate.AbstractDAO<E> {

    public AbstractDAOBase(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void deleteById(int id) {
        E e = get(id);
        delete(e);
    }

    public void delete(E e) {
        this.currentSession().delete(e);
    }

    public E save(E e) {
        return persist(e);
    }

    public Serializable saveWithoutUpdate(E e) {
        return this.currentSession().save(e);
    }

    public E findById(int id) {
        return get(id);
    }

    protected Criteria buildCriteria(Map<String, List<?>> filter) {
        return buildCriteria(criteria(), filter);
    }

    protected Criteria buildCriteria(Criteria criteria, Map<String, List<?>> filter){
        for (String key : filter.keySet()) {
            List<?> values = filter.get(key);
            switch (values.size()) {
                case 0:
                    break;
                case 1:
                    criteria.add(Restrictions.eq(key, values.get(0)));
                    break;
                default:
                    criteria.add(Restrictions.in(key, values));
                    break;
            }
        }
        return criteria;
    }

    protected Criteria buildCriteriaWithAlias(Map<String, List<?>> filter, Map<String, String> criteriaMap){
        Criteria c = criteria();
        for(String key: criteriaMap.keySet()){
            c.createCriteria(key, criteriaMap.get(key));
        }
        return buildCriteria(c, filter);
    }

    protected List<Object> validateAndCastValue(String fieldName, Collection<String> fieldValues) {
        List<Object> validValues = new ArrayList<>();
        for (String fieldValue : fieldValues) {
            validValues.add(validateAndCastValue(fieldName, fieldValue));
        }
        return validValues;
    }

    protected Object validateAndCastValue(String fieldName, String fieldValue) {
        Object value = fieldValue;
        try {
            Field field = getEntityClass().getDeclaredField(fieldName);
            Class klazz = field.getType();
            if (klazz.isEnum()) {
                value = Enum.valueOf(klazz, fieldValue);
            } else if (klazz.isPrimitive()) {
                switch (klazz.getName()) {
                    case "Boolean":
                    case "bool":
                        value = Boolean.parseBoolean(fieldValue);
                        break;
                    case "Short":
                    case "short":
                        value = Short.parseShort(fieldValue);
                    case "Integer":
                    case "int":
                        value = Integer.parseInt(fieldValue);
                        break;
                    case "Long":
                    case "long":
                        value = Long.parseLong(fieldValue);
                        break;
                    case "Double":
                    case "double":
                        value = Double.parseDouble(fieldValue);
                        break;
                    case "Float":
                    case "float":
                        value = Float.parseFloat(fieldValue);
                        break;
                    case "char":
                    case "Character":
                        value = fieldValue.charAt(0);
                        break;
                    case "byte":
                    case "Byte":
                        value = Byte.parseByte(fieldValue);
                        break;
                }
            } else {
                throw new IllegalArgumentException(String.format("Field %d is not primitive/enum", fieldName));
            }
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(String.format("Invalid field %s", fieldName));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Invalid value %s for field", fieldValue, fieldName));
        }
        return value;
    }

    private void setFilter(Multimap<String, String> filter, Criteria criteria, List<String> notFilter) {
        for (String column : filter.keySet()) {
            if (!filter.get(column).isEmpty()) {
                if (notFilter.contains(column)) {
                    criteria.add(Restrictions.not(Restrictions.in(column, validateAndCastValue(column, filter.get(column)))));
                } else {
                    criteria.add(Restrictions.in(column, validateAndCastValue(column, filter.get(column))));
                }
            }
        }
    }

    public long getAllCount(String sortColumn, String order, Multimap<String, String> filter) {
        List<String> notFilter = Collections.EMPTY_LIST;
        return getAllCount(sortColumn, order, filter, notFilter);
    }

    public long getAllCount(String sortColumn, String order, Multimap<String, String> filter, List<String> notFilter) {
        Criteria criteria = criteria();
        setFilter(filter, criteria, notFilter);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    public List<E> getAll(String sortColumn, String order, Multimap<String, String> filter, int pageNumber, int pageSize) {
        List<String> notFilter = Collections.EMPTY_LIST;
        return getAll(sortColumn, order, filter, pageNumber, pageSize, notFilter);
    }

    public List<E> getAll(String sortColumn, String order, Multimap<String, String> filter, int pageNumber, int pageSize,
        List<String> notFilter) {
        Criteria criteria = criteria();
        if (StringUtils.isNotEmpty(sortColumn)) {
            Order sort = null;
            if ("asc".equalsIgnoreCase(order)) {
                sort = Order.asc(sortColumn);
            } else if ("desc".equalsIgnoreCase(order)) {
                sort = Order.desc(sortColumn);
            } else {
                throw new IllegalArgumentException("order can be asc or desc");
            }
            criteria.addOrder(sort);
        }
        setFilter(filter, criteria, notFilter);
        if (pageNumber >= 0) {
            PaginationUtils.paginate(criteria, pageNumber, pageSize);
        }
        return list(criteria);
    }
}

