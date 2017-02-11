package com.vaka.hotel_manager.repository.util.mysql;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.repository.exception.ConstraintViolationException;
import com.vaka.hotel_manager.repository.exception.ConstraintViolationType;
import com.vaka.hotel_manager.repository.util.SQLExceptionParser;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

/**
 * Created by Iaroslav on 2/6/2017.
 */
public class MysqlExceptionParser implements SQLExceptionParser {
    private Map<String, String> domainFieldNameByTableFieldName;

    @Override
    public ConstraintViolationException parse(SQLIntegrityConstraintViolationException e) {
        ConstraintViolationType violationType = getViolationType(e.getMessage());
        String violatedTableFieldName = getViolatedTableFieldName(e.getMessage(), violationType);
        String violatedDomainModelFieldName = getDomainFieldNameByTableFieldName().get(violatedTableFieldName);
        return new ConstraintViolationException(e.getMessage(), e, violationType, violatedDomainModelFieldName);
    }

    private ConstraintViolationType getViolationType(String exceptionMessage) {
        if (exceptionMessage.startsWith("Cannot add or update a child row: a foreign key constraint fails")) {
            return ConstraintViolationType.FOREIGN_KEY_CREATE;
        } else if (exceptionMessage.startsWith("Cannot delete or update a parent row: a foreign key constraint fails")) {
            return ConstraintViolationType.FOREIGN_KEY_DELETE_OR_UPDATE;
        } else if (exceptionMessage.matches("Column '[\\w]+' cannot be null")) {
            return ConstraintViolationType.NOT_NULL;
        } else if (exceptionMessage.matches("Duplicate entry '[\\w]+' for key '[\\w]+'")) {
            return ConstraintViolationType.DUPLICATE_ENTRY;
        }
        return ConstraintViolationType.UNDEFINED;
    }

    private String getViolatedTableFieldName(String exceptionMessage, ConstraintViolationType type) {
        switch (type) {
            case DUPLICATE_ENTRY: {
                int openingQuote = exceptionMessage.substring(0, exceptionMessage.length() - 2).lastIndexOf('\'');
                return exceptionMessage.substring(openingQuote + 1, exceptionMessage.length() - 1);
            }
            case FOREIGN_KEY_CREATE: {
//                String secondPart = exceptionMessage.split("FOREIGN KEY")[1];
//                int openingQuote = secondPart.indexOf('`');
//                int closingQuote = secondPart.indexOf('`', openingQuote + 1);
//                return secondPart.substring(openingQuote + 1, closingQuote);
            }
            case FOREIGN_KEY_DELETE_OR_UPDATE: {
                String secondPart = exceptionMessage.split("FOREIGN KEY")[1];
                int openingQuote = secondPart.indexOf('`');
                int closingQuote = secondPart.indexOf('`', openingQuote + 1);
                return secondPart.substring(openingQuote + 1, closingQuote);
            }
            case NOT_NULL: {
                return exceptionMessage.substring(exceptionMessage.indexOf('\'') + 1, exceptionMessage.lastIndexOf('\''));
            }
            default:
                throw new UnsupportedOperationException();//TODO change to proper exception
        }
    }

    public Map<String, String> getDomainFieldNameByTableFieldName() {
        if (domainFieldNameByTableFieldName == null) {
            domainFieldNameByTableFieldName = ApplicationContextHolder.getContext().getBean("domainFieldNameByTableFieldName");
        }
        return domainFieldNameByTableFieldName;
    }
}
