package com.bosch.rcm.service;

import com.bosch.rcm.domain.Method;
import com.bosch.rcm.domain.Signal;
import com.bosch.rcm.domain.SignalData;
import com.bosch.rcm.domain.constant.MethodConstants;
import com.bosch.rcm.domain.constant.SignalConstants;
import org.springframework.stereotype.Service;

@Service
public class DiagnoseService {

    public Double methodResolve(Double result, Signal sigOne, Method methodOne, Method methodTwo, SignalData dataOne, SignalData dataTwo) {
        Double temp = (Double) dataOne.getValues();
        if (sigOne.getDataType().getName().equalsIgnoreCase(SignalConstants.DATA_TYPE_BOOL)) {
            // Boolean signal
            int value1 = temp.intValue();
            int value2 = 0;
            if (dataTwo != null) {
                temp = (Double) dataTwo.getValues();
                value2 = temp.intValue();
            }
            if (methodOne != null) {
                value1 = value1 != 0 ? 0 : 1;
            }
            if (methodTwo != null) {
                switch (methodTwo.getName()) {
                    case MethodConstants.AND: {
                        result = (double) (value1 & value2);
                        break;
                    }
                    case MethodConstants.OR: {
                        result = (double) (value1 | value2);
                        break;
                    }
                }
            } else {
                result = (double) value1;
            }
        } else {
            // Numeric signal
            Double value1 = (Double) dataOne.getValues();
            Double value2 = 0d;
            if (dataTwo != null) {
                value2 = (Double) dataTwo.getValues();
            }
            if (methodOne != null) {
                switch (methodOne.getName()) {
                    case MethodConstants.x01: {
                        value1 = value1 * 0.1;
                        break;
                    }
                    case MethodConstants.x001: {
                        value1 = value1 * 0.01;
                        break;
                    }
                    case MethodConstants.x10: {
                        value1 = value1 * 10;
                        break;
                    }
                    case MethodConstants.x100: {
                        value1 = value1 * 100;
                        break;
                    }
                    case MethodConstants.SUB: {
                        value1 = -value1;
                        break;
                    }
                }
            }
            if (methodTwo != null) {
                switch (methodTwo.getName()) {
                    case MethodConstants.GREATER: {
                        if (value1 > value2) {
                            result = 1d;
                            break;
                        }
                        result = (double) 0;
                        break;
                    }
                    case MethodConstants.LESSER: {
                        if (value1 < value2) {
                            result = 1d;
                            break;
                        }
                        result = (double) 0;
                        break;
                    }
                    case MethodConstants.EQUAL: {
                        if (value1.equals(value2)) {
                            result = 1d;
                            break;
                        }
                        result = (double) 0;
                        break;
                    }
                    case MethodConstants.NOT_EQUAL: {
                        if (!value1.equals(value2)) {
                            result = 1d;
                            break;
                        }
                        result = (double) 0;
                        break;
                    }
                    case MethodConstants.ADD: {
                        result = value1 + value2;
                        break;
                    }
                    case MethodConstants.SUB: {
                        result = value1 - value2;
                        break;
                    }
                    case MethodConstants.MULTI: {
                        result = value1 * value2;
                        break;
                    }
                    case MethodConstants.DIV: {
                        result = value1 / value2;
                        break;
                    }
                }
            } else {
                result = value1;
            }
        }
        return result;
    }
}
