package com.bosch.rcm.service.util;

import com.bosch.rcm.domain.Signal;
import com.bosch.rcm.domain.SignalData;
import com.bosch.rcm.domain.SignalError;
import com.bosch.rcm.domain.Threshold;
import com.bosch.rcm.domain.constant.SignalConstants;
import com.bosch.rcm.domain.dto.ErrorDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CommonUtil {
    private final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public int checkStatus(Signal signal, SignalData signalData) {
        String dataType = signal.getDataType() != null ? signal.getDataType().getName() : null;
        if (dataType != null) {
            switch (dataType) {
                case SignalConstants.DATA_TYPE_INT16:
                case SignalConstants.DATA_TYPE_INT32:
                case SignalConstants.DATA_TYPE_FLOAT: {
                    return signalNumericError(signalData, signal);
                }
                case SignalConstants.DATA_TYPE_BOOL: {
                    return signalBoolError(signalData, signal);
                }
                default: {
                    return 0;
                }
            }
        }
        return -1;
    }

    private int signalNumericError(SignalData signalData, Signal signal) {
        if (signal.getThresholds() == null) {
            return -1;
        }
        Threshold thresholdTooLow = extractThresholdByLevel(signal.getThresholds(), SignalConstants.THRESHOLD_MIN_MIN);
        Threshold thresholdLow = extractThresholdByLevel(signal.getThresholds(), SignalConstants.THRESHOLD_MIN);
        Threshold thresholdHigh = extractThresholdByLevel(signal.getThresholds(), SignalConstants.THRESHOLD_MAX);
        Threshold thresholdTooHigh = extractThresholdByLevel(signal.getThresholds(), SignalConstants.THRESHOLD_MAX_MAX);
        if (thresholdTooLow == null || thresholdLow == null || thresholdHigh == null || thresholdTooHigh == null) {
            return -1;
        }
        double thresholdTooLowValue;
        double thresholdLowValue;
        double thresholdHighValue;
        double thresholdTooHighValue;
        try {
            thresholdTooLowValue = Double.parseDouble(thresholdTooLow.getValues());
            thresholdLowValue = Double.parseDouble(thresholdLow.getValues());
            thresholdHighValue = Double.parseDouble(thresholdHigh.getValues());
            thresholdTooHighValue = Double.parseDouble(thresholdTooHigh.getValues());
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
        double signalDataValue = Double.parseDouble(String.valueOf(signalData.getValues()));
        //Not normal if greater than high and lower than low
        if (signalDataValue < thresholdLowValue || signalDataValue > thresholdHighValue) {
            // Error less than threshold too low
            if (signalDataValue < thresholdTooLowValue) {
                return 1;
            }
            // Error greater than threshold too high
            else if (signalDataValue > thresholdTooHighValue) {
                return 5;
            }
            // Error between low-tooLow
            else if (signalDataValue >= thresholdTooLowValue && signalDataValue <= thresholdLowValue) {
                return 2;
            }
            // Error between high-tooHigh
            else if (signalDataValue >= thresholdHighValue && signalDataValue <= thresholdTooHighValue) {
                return 4;
            }
        } else {
            return 3;
        }
        return -1;
    }

    private int signalBoolError(SignalData signalData, Signal signal) {
        Threshold thresholdBool = extractThresholdByLevel(signal.getThresholds(), "");
        if (thresholdBool == null) {
            return -1;
        }
        String thresholdValueRaw = thresholdBool.getValues();
        double signalDataValueRaw = (double) signalData.getValues();
        boolean thresholdValue = thresholdValueRaw.equalsIgnoreCase("true");
        boolean signalDataValue = signalDataValueRaw == 1;
        // Case of value receive is 0 or 1
        // Error at threshold true || Error at threshold false
        if (signalDataValue == thresholdValue) {
            return 5;
        } else {
            return 3;
        }
    }

    public ErrorDataDTO errorDataDTOMapping(SignalError signalError, Integer digit, String dataType) {
        ErrorDataDTO errorDataDTO = new ErrorDataDTO();
        errorDataDTO.setId(signalError.getId());
        errorDataDTO.setName(signalError.getName());
        errorDataDTO.setAck(signalError.isAck());
        errorDataDTO.setThreshold(signalError.getThreshold());
        errorDataDTO.setTimestamp(signalError.getTimestamp());
        errorDataDTO.setValues(signalError.getValues());
        errorDataDTO.setDigit(digit);
        errorDataDTO.setDataType(dataType);
        return errorDataDTO;
    }

    public Threshold extractThresholdByLevel(Set<Threshold> thresholdSet, String level) {
        return thresholdSet.stream()
            .filter(item -> item.getLevel().equalsIgnoreCase(level))
            .findAny()
            .orElse(null);
    }
}
