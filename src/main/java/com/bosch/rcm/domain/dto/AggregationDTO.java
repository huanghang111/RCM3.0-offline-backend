package com.bosch.rcm.domain.dto;

import com.bosch.rcm.domain.SignalData;
import com.bosch.rcm.domain.SignalError;

public class AggregationDTO {

    public static class SignalErrorDTO {
        private String name;
        private String level;
        private SignalError firstOccurrence;
        private SignalError lastOccurrence;
        private long totalOccurrence;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public SignalError getFirstOccurrence() {
            return firstOccurrence;
        }

        public void setFirstOccurrence(SignalError firstOccurrence) {
            this.firstOccurrence = firstOccurrence;
        }

        public SignalError getLastOccurrence() {
            return lastOccurrence;
        }

        public void setLastOccurrence(SignalError lastOccurrence) {
            this.lastOccurrence = lastOccurrence;
        }

        public long getTotalOccurrence() {
            return totalOccurrence;
        }

        public void setTotalOccurrence(long totalOccurrence) {
            this.totalOccurrence = totalOccurrence;
        }
    }

    public static class CountErrorDTO {
        private String name;
        private long occurrence;
        private String level;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getOccurrence() {
            return occurrence;
        }

        public void setOccurrence(long occurrence) {
            this.occurrence = occurrence;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public static class SignalErrorGroupByName {
        private String id; //Group by name but return in id field
        private long total;

        public String getId() {
            return id;
        }
    }

    public static class SignalDataDTO {
        private String id;
        private SignalData lastOccurrence;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public SignalData getLastOccurrence() {
            return lastOccurrence;
        }

        public void setLastOccurrence(SignalData lastOccurrence) {
            this.lastOccurrence = lastOccurrence;
        }
    }
    public static class SignalFaultErrorDTO {
        private String id;
        private String level;
        private SignalError lastOccurrence;
        private long totalOccurrence;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public SignalError getLastOccurrence() {
            return lastOccurrence;
        }

        public void setLastOccurrence(SignalError lastOccurrence) {
            this.lastOccurrence = lastOccurrence;
        }

        public long getTotalOccurrence() {
            return totalOccurrence;
        }

        public void setTotalOccurrence(long totalOccurrence) {
            this.totalOccurrence = totalOccurrence;
        }
    }

    public static class SignalErrorDTONoCount {
        private String name;
        private String level;
        private SignalError firstOccurrence;
        private SignalError lastOccurrence;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public SignalError getFirstOccurrence() {
            return firstOccurrence;
        }

        public void setFirstOccurrence(SignalError firstOccurrence) {
            this.firstOccurrence = firstOccurrence;
        }

        public SignalError getLastOccurrence() {
            return lastOccurrence;
        }

        public void setLastOccurrence(SignalError lastOccurrence) {
            this.lastOccurrence = lastOccurrence;
        }
    }
}
